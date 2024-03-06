package orderbook.handler;

import orderbook.config.TestConfiguration;
import orderbook.model.OrderBookOrders;
import orderbook.model.OrderBookResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
public class WebSocketCallBackHandlerTest {

    @Autowired
    WebSocketCallBackHandler webSocketCallBackHandler;
    @Test
    public void onResponse() {
        OrderBookResponseDto orderBookResponseDto = OrderBookResponseDto.builder()
                .s("s")
                .lastUpdateId(1l)
                .bids(List.of(OrderBookOrders.builder().price("1111").quantity(".090").build()))
                .asks(List.of(OrderBookOrders.builder().price("1111").quantity(".090").build())) .build();
        webSocketCallBackHandler.onResponse(orderBookResponseDto);

    }

    @Test
    public void setCallback() {
    }
}