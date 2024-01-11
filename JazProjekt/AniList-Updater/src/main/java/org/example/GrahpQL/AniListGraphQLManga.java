package org.example.GrahpQL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Model.Manga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AniListGraphQLManga {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
        EntityManager em = emf.createEntityManager();
        for (int i = 100000; i <= 100500; i++) {
            String query = "query ($id: Int) { Media (id: $id, type: MANGA) { id format status startDate{ year month day } endDate{ year month day } chapters volumes averageScore popularity favourites genres title { romaji english native } } }";
            Map<String, Object> variables = new HashMap<>();
            variables.put("id", i);

            try {
                String response = sendGraphQLRequest(query, variables);
                Manga manga = parseGraphQLResponseToManga(response, em);

                if (manga != null) {
                    em.getTransaction().begin();
                    Manga mergedManga = em.merge(manga);
                    em.getTransaction().commit();
                }
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
    private static LocalDate convertFuzzyDateIntToLocalDate(int year, int month, int day) {
        if (month == 0 || day == 0) {
            return null;
        }
        return LocalDate.of(year, month, day);
    }
    private static Manga parseGraphQLResponseToManga(String response, EntityManager em) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode mediaNode = rootNode.path("data").path("Media");
        Manga manga = new Manga();

        manga.setId(mediaNode.path("id").asInt());
        manga.setFormat(mediaNode.path("format").asText());
        manga.setStatus(mediaNode.path("status").asText());
        manga.setChapters(mediaNode.path("chapters").asInt());
        manga.setVolumes(mediaNode.path("volumes").asInt());

        int startYear = mediaNode.path("startDate").path("year").asInt();
        int startMonth = mediaNode.path("startDate").path("month").asInt();
        int startDay = mediaNode.path("startDate").path("day").asInt();
        LocalDate startDate = convertFuzzyDateIntToLocalDate(startYear, startMonth, startDay);

        int endYear = mediaNode.path("endDate").path("year").asInt();
        int endMonth = mediaNode.path("endDate").path("month").asInt();
        int endDay = mediaNode.path("endDate").path("day").asInt();
        LocalDate endDate = convertFuzzyDateIntToLocalDate(endYear, endMonth, endDay);

        manga.setStartDate(startDate);
        manga.setEndDate(endDate);

        manga.setAverageScore(mediaNode.path("averageScore").asInt());
        manga.setPopularity(mediaNode.path("popularity").asInt());
        manga.setFavourites(mediaNode.path("favourites").asInt());

        JsonNode genresNode = mediaNode.path("genres");
        List<String> genres = new ArrayList<>();
        for (JsonNode genreNode : genresNode) {
            genres.add(genreNode.asText());
        }
        manga.setGenres(genres);

        manga.setRomaji(mediaNode.path("title").path("romaji").asText());
        manga.setEnglish(mediaNode.path("title").path("english").asText());
        manga.setOriginal(mediaNode.path("title").path("native").asText());
        if (mediaNode.path("format").asText().equals("0") ||
                mediaNode.path("status").asText().equals("0")){
            return null;
        }

        return manga;
    }

}