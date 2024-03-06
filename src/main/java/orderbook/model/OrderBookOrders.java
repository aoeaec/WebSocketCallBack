package orderbook.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import orderbook.serialization.OrderBookOrdersDeserializer;

import java.math.BigDecimal;

@JsonDeserialize(using = OrderBookOrdersDeserializer.class)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderBookOrders{
    @EqualsAndHashCode.Include
    String price;

    String quantity;

    public BigDecimal getNumericalValueForQuantity(){
        return new BigDecimal(quantity);
    }

    @Override
    public String toString() {
        return "[\"" + price + '\"' +
                ",\"" + quantity + '\"' +
                "]";
    }
}
