package orderbook.service;

import orderbook.config.TestConfiguration;
import orderbook.handler.BinanceWebSocketHandler;
import orderbook.model.OrderBookResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
class OrderBookServiceTest {

    @Autowired
    OrderBookService orderBookService;
    @Test
    void updateOrderBook() {
        orderBookService.updateOrderBook(OrderBookResponseDto.builder().build());
    }
}