package com.visa.innovation.paymentservice.odata.service;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.wrappers.vdp.AuthWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.CaptureWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.SaleWrapperVDP;

public class EdmEntityProcessor implements EntityProcessor {

	@Autowired
	AuthWrapperVDP authWrapperVDP;

	@Autowired
	CaptureWrapperVDP captureWrapperVDP;

	@Autowired
	SaleWrapperVDP salesWrapperVDP;

	private OData odata;
	private ServiceMetadata serviceMetaData;

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetaData = serviceMetaData;
	}

	@Override
	public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		UriResource uriResource = resourceParts.get(0);
		if (uriResource == null) {
			throw new ODataApplicationException("No EntitySet specified",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
		if (!(uriResource instanceof UriResourceEntitySet)) {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourceParts.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
		EdmEntitySet returnEntitySet = null;
		EdmEntityType returnEntityType = null;
		Entity returnEntity = null;

		// step 1.1: retrieve select option
		SelectOption selectOption = uriInfo.getSelectOption();
		String selectList = odata.createUriHelper().buildContextURLSelectList(edmEntityType, null, selectOption);

		if (resourceParts.size() == 1) { // no navigation
			returnEntitySet = edmEntitySet;
			returnEntityType = edmEntityType;
			String id = keyPredicates.get(0).getText();
			id = id.substring(1, id.length() - 1);
			if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_AUTHORIZATION_FQN)) {

				try {
					returnEntity = authWrapperVDP.getAuthById(id);
				} catch (GenericVDPException e) {
					CustomLogger.logException(e);
					e.printStackTrace();
				}
			} else if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_CAPTURE_FQN)) {
				try {
					returnEntity = captureWrapperVDP.getCaptureById(id);
				} catch (GenericVDPException e) {
					CustomLogger.logException(e);
					e.printStackTrace();
				}

			} else if (edmEntityType.getFullQualifiedName().equals(EdmProvider.ET_SALE_FQN)) {
				try {
					returnEntity = salesWrapperVDP.getSaleById(id);
				} catch (GenericVDPException e) {
					CustomLogger.logException(e);
					e.printStackTrace();
				}
			}
		} else {
			throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ROOT);
		}

		// step 2.1: check returndata
		if (returnEntity == null) {
			throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ROOT);
		}

		// step 3: create a serializer based on the requested format
		ODataSerializer serializer = odata.createSerializer(responseFormat);

		// step 4: serialize the content into inputstream
		ContextURL contextUrl = ContextURL.with().entitySet(returnEntitySet).selectList(selectList)
				.suffix(Suffix.ENTITY).build();
		EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextUrl).select(selectOption)
				.build();
		SerializerResult serializedContent = serializer.entity(serviceMetaData, returnEntityType, returnEntity, opts);

		// step 5: configure the response
		response.setContent(serializedContent.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());

		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());

	}

	@Override
	public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		// Not implemented using Olingo
	}

	@Override
	public void updateEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		// Not implemented using Olingo
	}

	@Override
	public void deleteEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo)
			throws ODataApplicationException, ODataLibraryException {
		// Not implemented using Olingo
	}

}
