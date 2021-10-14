package ru.noughtscrosses.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noughtscrosses.dto.AuthResponse;
import ru.noughtscrosses.dto.TokenRequest;
import ru.noughtscrosses.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService=authService;
    }

    @PostMapping("/token")
    public AuthResponse token(@RequestBody TokenRequest req) {
        return new AuthResponse(authService.createAccessToken(req.getName()));
    }
}
