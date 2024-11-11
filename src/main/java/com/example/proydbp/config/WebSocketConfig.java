package com.example.proydbp.config;


import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.user.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;
                switch (Objects.requireNonNull(accessor.getCommand())) {
                    case CONNECT:
                        String authToken = accessor
                                .getFirstNativeHeader("Authorization")
                                .substring(7);

                        String userEmail = jwtService.extractUsername(authToken);
                        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
                        var user = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                authToken,
                                userDetails.getAuthorities());

                        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null)
                            jwtService.validateToken(authToken, userEmail);
                        else
                            throw new UnauthorizeOperationException("Unauthorized message");

                        accessor.setUser(user);
                        break;
                    case SEND:
                        Principal principal = accessor.getUser();
                        if (principal != null)
                            System.out.println("\n\nUser Connected: " + principal.getName() + "\n\n");

                        break;
                    case DISCONNECT:
                        Principal principalDisconnect = accessor.getUser();
                        if (principalDisconnect != null)
                            System.out.println("\n\nUser Disconnected: " + principalDisconnect.getName() + "\n\n");

                        break;
                    default:
                        break;
                }
                return message;
            }
        });
    }
}
