package com.visa.innovation.paymentservice.odata.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.stereotype.Component;

@Component
public class EdmProvider extends CsdlAbstractEdmProvider {

	public static final String NAMESPACE = "OData.Payments";

	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_AUTHORIZATION_NAME = "authorization";
	public static final String ET_SALE_NAME = "sale";
	public static final String ET_CAPTURE_NAME = "capture";

	public static final FullQualifiedName CT_PAYMENT = new FullQualifiedName(NAMESPACE, "payment");

	public static final FullQualifiedName ET_AUTHORIZATION_FQN = new FullQualifiedName(NAMESPACE,
			ET_AUTHORIZATION_NAME);
	public static final FullQualifiedName ET_SALE_FQN = new FullQualifiedName(NAMESPACE, ET_SALE_NAME);
	public static final FullQualifiedName ET_CAPTURE_FQN = new FullQualifiedName(NAMESPACE, ET_CAPTURE_NAME);

	public static final String ES_AUTHORIZATIONS_NAME = "authorizations";
	public static final String ES_SALES_NAME = "sales";
	public static final String ES_CAPTURES_NAME = "captures";

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {

		// creating Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);

		// adding EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		entityTypes.add(getEntityType(ET_AUTHORIZATION_FQN));
		entityTypes.add(getEntityType(ET_SALE_FQN));
		entityTypes.add(getEntityType(ET_CAPTURE_FQN));

		// ComplexTypes
		List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
		complexTypes.add(getComplexType(CT_PAYMENT));
		schema.setComplexTypes(complexTypes);

		schema.setEntityTypes(entityTypes);

		// adding EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// adding to the schemas
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	public CsdlEntityContainer getEntityContainer() {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(CONTAINER, ES_AUTHORIZATIONS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_SALES_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_CAPTURES_NAME));

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;

	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

		if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}
		return null;
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

		if (entityContainer.equals(CONTAINER)) {
			if (entitySetName.equalsIgnoreCase(ES_AUTHORIZATIONS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_AUTHORIZATIONS_NAME);
				entitySet.setType(ET_AUTHORIZATION_FQN);
				return entitySet;
			} else if (entitySetName.equalsIgnoreCase(ES_SALES_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_SALES_NAME);
				entitySet.setType(ET_SALE_FQN);
				return entitySet;
			} else if (entitySetName.equalsIgnoreCase(ES_CAPTURES_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_CAPTURES_NAME);
				entitySet.setType(ET_CAPTURE_FQN);
				return entitySet;
			}
		}
		return null;
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
		// this method is called for one of the EntityTypes that are configured
		// in the Schema
		if (entityTypeName.equals(ET_AUTHORIZATION_FQN)) {

			// create EntityType properties
			CsdlProperty authorizationId = new CsdlProperty().setName(EdmConstants.authorizationId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty amount = new CsdlProperty().setName(EdmConstants.amount)
					.setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
			CsdlProperty currency = new CsdlProperty().setName(EdmConstants.currency)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty created = new CsdlProperty().setName(EdmConstants.created)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty status = new CsdlProperty().setName(EdmConstants.status)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty orderId = new CsdlProperty().setName(EdmConstants.orderId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// CsdlProperty merchantId = new
			// CsdlProperty().setName(EdmConstants.merchantId)
			// .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// CsdlProperty payment = new
			// CsdlProperty().setName(EdmConstants.payment).setType(new
			// CsdlComplexType()
			// .setName(CT_PAYMENT.getName()).setProperties(Arrays.asList(type,
			// accountNumber)).getBaseTypeFQN());
			CsdlProperty payment = new CsdlProperty().setName(EdmConstants.payment)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName(EdmConstants.authorizationId);

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ET_AUTHORIZATION_NAME);
			entityType
					.setProperties(Arrays.asList(authorizationId, amount, currency, created, orderId, status, payment));
			entityType.setKey(Collections.singletonList(propertyRef));
			return entityType;
		} else if (entityTypeName.equals(ET_SALE_FQN)) {

			// create EntityType properties
			CsdlProperty saleId = new CsdlProperty().setName(EdmConstants.saleId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty amount = new CsdlProperty().setName(EdmConstants.amount)
					.setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
			CsdlProperty currency = new CsdlProperty().setName(EdmConstants.currency)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty created = new CsdlProperty().setName(EdmConstants.created)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty status = new CsdlProperty().setName(EdmConstants.status)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty orderId = new CsdlProperty().setName(EdmConstants.orderId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// CsdlProperty merchantId = new
			// CsdlProperty().setName(EdmConstants.merchantId)
			// .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// CsdlProperty payment = new
			// CsdlProperty().setName(EdmConstants.payment).setType(CT_PAYMENT);
			CsdlProperty payment = new CsdlProperty().setName(EdmConstants.payment)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName(EdmConstants.saleId);

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ET_SALE_NAME);
			entityType.setProperties(Arrays.asList(saleId, amount, currency, created, orderId, status, payment));
			entityType.setKey(Collections.singletonList(propertyRef));
			return entityType;

		} else if (entityTypeName.equals(ET_CAPTURE_FQN)) {

			// create EntityType properties
			CsdlProperty captureId = new CsdlProperty().setName(EdmConstants.captureId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty amount = new CsdlProperty().setName(EdmConstants.amount)
					.setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
			CsdlProperty currency = new CsdlProperty().setName(EdmConstants.currency)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty created = new CsdlProperty().setName(EdmConstants.created)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty status = new CsdlProperty().setName(EdmConstants.status)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty orderId = new CsdlProperty().setName(EdmConstants.orderId)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// TODO: Add merchant Id when merchant Defined Data is fixed.
			// CsdlProperty merchantId = new
			// CsdlProperty().setName(EdmConstants.merchantId)
			// .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			// CsdlProperty payment = new
			// CsdlProperty().setName(EdmConstants.payment).setType(CT_PAYMENT);
			CsdlProperty payment = new CsdlProperty().setName(EdmConstants.payment)
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName(EdmConstants.captureId);

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ET_CAPTURE_NAME);
			entityType.setProperties(Arrays.asList(captureId, amount, currency, created, orderId, status, payment));
			entityType.setKey(Collections.singletonList(propertyRef));
			return entityType;

		}

		return null;
	}

	public CsdlComplexType getComplexType(FullQualifiedName complexTypeFQNName) throws ODataException {
		if (NAMESPACE.equals(complexTypeFQNName.getNamespace())) {
			if (CT_PAYMENT.getName().equals(complexTypeFQNName.getName())) {

				CsdlProperty type = new CsdlProperty().setName(EdmConstants.type)
						.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
				CsdlProperty accountNumber = new CsdlProperty().setName(EdmConstants.accountNumber)
						.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
				CsdlComplexType entityType = new CsdlComplexType();
				entityType.setName(CT_PAYMENT.getName());
				entityType.setProperties(Arrays.asList(type, accountNumber));
				return entityType;
			}

		}

		return null;

	}

}
