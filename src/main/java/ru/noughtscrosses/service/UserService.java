package ru.noughtscrosses.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.noughtscrosses.db.model.User;
import ru.noughtscrosses.db.repository.UserRepository;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.AuthResponse;
import ru.noughtscrosses.exception.code.DataNotFoundResponse;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService=authService;
    }

    public User getCurrentUser() {
        return userRepository.findById(authService.getCurrentUserId()).orElseThrow(() -> new ApiException(
            DataNotFoundResponse.USER_NOT_FOUND));
    }


}
