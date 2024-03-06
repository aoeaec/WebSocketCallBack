package orderbook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import orderbook.handler.BinanceWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebConfig {

    @Autowired
    BinanceWebSocketHandler binanceWebSocketHandler;

    @Value("${app.webSocket.url}")
    private String webSocketUri;

    @Value("${app.rest.url}")
    private String restUri;
    @Bean
    public WebSocketConnectionManager wsConnectionManager() {

        return new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                binanceWebSocketHandler,
                this.webSocketUri);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(restUri).build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
