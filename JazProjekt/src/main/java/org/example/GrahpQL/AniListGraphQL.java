package org.example.GrahpQL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Enum.MediaFormat;
import org.example.Enum.MediaSource;
import org.example.Enum.MediaStatus;
import org.example.Model.Anime;
import org.example.Model.Studio;

public class AniListGraphQL {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
        EntityManager em = emf.createEntityManager();
        for (int i = 1; i <= 300; i++) {
            String query = "query ($id: Int) { Media (id: $id, type: ANIME) { id format episodes duration status startDate{ year month day } endDate{ year month day } averageScore popularity favourites studios { edges { id } } source genres title { romaji english native } } }";
            Map<String, Object> variables = new HashMap<>();
            variables.put("id", i);

            try {
                String response = sendGraphQLRequest(query, variables);
                Anime anime = parseGraphQLResponse(response);
                em.getTransaction().begin();
                em.persist(anime);
                em.getTransaction().commit();
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        em.close();
        emf.close();
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
    public static Anime parseGraphQLResponse(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode mediaNode = rootNode.path("data").path("Media");
        Anime anime = new Anime();

        anime.setId(mediaNode.path("id").asInt());
        anime.setFormat(MediaFormat.valueOf(mediaNode.path("format").asText()));
        anime.setEpisodes(mediaNode.path("episodes").asInt());
        anime.setEpisodes_duration(mediaNode.path("duration").asInt());
        anime.setStatus(MediaStatus.valueOf(mediaNode.path("status").asText()));
        anime.setStart_date(LocalDate.of(
                mediaNode.path("startDate").path("year").asInt(),
                mediaNode.path("startDate").path("month").asInt(),
                mediaNode.path("startDate").path("day").asInt()
        ));
        anime.setEnd_date(LocalDate.of(
                mediaNode.path("endDate").path("year").asInt(),
                mediaNode.path("endDate").path("month").asInt(),
                mediaNode.path("endDate").path("day").asInt()
        ));
        anime.setAverage_score(mediaNode.path("averageScore").asInt());
        anime.setPopularity(mediaNode.path("popularity").asInt());
        anime.setFavourites(mediaNode.path("favourites").asInt());
        JsonNode studiosNode = mediaNode.path("studios").path("edges");
        List<Studio> studios = new ArrayList<>();
        for (JsonNode studioNode : studiosNode) {
            Studio studio = new Studio();
            studio.setId(studioNode.path("id").asInt());
            studios.add(studio);
        }
        anime.setStudios(studios);

        anime.setSource(MediaSource.valueOf(mediaNode.path("source").asText()));
        JsonNode genresNode = mediaNode.path("genres");
        List<String> genres = new ArrayList<>();
        for (JsonNode genreNode : genresNode) {
            genres.add(genreNode.asText());
        }
        anime.setGenres(genres);
        anime.setRomaji(mediaNode.path("title").path("romaji").asText());
        anime.setEnglish(mediaNode.path("title").path("english").asText());
        anime.setOriginal(mediaNode.path("title").path("native").asText());

        return anime;
    }
}