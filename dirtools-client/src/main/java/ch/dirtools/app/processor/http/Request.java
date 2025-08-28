/*=============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Che-Hung Lin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *===========================================================================*/

package ch.dirtools.app.http;

import ch.platform.common.logging.C6LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class Request {

    private final String protocol = "http";
    private final String host;
    private final int port;
    private Logger log = null;
    private HttpClient httpClient;

    public Request(String host, int port) {
        this.log = C6LogFactory.getLogger(this.getClass());
        this.host = host;
        this.port = port;
        httpClient = HttpClientBuilder.create().build();
    }

    private URIBuilder getURIBuilder(String uri) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.setScheme(protocol);
        uriBuilder.setHost(host);
        uriBuilder.setPort(port);
        return uriBuilder;
    }

    public Response<String> get(String uri, Map<String, String> queryParams) throws IOException, RuntimeException, URISyntaxException {
        URIBuilder uriBuilder = getURIBuilder(uri);
        HttpGet getMethod = new HttpGet();
        if (queryParams != null && queryParams.size() > 0) {
            for (Map.Entry<String, String> each : queryParams.entrySet()) {
                uriBuilder.addParameter(each.getKey(), each.getValue());
            }
        }
        URI finalUri = uriBuilder.build();
        getMethod.setURI(finalUri);
        log.info("Sending GET request to URI " + finalUri);
        return sendGET(getMethod);
    }

    private Response<String> sendGET(HttpGet getMethod) throws IOException {
        try {
            HttpResponse response = httpClient.execute(getMethod);
            return processResponse(response.getStatusLine().getStatusCode(), response.getEntity());
        } catch (IOException e) {
            log.error("IO Error executing GET method: " + e);
            getMethod.abort();
            throw e;
        } catch (Exception e) {
            getMethod.abort();
            throw e;
        }
    }

    private Response<String> processResponse(int status, HttpEntity entity) throws IOException {
        log.info("STATUS returned from server " + status);
        Response<String> response = new Response<>(status);
        if (entity == null) {
            log.warn("Empty entity");
        } else {
            response.setData(EntityUtils.toString(entity, "UTF-8"));
        }
        return response;
    }

}