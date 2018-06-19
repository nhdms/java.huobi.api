/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author X
 */
public class Request {

//    org.slf4j.Logger logger = LoggerFactory.getLogger(Request.class);

    public static enum Method {
        GET,
        POST
    }
    private String url;
    private Map<String, String> headers = new HashMap<>();
    private Proxy proxy = null;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
    HttpClient client = HttpClientBuilder.create().build();
    private boolean jsonContentType;

    public Request() {
        setDefaultHeader();
    }

    public Request(String url) {
        this.url = url;
        setDefaultHeader();
    }

    public Request(String url, boolean jsonContentType) {
        this.url = url;
        this.jsonContentType = jsonContentType;
        setDefaultHeader();
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    private void setDefaultHeader() {
        this.headers.put("User-Agent", USER_AGENT);
        if (jsonContentType) {
            this.headers.put("Content-type", "application/json");
        }
    }

    public RequestResponse get() throws RequestException {
        try {
            HttpGet request = new HttpGet(url);

            // add request header
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
            // set proxy
            if (proxy != null) {
                HttpHost proxy = new HttpHost(this.proxy.getHost(), this.proxy.getPort(), "http");

                RequestConfig config = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();

                request.setConfig(config);
            }

            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return new RequestResponse(response.getStatusLine().getStatusCode(), result.toString());
        } catch (IOException e) {
            throw new RequestException(e.getMessage() + " (" + e.getClass() + ")");
        }
    }

    public RequestResponse post(String data) throws RequestException {
        try {
            HttpPost post = new HttpPost(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }

            // set proxy
            if (proxy != null) {
                HttpHost proxy = new HttpHost(this.proxy.getHost(), this.proxy.getPort(), "http");

                RequestConfig config = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();

                post.setConfig(config);
            }

            post.setHeader("Content-type", "application/json");
            StringEntity postingString = new StringEntity(data);
            post.setEntity(postingString);
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return new RequestResponse(response.getStatusLine().getStatusCode(), result.toString());
        } catch (IOException e) {
            throw new RequestException(e.getMessage() + " (" + e.getClass() + ")");
        }
    }

    public void setProxy(String host, int port) {
        this.proxy = new Proxy(host, port);
    }

    private static class Proxy {

        private String host;
        private int port;

        public Proxy(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

}
