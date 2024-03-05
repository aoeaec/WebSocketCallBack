package orderbook.handler;

import orderbook.config.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
public class BinanceWebSocketHandlerTest {

    @Autowired
    BinanceWebSocketHandler webSocketHandler;



    @Test
    public void handleMessage() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        TextMessage textMessage = new TextMessage("{\"e\":\"depthUpdate\",\"E\":1709651684684,\"s\":\"BNBBTC\",\"U\":3330215919,\"u\":3330215990,\"b\":[[\"0.00611500\",\"2.73900000\"],[\"0.00611400\",\"8.32400000\"],[\"0.00611300\",\"4.43100000\"],[\"0.00611200\",\"8.70300000\"],[\"0.00611100\",\"6.49900000\"],[\"0.00611000\",\"4.75000000\"],[\"0.00610900\",\"1.67200000\"],[\"0.00610000\",\"35.45900000\"],[\"0.00608100\",\"36.03300000\"],[\"0.00607900\",\"11.15200000\"],[\"0.00605200\",\"0.07900000\"],[\"0.00604800\",\"3.54400000\"]],\"a\":[[\"0.00611600\",\"4.59700000\"],[\"0.00611700\",\"8.30800000\"],[\"0.00611800\",\"3.26300000\"],[\"0.00611900\",\"0.00000000\"],[\"0.00612000\",\"4.24900000\"],[\"0.00612100\",\"2.05200000\"],[\"0.00612200\",\"1.67500000\"],[\"0.00612300\",\"0.00000000\"],[\"0.00613200\",\"13.07500000\"],[\"0.00615400\",\"1.40400000\"],[\"0.00617800\",\"122.90600000\"],[\"0.00618000\",\"1.19600000\"],[\"0.00620100\",\"6.36500000\"]]}");
        webSocketHandler.handleMessage(session,textMessage);
    }
}