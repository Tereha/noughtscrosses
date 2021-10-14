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
import org.springframework.web.bind.annotation.RequestBody;
import ru.noughtscrosses.db.model.User;
import ru.noughtscrosses.db.repository.UserRepository;
import ru.noughtscrosses.dto.TokenRequest;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.AuthResponse;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    private static final long EXPIRES_IN_MILLISECONDS_FOR_ONE_DAY = 60L * 60L * 24L * 1000L;

    private ThreadLocal<Claims> claims = new ThreadLocal<>();

    @Value("${APPSETTING_JWT_SECRET:vlk3T9dC%hp3O5m34n8$Xc4345#x6pghf}")
    private String secret;

    @Value("${citycheers.jwt.uuid:a74fgt96-f522-33e9-802a-5aa537777bd8}")
    private String jwtUUID;

    private static final String CHARSET_NAME = "UTF-8";
    private static final String ENV_ID = "env_id";
    private static final String AUTHORIZATION_SCHEME_BEARER = "bearer";

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createAccessToken(final String name) {
        User user = userRepository.findByName(name).orElseGet(() ->
            userRepository.add(new User(name)));
        return createToken(user.getId(), EXPIRES_IN_MILLISECONDS_FOR_ONE_DAY);
    }

    private String createToken(UUID userId, long expirationMilliseconds) {
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder().setSubject(userId.toString()).claim(ENV_ID, jwtUUID);
        builder.setExpiration(new Date(nowMillis + expirationMilliseconds));
        builder.setIssuedAt(new Date(nowMillis));
        builder.signWith(SignatureAlgorithm.HS512, getSecret());

        return builder.compact();
    }

    private byte[] getSecret() {
        return secret.getBytes(Charset.forName(CHARSET_NAME));
    }

    public ThreadLocal<Claims> getClaims() {
        return claims;
    }

    public UUID getCurrentUserId() {
        return UUID.fromString(claims.get().getSubject());
    }

    public void parse(HttpServletRequest httpReq) {
        Claims claims =
            Jwts.parser()
                .setSigningKey(getSecret())
                .parseClaimsJws(resolveToken(httpReq))
                .getBody();
        if (!jwtUUID.equals(claims.get(ENV_ID))) {
            log.debug("Invalid environment:{},expected:{}", claims.get(ENV_ID), jwtUUID);
            throw new ApiException(AuthResponse.TOKEN_INVALID);
        }
        getClaims().set(claims);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null) {
            throw new ApiException(AuthResponse.AUTH_NOT_PRESENT.withDescription(getRequestTitle(req)));
        }
        if (!bearerToken.toLowerCase().startsWith(AUTHORIZATION_SCHEME_BEARER)) {
            throw new ApiException(AuthResponse.AUTH_INVALID.withDescription(getRequestTitle(req)));
        }
        return bearerToken.split(" ")[1];
    }

    private String getRequestTitle(HttpServletRequest req) {
        return new StringBuilder(req.getMethod()).append(" ").append(req.getRequestURI()).toString();
    }
}
