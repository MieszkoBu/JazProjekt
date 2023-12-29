package org.example;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class AniListOAuthClient {
    @Value("${aniList.clientId}")
    private String clientId;

    @Value("${aniList.clientSecret}")
    private String clientSecret;

    @Value("${aniList.clientRedirectUrl}")
    private String clientRedirectUrl;

    private final WebClient webClient;

    public AniListOAuthClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://anilist.co/api/v2/oauth").build();
    }

    public String getAuthorizationUrl(String state) {
        return "https://anilist.co/api/v2/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + clientRedirectUrl +
                "&response_type=code" +
                "&state=" + state;
    }

    public Mono<String> requestAccessToken(String authorizationCode) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("code", authorizationCode);
        data.add("redirect_uri", clientRedirectUrl);

        return webClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .retrieve()
                .bodyToMono(AuthorizationResponse.class)
                .map(AuthorizationResponse::getAccessToken)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.empty();
                });
    }

    private static class AuthorizationResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }
}