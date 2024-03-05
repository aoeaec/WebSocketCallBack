package orderbook.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import orderbook.model.OrderBookOrders;

import java.io.IOException;

public class OrderBookOrdersDeserializer extends JsonDeserializer<OrderBookOrders> {

    @Override
    public OrderBookOrders deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return OrderBookOrders.builder()
                .price(node.get(0).textValue())
                .quantity(node.get(1).textValue())
                .build();
    }
}