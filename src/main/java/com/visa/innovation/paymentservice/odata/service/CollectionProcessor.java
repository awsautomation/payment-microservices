package com.visa.innovation.paymentservice.odata.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByItem;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.service.AuthServiceVDP;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.wrappers.vdp.AuthWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.CaptureWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.SaleWrapperVDP;

public class CollectionProcessor implements EntityCollectionProcessor {

	@Autowired
	AuthWrapperVDP authWrapper;

	@Autowired
	AuthServiceVDP authService;

	@Autowired
	SaleWrapperVDP saleWrapper;

	@Autowired
	CaptureWrapperVDP captureWrapper;

	private OData odata;
	private ServiceMetadata serviceMetaData;

	@Override
	public void init(OData odata, ServiceMetadata serviceMetaData) {
		this.odata = odata;
		this.serviceMetaData = serviceMetaData;
	}

	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

		String appId = request.getHeader("x-api-key");
		String userId = request.getHeader("on-behalf-of-user");
		String cardId = request.getHeader("x-card-id");
		FilterMetaData filterMetaData = new FilterMetaData.Builder().appId(appId).cardId(cardId).userId(userId).build();
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		UriResource uriResource = resourceParts.get(0);
		if (!(uriResource instanceof UriResourceEntitySet)) {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourceParts.get(0);

		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		EntityCollection targetEntityCollection = new EntityCollection();
		EntityCollection returnEntityCollection = new EntityCollection();

		if (resourceParts.size() == 1) { // no navigation
			if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_AUTHORIZATION_FQN)) {
				targetEntityCollection = authWrapper.authEntityCollection(filterMetaData);
			} else if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_SALE_FQN)) {
				targetEntityCollection = saleWrapper.saleEntityCollection(filterMetaData);
			} else if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_CAPTURE_FQN)) {
				targetEntityCollection = captureWrapper.captureEntityCollection(filterMetaData);
			}
		}

		List<Entity> entityList = targetEntityCollection.getEntities();
		FilterOption filterOption = uriInfo.getFilterOption();

		if (filterOption != null) {
			// Apply $filter system query option
			try {

				Iterator<Entity> entityIterator = entityList.iterator();

				// Evaluate the expression for each entity
				// If the expression is evaluated to "true", keep the entity
				// otherwise remove it from the entityList
				while (entityIterator.hasNext()) {
					// To evaluate the the expression, create an instance of the
					// Filter Expression Visitor and pass
					// the current entity to the constructor
					Entity currentEntity = entityIterator.next();
					Expression filterExpression = filterOption.getExpression();
					FilterExpressionVisitor expressionVisitor = new FilterExpressionVisitor(currentEntity);

					// Start evaluating the expression
					Object visitorResult = filterExpression.accept(expressionVisitor);

					// The result of the filter expression must be of type
					// Edm.Boolean
					if (visitorResult instanceof Boolean) {
						if (!Boolean.TRUE.equals(visitorResult)) {
							// The expression evaluated to false (or null), so
							// we have to remove the currentEntity from
							// entityList
							entityIterator.remove();
						}
					} else {
						throw new ODataApplicationException("A filter expression must evaulate to type Edm.Boolean",
								HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
					}
				}

			} catch (ExpressionVisitException e) {
				throw new ODataApplicationException("Exception in filter evaluation",
						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
			}
		}

		CountOption countOption = uriInfo.getCountOption();
		if (countOption != null) {
			boolean isCount = countOption.getValue();
			if (isCount) {
				targetEntityCollection.setCount(entityList.size());
			}
		}

		OrderByOption orderByOption = uriInfo.getOrderByOption();
		if (orderByOption != null) {
			List<OrderByItem> orderItemList = orderByOption.getOrders();
			final OrderByItem orderByItem = orderItemList.get(0);
			Expression expression = orderByItem.getExpression();
			if (expression instanceof Member) {
				UriInfoResource resourcePath = ((Member) expression).getResourcePath();
				UriResource uriResourceOrderBy = resourcePath.getUriResourceParts().get(0);
				if (uriResourceOrderBy instanceof UriResourcePrimitiveProperty) {
					EdmProperty edmProperty = ((UriResourcePrimitiveProperty) uriResource).getProperty();
					final String sortPropertyName = edmProperty.getName();

					// do the sorting for the list of entities
					Collections.sort(entityList, new Comparator<Entity>() {

						// we delegate the sorting to the native sorter of
						// Integer and String
						public int compare(Entity entity1, Entity entity2) {
							int compareResult = 0;

							if (sortPropertyName.equals(EdmConstants.saleId)
									|| sortPropertyName.equals(EdmConstants.accountNumber)
									|| sortPropertyName.equals(EdmConstants.currency)
									|| sortPropertyName.equals(EdmConstants.status)
									|| sortPropertyName.equals(EdmConstants.accountNumber)
									|| sortPropertyName.equals(EdmConstants.authorizationId)) {

								String propertyValue1 = (String) entity1.getProperty(sortPropertyName).getValue();
								String propertyValue2 = (String) entity2.getProperty(sortPropertyName).getValue();

								compareResult = propertyValue1.compareTo(propertyValue2);
							} else if (sortPropertyName.equals(EdmConstants.amount)) {
								Double double1 = (Double) entity1.getProperty(sortPropertyName).getValue();
								Double double2 = (Double) entity2.getProperty(sortPropertyName).getValue();
								compareResult = double1.compareTo(double2);
							} else {
								try {
									throw new ODataApplicationException(
											"Order by not supported for the property " + sortPropertyName,
											HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
								} catch (ODataApplicationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							// if 'desc' is specified in the URI, change the
							// order of the list
							if (orderByItem.isDescending()) {
								return -compareResult;
							}

							return compareResult;
						}
					});
				}
			}
		}

		SkipOption skipOption = uriInfo.getSkipOption();
		if (skipOption != null) {
			int skipNumber = skipOption.getValue();
			if (skipNumber >= 0) {
				if (skipNumber <= entityList.size()) {
					entityList = entityList.subList(skipNumber, entityList.size());
				} else {
					// The client skipped all entities
					entityList.clear();
				}
			} else {
				throw new ODataApplicationException("Invalid value for $skip",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
			}
		}

		// handle $top
		TopOption topOption = uriInfo.getTopOption();
		if (topOption != null) {
			int topNumber = topOption.getValue();
			if (topNumber >= 0) {
				if (topNumber <= entityList.size()) {
					entityList = entityList.subList(0, topNumber);
				} // else the client has requested more entities than available
					// => return what we have
			} else {
				throw new ODataApplicationException("Invalid value for $top",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
			}
		}

		for (Entity entity : entityList) {
			returnEntityCollection.getEntities().add(entity);
		}

		SelectOption selectOption = uriInfo.getSelectOption();

		// step 3: create a serializer based on the requested format
		ODataSerializer serializer = odata.createSerializer(responseFormat);

		// step 4: serialize the content into inputstream
		String selectList = odata.createUriHelper().buildContextURLSelectList(edmEntityType, null, selectOption);
		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList).build();

		final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl)
				.count(countOption).select(selectOption).build();

		try {
			SerializerResult serializedContent = serializer.entityCollection(serviceMetaData, edmEntityType,
					returnEntityCollection, opts);
			response.setContent(serializedContent.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// step 5: configure the response
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}
}
