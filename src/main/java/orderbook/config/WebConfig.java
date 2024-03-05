package orderbook.config;

import orderbook.handler.BinanceWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebConfig {

    @Autowired
    BinanceWebSocketHandler binanceWebSocketHandler;
    private final String webSocketUri = "wss://stream.binance.com:9443/ws/bnbbtc@depth";
    private final String restUri = "https://api.binance.com/api/v3/depth?symbol=BNBBTC&limit=5";
    @Bean
    public WebSocketConnectionManager wsConnectionManager() {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                binanceWebSocketHandler,
                this.webSocketUri);

        return manager;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(restUri).build();
    }
}
