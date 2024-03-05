package orderbook.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import orderbook.model.OrderBookResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class BinanceWebSocketHandler implements WebSocketHandler {


    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }


    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ObjectMapper m = new ObjectMapper();
        OrderBookResponseDto orderBookResponseDto = m.readValue((String) message.getPayload(), OrderBookResponseDto.class);
        System.out.println(orderBookResponseDto);
        System.out.println(message.getPayload());
    }

    /**
     * Error handling.
     */

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    public boolean supportsPartialMessages() {
        return false;
    }


}