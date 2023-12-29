package org.example.Controllers;

import org.example.AniListOAuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AniListOAuthClient aniListOAuthClient;

    public AuthController(AniListOAuthClient aniListOAuthClient) {
        this.aniListOAuthClient = aniListOAuthClient;
    }

    @GetMapping("/login")
    public RedirectView login() {
        String state = generateState();
        String authorizationUrl = aniListOAuthClient.getAuthorizationUrl(state);
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam(name = "code") String authorizationCode,
                                 @RequestParam(name = "state") String state,
                                 Model model) {
        Mono<String> accessTokenMono = aniListOAuthClient.requestAccessToken(authorizationCode);
        model.addAttribute("accessToken", accessTokenMono.block());
        return "callback";
    }
    private String generateState() {
        return "unique-state";
    }
}
