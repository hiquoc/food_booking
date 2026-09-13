package com.huy.food.filters;

import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.securities.UserPrincipal;
import com.huy.food.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel){
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("WebSocket message received: {}", accessor);
        if (accessor == null) {
            return message;
        }
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("STOMP CONNECT received, authHeaderPresent={}", authHeader != null);
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                String token = authHeader.substring(7);
                try{
                    UserPrincipal userPrincipal = jwtTokenProvider.validateAndGetPrincipal(token);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userPrincipal,null, List.of(new SimpleGrantedAuthority("ROLE_"+userPrincipal.role())));
                    accessor.setUser(authentication);
                    log.info("WebSocket authenticated: {}", userPrincipal.userId());
                }catch(Exception e){
                    log.warn("Token parse error",e);
                    throw new ForbiddenException("Invalid token");
                }
            }else{
                log.warn("Authorization header not present");
                throw new ForbiddenException("Missing authorization header");
            }
        }
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            validateDestinationRole(accessor);
        }
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            validateSubscription(accessor);
        }
        return message;
    }

    private void validateDestinationRole(StompHeaderAccessor accessor) {
        if (!"/app/location".equals(accessor.getDestination())) {
            return;
        }

        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication == null ||
                authentication.getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_SHIPPER"))) {
            throw new ForbiddenException("Only shippers can send location");
        }
    }
    private void validateSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination == null) {
            return;
        }
        Authentication authentication = (Authentication) accessor.getUser();

        if (destination.startsWith("/topic/delivery/") && !authentication.getAuthorities().equals("ROLE_USER")){
            throw new ForbiddenException("Only users can subscribe to delivery location");
        }
    }
}
