package orderbook.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "orderbook.service",
        "orderbook.config",
        "orderbook.serialization",
        "orderbook.handler"

})
public class TestConfiguration {

}