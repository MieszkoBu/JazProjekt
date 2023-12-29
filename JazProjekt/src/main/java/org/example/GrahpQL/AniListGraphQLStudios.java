package org.example.GrahpQL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AniListGraphQLStudios {
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            String query = "query ($id: Int) {" +
                    "  Studio (id: $id) {" +
                    "    id" +
                    "    isAnimationStudio" +
                    "    name" +
                    "    siteUrl" +
                    "  }" +
                    "}";
            Map<String, Object> variables = new HashMap<>();
            variables.put("id", i);

            try {
                String response = sendGraphQLRequest(query, variables);
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String sendGraphQLRequest(String query, Map<String, Object> variables) throws IOException {
        String url = "https://graphql.anilist.co";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        String requestBody = String.format("{\"query\":\"%s\",\"variables\":%s}", query, mapToJson(variables));
        connection.getOutputStream().write(requestBody.getBytes());

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            if (connection.getResponseCode() == 429) {
                int retryAfter = connection.getHeaderFieldInt("Retry-After", 5);
                System.out.println("Rate limit exceeded. Waiting for " + retryAfter + " seconds.");
                try {
                    Thread.sleep(retryAfter * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return sendGraphQLRequest(query, variables);
            } else {
                throw e;
            }
        } finally {
            connection.disconnect();
        }
    }

    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\",");
            } else {
                json.append(entry.getValue()).append(",");
            }
        }
        if (json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");
        return json.toString();
    }
}
