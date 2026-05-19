package com.vita.vitapickBack.jwtToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

//커스텀인증필터 클래스

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        try {
            // 헤더에서 토큰 꺼내기
            String token = parseBearerToken(request);
            log.info("** JwtAuthenticationFilter, token 확인 => " + token);

            if (token != null && !token.equalsIgnoreCase("null")) {

                // 토큰 검증 & claims 꺼내기
                Map<String, Object> claims = tokenProvider.validateToken(token);
                log.info("** Authenticated 결과 JWT claims: " + claims);

                String userId = (String) claims.get("id");
                
                request.setAttribute("claims", claims);

                // roleList 꺼내기 (타입 안전하게)
                ObjectMapper mapper = new ObjectMapper();
                List<String> roleList = mapper.convertValue(
                    claims.get("roleList"),
                    new TypeReference<List<String>>() {}
                );

                // 인증 완료 처리
                AbstractAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        roleList.stream()
                            .map(str -> new SimpleGrantedAuthority("ROLE_" + str))
                            .collect(Collectors.toList())
                    );

                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // SecurityContextHolder 에 인증된 유저 등록
                SecurityContext securityContext =
                    SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

            } // if_token 존재

        } catch (Exception e) {
            log.error("doFilterInternal() Exception => " + e.toString());
        } //try

        filterChain.doFilter(request, response);
    }

    // 헤더에서 토큰 파싱
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
