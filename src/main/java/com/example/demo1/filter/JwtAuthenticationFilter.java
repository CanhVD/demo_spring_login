//package com.example.demo1.filter;
//
//import com.example.demo1.service.jwt.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Value("${api.prefix}")
//    private String apiPrefix;
//
//    private final JwtService jwtService;
//
//    public JwtAuthenticationFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws IOException, ServletException {
//        if (isBypassToken(request)) {
//            filterChain.doFilter(request, response); //enable bypass
//            return;
//        }
//        try {
//            final String authHeader = request.getHeader("Authorization");
//            final String jwt = authHeader.substring(7);
//            if (jwtService.isTokenValid(jwt)) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            throw new Exception("Token invalid");
//        } catch (Exception exception) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write(exception.getMessage());
//        }
//    }
//
//    private boolean isBypassToken(@NonNull HttpServletRequest request) {
//        final List<Pair<String, String>> bypassTokens = Arrays.asList(
//                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
//
//                // Swagger
//                Pair.of("/api-docs", "GET"),
//                Pair.of("/api-docs/**", "GET"),
//                Pair.of("/swagger-resources", "GET"),
//                Pair.of("/swagger-resources/**", "GET"),
//                Pair.of("/configuration/ui", "GET"),
//                Pair.of("/configuration/security", "GET"),
//                Pair.of("/swagger-ui/**", "GET"),
//                Pair.of("/swagger-ui.html", "GET"),
//                Pair.of("/swagger-ui/index.html", "GET")
//        );
//
//        String requestPath = request.getServletPath();
//        String requestMethod = request.getMethod();
//
//        for (Pair<String, String> token : bypassTokens) {
//            String path = token.getFirst();
//            String method = token.getSecond();
//            // Check if the request path and method match any pair in the bypassTokens list
//            if (requestPath.matches(path.replace("**", ".*"))
//                    && requestMethod.equalsIgnoreCase(method)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
