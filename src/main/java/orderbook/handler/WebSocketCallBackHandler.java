package orderbook.handler;

import orderbook.model.OrderBookResponseDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
@Component
public class WebSocketCallBackHandler
{

    private final AtomicReference<Consumer<OrderBookResponseDto>> callBack = new AtomicReference<>();


    public void onResponse(OrderBookResponseDto orderBookResponse) {
        this.callBack.get().accept(orderBookResponse);
    }

    public void setCallback(Consumer<OrderBookResponseDto> callBack) {
        this.callBack.set(callBack);
    }
}