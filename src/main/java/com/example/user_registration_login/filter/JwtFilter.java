package com.example.user_registration_login.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.user_registration_login.dto.responceDto.DefaultResponse;
import com.example.user_registration_login.entity.AppUser;
import com.example.user_registration_login.repository.UserRepository;
import com.example.user_registration_login.utils.ResponseCodeUtils;
import com.example.user_registration_login.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(header, request);
            if (Objects.isNull(authenticationToken)) {
                log.info("JWTRequestFilter:[doFilterInternal] -> Not authenticated. Public request.");
            } else {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWTRequestFilter:[doFilterInternal] -> Exception: {}", e.getMessage(), e);
            DefaultResponse.builder()
                    .code(ResponseCodeUtils.UNAUTHORIZED_CODE)
                    .status(ResponseUtils.UNAUTHORIZED)
                    .message("Unauthorized")
                    .build();
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token, HttpServletRequest request) {
        if (token != null) {
            String username = JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build().verify(token.replace("Bearer ","")).getSubject();
            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                AppUser appUser = userRepository.findAppUserByEmail(username);
                if (appUser != null) {
                    request.setAttribute("user", appUser);
                }
                return usernamePasswordAuthenticationToken;
            }
        }
        return null;
    }
}
