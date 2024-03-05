package orderbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookResponseDto {

    @JsonProperty("E")
    public long e;
    public String s;
    @JsonProperty("U")
    public long u;

    public long lastUpdateId;

    @JsonProperty("a")
    public List<OrderBookOrders> asks;

    @JsonProperty("b")
    public List<OrderBookOrders> bids;

    @Override
    public String toString() {
        return "OrderBookResponseDto{" +
                "e=" + e +
                ", s='" + s + '\'' +
                ", u=" + u +
                ", asks=" + asks +
                ", bids=" + bids +
                '}';
    }
}