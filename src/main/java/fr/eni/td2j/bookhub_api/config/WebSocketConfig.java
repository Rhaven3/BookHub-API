package fr.eni.td2j.bookhub_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;

    public WebSocketConfig(StompAuthChannelInterceptor stompAuthChannelInterceptor) {
        this.stompAuthChannelInterceptor = stompAuthChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200") // ton front
                .withSockJS(); // fallback si WebSocket bloqué par un proxy/firewall
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic pour du broadcast, /queue pour du point-à-point (via convertAndSendToUser)
        registry.enableSimpleBroker("/topic", "/queue");
        // préfixe pour les messages envoyés PAR le client VERS le serveur (@MessageMapping)
        registry.setApplicationDestinationPrefixes("/app");
        // préfixe utilisé en interne pour identifier les destinations "utilisateur"
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }
}
