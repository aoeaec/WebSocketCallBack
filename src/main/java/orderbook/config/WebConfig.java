package orderbook.config;

import orderbook.handler.BinanceWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebConfig {

    private final String webSocketUri = "wss://stream.binance.com:9443/ws/bnbbtc@depth";
    private final String restUri = "";
    @Bean
    public WebSocketConnectionManager wsConnectionManager() {

        //Generates a web socket connection
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                new BinanceWebSocketHandler(),
                this.webSocketUri);

        //Will connect as soon as possible
        manager.setAutoStartup(true);

        return manager;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(restUri).build();
    }
}
