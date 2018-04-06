package com.visa.innovation.paymentservice.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.core.ODataHandlerException;
import org.apache.olingo.server.core.ODataHttpHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import com.visa.innovation.paymentservice.odata.service.CollectionProcessor;
import com.visa.innovation.paymentservice.odata.service.EdmEntityProcessor;
import com.visa.innovation.paymentservice.odata.service.EdmProvider;

public class EdmController {

    private String baseURI = "v1/";

    private int split = 0;

    @Autowired
    private EdmProvider edmProvider;

    @Autowired
    private CollectionProcessor entityCollectionProcessor;

    @Autowired
    private EdmEntityProcessor entityProcessor;

    protected ResponseEntity<String> process(HttpServletRequest req) throws ODataApplicationException {

        try {
            OData odata = OData.newInstance();
            ServiceMetadata edm = odata.createServiceMetadata(edmProvider, new ArrayList<>());

           // ODataHandler handler = new ODataHandler(odata, edm, new ServerCoreDebugger(odata));
            ODataHttpHandler handler = new ODataHttpHandlerImpl(odata, edm);
            handler.register(entityCollectionProcessor);
            handler.register(entityProcessor);

            ODataResponse response = handler.process(createODataRequest(req, split));
            String responseStr = StreamUtils.copyToString(
                    response.getContent(), Charset.defaultCharset());
            MultiValueMap<String, String> headers = new HttpHeaders();
            Map<String, List<String>> responseHeaders = response.getAllHeaders();
            for (String key : responseHeaders.keySet()) {
                headers.put(key, responseHeaders.get(key));
            }

            return new ResponseEntity<>(responseStr, headers,
                    HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception ex) {
            //LOGGER.error(ex.getMessage(), ex);
            throw new ODataApplicationException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatusCode.INTERNAL_SERVER_ERROR
                            .getStatusCode(), Locale.ENGLISH);
        }
    }

    /**
     * Creates the o data request.
     *
     * @param httpRequest the http request
     * @param split       the split
     * @return the o data request
     * @throws ODataException
     */
    private ODataRequest createODataRequest(final HttpServletRequest httpRequest, final int split)
            throws ODataException, IOException {
            ODataRequest odRequest = new ODataRequest();

            odRequest.setBody(httpRequest.getInputStream());
            extractHeaders(odRequest, httpRequest);
            extractMethod(odRequest, httpRequest);
            extractUri(odRequest, httpRequest, split);

            return odRequest;
    }

    /**
     * Extract method.
     *
     * @param odRequest   the od request
     * @param httpRequest the http request
     * @throws ODataException the odata exception
     */
    private void extractMethod(final ODataRequest odRequest, final HttpServletRequest httpRequest)
            throws ODataException {
        try {
            HttpMethod httpRequestMethod = HttpMethod.valueOf(httpRequest.getMethod());

            if (httpRequestMethod == HttpMethod.POST) {
                String xHttpMethod = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD);
                String xHttpMethodOverride = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD_OVERRIDE);

                if (xHttpMethod == null && xHttpMethodOverride == null) {
                    odRequest.setMethod(httpRequestMethod);
                } else if (xHttpMethod == null) {
                    odRequest.setMethod(HttpMethod.valueOf(xHttpMethodOverride));
                } else if (xHttpMethodOverride == null) {
                    odRequest.setMethod(HttpMethod.valueOf(xHttpMethod));
                } else {
                    if (!xHttpMethod.equalsIgnoreCase(xHttpMethodOverride)) {
                        throw new ODataHandlerException(
                                "Ambiguous X-HTTP-Methods",
                                ODataHandlerException.MessageKeys.AMBIGUOUS_XHTTP_METHOD,
                                xHttpMethod, xHttpMethodOverride);
                    }

                    odRequest.setMethod(HttpMethod.valueOf(xHttpMethod));
                }
            } else {
                odRequest.setMethod(httpRequestMethod);
            }
        } catch (IllegalArgumentException e) {
            throw new ODataHandlerException("Invalid HTTP method" + httpRequest.getMethod(),
                    ODataHandlerException.MessageKeys.INVALID_HTTP_METHOD,
                    httpRequest.getMethod());
        }
    }

    /**
     * Extract uri.
     *
     * @param odRequest   the od request
     * @param httpRequest the http request
     * @param split       the split
     */
    private void extractUri(final ODataRequest odRequest,
                            final HttpServletRequest httpRequest, final int split) {
        String rawRequestUri = httpRequest.getRequestURL().toString().toLowerCase();

        String rawODataPath;
        if (!"".equals(httpRequest.getServletPath())) {
            int beginIndex;
            beginIndex = rawRequestUri.indexOf(baseURI);
            beginIndex += baseURI.length();
            rawODataPath = rawRequestUri.substring(beginIndex);
        } else if (!"".equals(httpRequest.getContextPath())) {
            int beginIndex;
            beginIndex = rawRequestUri.indexOf(httpRequest.getContextPath());
            beginIndex += httpRequest.getContextPath().length();
            rawODataPath = rawRequestUri.substring(beginIndex);
        } else {
            rawODataPath = httpRequest.getRequestURI();
        }

        String rawServiceResolutionUri;
        if (split > 0) {
            rawServiceResolutionUri = rawODataPath;
            for (int i = 0; i < split; i++) {
                int e = rawODataPath.indexOf("/", 1);
                if (-1 == e) {
                    rawODataPath = "";
                } else {
                    rawODataPath = rawODataPath.substring(e);
                }
            }

            int end = rawServiceResolutionUri.length() - rawODataPath.length();
            rawServiceResolutionUri = rawServiceResolutionUri.substring(0, end);
        } else {
            rawServiceResolutionUri = null;
        }

        String rawBaseUri = rawRequestUri.substring(0, rawRequestUri.length() - rawODataPath.length());

        odRequest.setRawQueryPath(httpRequest.getQueryString());
        odRequest.setRawRequestUri(rawRequestUri
                + (httpRequest.getQueryString() == null ? "" : "?"
                + httpRequest.getQueryString()));

        odRequest.setRawODataPath(rawODataPath);
        odRequest.setRawBaseUri(rawBaseUri);
        odRequest.setRawServiceResolutionUri(rawServiceResolutionUri);
    }

    /**
     * Extract headers.
     *
     * @param odRequest the od request
     * @param req       the req
     */
    private void extractHeaders(final ODataRequest odRequest,
                                final HttpServletRequest req) {
        for (Enumeration<?> headerNames = req.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = (String) headerNames.nextElement();
            List<String> headerValues = new ArrayList<>();
            for (Enumeration<?> headers = req.getHeaders(headerName); headers.hasMoreElements(); ) {
                String value = (String) headers.nextElement();
                headerValues.add(value);
            }

            odRequest.addHeader(headerName, headerValues);
        }
    }

    protected String getBaseURI() {
        return baseURI;
    }

    protected void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }
}