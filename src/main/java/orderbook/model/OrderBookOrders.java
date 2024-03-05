package orderbook.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import orderbook.serialization.OrderBookOrdersDeserializer;

@JsonDeserialize(using = OrderBookOrdersDeserializer.class)
@Data
@Builder
public class OrderBookOrders {
    String price;
    String quantity;

    @Override
    public String toString() {
        return "OrderBookOrders{" +
                "price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
