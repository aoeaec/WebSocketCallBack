package orderbook.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import orderbook.exception.BinanceException;
import orderbook.model.OrderBookResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class BinanceWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WebSocketCallBackHandler callBackHandler;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ObjectMapper m = new ObjectMapper();
        OrderBookResponseDto orderBookResponseDto = m.readValue((String) message.getPayload(), OrderBookResponseDto.class);
        this.callBackHandler.onResponse(orderBookResponseDto);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        throw new BinanceException("Error connecting WebSocket ", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}