package orderbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orderbook.config.TestConfiguration;
import orderbook.model.OrderBookOrders;
import orderbook.model.OrderBookResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static orderbook.constant.AppConstant.ASKS;
import static orderbook.constant.AppConstant.BIDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
class OrderBookServiceTest {

    @Autowired
    OrderBookService orderBookService;

    @Autowired
    ObjectMapper objectMapper;



    @Test
    public void testUpdateOrderBook_AllAsksAndBidsCorrect() {
        OrderBookResponseDto webSocketEvent = getWebSocketEvent();
        orderBookService.init();
        orderBookService.updateOrderBook(webSocketEvent);

        Map<String, List<OrderBookOrders>> localOrderBook = orderBookService.getLocalOrderBook();
        assertEquals(localOrderBook.get(ASKS).size(), 13);
        assertEquals(localOrderBook.get(BIDS).size(), 12);
    }

    @Test
    public void testUpdateOrderBook_ZeroPriceAsksAndBids() {
        OrderBookResponseDto webSocketEvent = getWebSocketEvent();

        webSocketEvent.getAsks().stream().filter(ask -> ask.getPrice().equals("0.00617800")).forEach(ask -> ask.setQuantity("0.000000"));
        webSocketEvent.getBids().stream().filter(bid -> bid.getPrice().equals("0.00608100")).forEach(bid -> bid.setQuantity("0.000000"));
        orderBookService.init();
        orderBookService.updateOrderBook(webSocketEvent);
        Map<String, List<OrderBookOrders>> localOrderBook = orderBookService.getLocalOrderBook();
        assertEquals(localOrderBook.get(ASKS).size(), 12);
        assertEquals(localOrderBook.get(BIDS).size(), 11);
    }

    @Test
    public void printOrderBook() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        orderBookService.init();
        Map<String, List<OrderBookOrders>> localOrderBook = new HashMap<>();
        localOrderBook.put(ASKS,getWebSocketEvent().getAsks());
        localOrderBook.put(BIDS,getWebSocketEvent().getBids());
        orderBookService.printOrderBook(localOrderBook, 123456);
        assertEquals("{lastUpdateId:123456, \"bids\":[[\"0.00604800\",\"3.54400000\"], [\"0.00605200\",\"0.07900000\"], [\"0.00607900\",\"11.15200000\"]], \"asks\":[[\"0.00620100\",\"6.36500000\"], [\"0.00618000\",\"1.19600000\"], [\"0.00617800\",\"122.90600000\"]]}\n", outContent.toString());
    }

    private OrderBookResponseDto getWebSocketEvent() {
        try {
            return objectMapper.readValue("{\"e\":\"depthUpdate\",\"E\":1709651684684,\"s\":\"BNBBTC\",\"U\":3330215919,\"u\":3330215990,\"b\":[[\"0.00611500\",\"2.73900000\"],[\"0.00611400\",\"8.32400000\"],[\"0.00611300\",\"4.43100000\"],[\"0.00611200\",\"8.70300000\"],[\"0.00611100\",\"6.49900000\"],[\"0.00611000\",\"4.75000000\"],[\"0.00610900\",\"1.67200000\"],[\"0.00610000\",\"35.45900000\"],[\"0.00608100\",\"36.03300000\"],[\"0.00607900\",\"11.15200000\"],[\"0.00605200\",\"0.07900000\"],[\"0.00604800\",\"3.54400000\"]],\"a\":[[\"0.00611600\",\"4.59700000\"],[\"0.00611700\",\"8.30800000\"],[\"0.00611800\",\"3.26300000\"],[\"0.00611900\",\"0.00000200\"],[\"0.00612000\",\"4.24900000\"],[\"0.00612100\",\"2.05200000\"],[\"0.00612200\",\"1.67500000\"],[\"0.00612300\",\"0.00400000\"],[\"0.00613200\",\"13.07500000\"],[\"0.00615400\",\"1.40400000\"],[\"0.00617800\",\"122.90600000\"],[\"0.00618000\",\"1.19600000\"],[\"0.00620100\",\"6.36500000\"]]}",
                     OrderBookResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}