package design_patterns.creational.builder_pattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class HttpRequest {
    private final String url;

    private final String method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final String body;
    private final int timeout;

    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.queryParams = Collections.unmodifiableMap(new HashMap<>(builder.queryParams));
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getBody() { return body; }
    public int getTimeout() { return timeout; }

    @Override
    public String toString() {
        return "HttpRequest{url='" + url + "', method='" + method +
                "', headers=" + headers + ", queryParams=" + queryParams +
                ", body='" + body + "', timeout=" + timeout + "}";
    }

    // Static nested Builder class
    public static class Builder {
        private final String url; // required
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParams = new HashMap<>();
        private String body;
        private int timeout = 30000;

        public Builder(String url) {
            this.url = url;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}

// build directory
class HttpRequestDirector {

    public HttpRequest buildSimpleGet(String url) {
        return new HttpRequest.Builder(url)
                .method("GET")
                .timeout(30000)
                .build();
    }

    public HttpRequest buildAuthenticatedPost(String url, String token, String body) {
        return new HttpRequest.Builder(url)
                .method("POST")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .body(body)
                .timeout(10000)
                .build();
    }

    public HttpRequest buildInternalServiceCall(String url) {
        return new HttpRequest.Builder(url)
                .method("GET")
                .addHeader("X-Internal-Service", "true")
                .addHeader("X-Trace-Id", java.util.UUID.randomUUID().toString())
                .timeout(5000)
                .build();
    }
}

public class HttpRequestBuilderDemo {
    public static void main(String[] args) {
        // Simple GET request - just the URL
        HttpRequest get = new HttpRequest.Builder("https://api.example.com/users")
                .build();

        // POST with body and custom timeout
        HttpRequest post = new HttpRequest.Builder("https://api.example.com/users")
                .method("POST")
                .addHeader("Content-Type", "application/json")
                .body("{\"name\":\"Alice\",\"email\":\"alice@example.com\"}")
                .timeout(5000)
                .build();

        // Authenticated PUT with query parameters
        HttpRequest put = new HttpRequest.Builder("https://api.example.com/config")
                .method("PUT")
                .addHeader("Authorization", "Bearer token123")
                .addHeader("Content-Type", "application/json")
                .addQueryParam("env", "production")
                .addQueryParam("version", "2")
                .body("{\"feature_flag\":true}")
                .timeout(10000)
                .build();

        System.out.println(get);
        System.out.println(post);
        System.out.println(put);

        HttpRequestDirector director = new HttpRequestDirector();

        HttpRequest getBuilder = director.buildSimpleGet("https://api.example.com/users");
        HttpRequest postBuilder = director.buildAuthenticatedPost(
                "https://api.example.com/orders", "token123", "{\"item\":\"book\"}");
        HttpRequest internal = director.buildInternalServiceCall(
                "https://internal.service/health");

        System.out.println(getBuilder);
        System.out.println(postBuilder);
        System.out.println(internal);
    }
}

