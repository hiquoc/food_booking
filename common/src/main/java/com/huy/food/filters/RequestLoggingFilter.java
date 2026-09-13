package com.huy.food.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String query = request.getQueryString();
        String ip = request.getRemoteAddr();

        log.info(
                "Incoming request: {} {}{} from {}",
                request.getMethod(),
                request.getRequestURI(),
                query != null ? "?" + query : "",
                ip != null ? ip : ""
        );
        try{
            filterChain.doFilter(request,response);
        }
        finally{
            long duration = System.currentTimeMillis() - start;
            log.info(
                    "Request completed: {} {} -> {} ({}ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration
            );
        }
    }
}
