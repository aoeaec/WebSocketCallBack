package orderbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookResponseDto {

    @JsonProperty("E")
    public long e;
    public String s;
    @JsonProperty("U")
    public long u;

    public ArrayList<OrderBookOrders> a;
    public ArrayList<OrderBookOrders> b;

    @Override
    public String toString() {
        return "OrderBookResponseDto{" +
                "e=" + e +
                ", s='" + s + '\'' +
                ", u=" + u +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}