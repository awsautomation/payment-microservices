package com.visa.innovation.paymentservice.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.cybersource.ws.client.Utility;
import com.cybersource.ws.client.XMLClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class SOAConnectionsUtils {

	@Autowired
	PropUtils propUtils;

	public <S, T> T makeSOARequest(S requestMessageSOA, Class<T> responseType) {

		T response = null;
		Document doc = null;
		Properties props = propUtils.getProperties();

		try {
			doc = objectToDocument(requestMessageSOA);
			displayDocument("SIMPLE ORDER API REQUEST:", doc);
			Document reply = XMLClient.runTransaction(doc, props);
			displayDocument("SIMPLE ORDER API RESPONSE:", reply);
			response = documentToObject(reply, responseType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	public <T> Document objectToDocument(T request) throws ParserConfigurationException, SAXException, IOException {

		CustomLogger.log(request);
		XmlFactory factory = new XmlFactory(new WstxInputFactory(), new WstxOutputFactory());
		XmlMapper xmlMapper = new XmlMapper(factory);
		// xmlMapper.setSerializationInclusion(Include.NON_NULL);
		String xml = "";
		DocumentBuilder builder = null;
		Document doc = null;

		xml = xmlMapper.writeValueAsString(request);
		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		builder = dbf.newDocumentBuilder();
		doc = builder.parse(is);

		return doc;
	}

	public <T> T documentToObject(Document doc, Class<T> responseType)
			throws JsonParseException, JsonMappingException, IOException, TransformerException {

		T value = null;

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		String xmlString = writer.toString();

		XmlMapper xmlMapper = new XmlMapper();
		value = xmlMapper.readValue(xmlString, responseType);
		CustomLogger.log(value);
		return value;
	}

	private static void displayDocument(String header, Document doc) {
		System.out.println(header);
		System.out.println(Utility.nodeToString(doc));
	}
}
