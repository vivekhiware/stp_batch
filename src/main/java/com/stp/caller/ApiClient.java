package com.stp.caller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApiClient {

    // Create an Apache HttpClient instance
    private static final CloseableHttpClient client = HttpClients.createDefault();

    public static String makePostRequest(String url, String jsonPayload) {
        try {
            // Create an HttpPost request
            HttpPost request = new HttpPost(url);
            
            // Set request headers
            request.setHeader("Content-Type", "application/json");
            
            // Set the JSON payload
            StringEntity entity = new StringEntity(jsonPayload, StandardCharsets.UTF_8);
            request.setEntity(entity);

            // Execute the request
            try (CloseableHttpResponse response = client.execute(request)) {
                // Check if the request was successful (HTTP status 200)
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Get the response body
                    HttpEntity responseEntity = response.getEntity();
                    return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                } else {
                    // Log failure if status code is not 200
                    System.out.println("Failed with status: " + statusCode);
                    return null;
                }
            }
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        }

        return null;
    }
}
