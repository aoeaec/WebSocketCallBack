package orderbook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import orderbook.handler.BinanceWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebConfig {

    @Autowired
    BinanceWebSocketHandler binanceWebSocketHandler;

    @Autowired
    private ApplicationArguments applicationArguments;

    @Value("${app.webSocket.url}")
    private String webSocketUri;

    @Value("${app.config.depthLimit:5}")
    private int depthLimit;
    @Value("${app.config.snapShotLimit:1000}")
    private int snapShotLimit;

    @Value("${app.config.symbol}")
    private String marketSymbol;

    @Value("${app.rest.url}")
    private String restUri;
    @Bean
    public WebSocketConnectionManager wsConnectionManager() {
        String symbol = ObjectUtils.isEmpty(applicationArguments.getSourceArgs()) ? marketSymbol : applicationArguments.getSourceArgs()[0];

        return new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                binanceWebSocketHandler,
                this.webSocketUri+ marketSymbol.toLowerCase()+"@depth");
    }

    @Bean
    public WebClient webClient() {
        String symbol = ObjectUtils.isEmpty(applicationArguments.getSourceArgs()) ? marketSymbol : applicationArguments.getSourceArgs()[0];
        String baseUrl = restUri + "?symbol=" + symbol.toUpperCase() + "&limit=" + snapShotLimit;
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
