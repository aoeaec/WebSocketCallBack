package orderbook.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderBookResponseDto {

    @JsonProperty("E")
    private long e;
    private String s;
    @JsonProperty("U")
    private long initialUpdateId;

    @JsonProperty("u")
    private long lastUpdateId;

    @JsonProperty("a")
    @JsonAlias("asks")
    private Set<OrderBookOrders> asks;

    @JsonProperty("b")
    @JsonAlias("bids")
    private Set<OrderBookOrders> bids;

    @Override
    public String toString() {
        return "OrderBookResponseDto{" +
                "e=" + e +
                ", s='" + s + '\'' +
                ", u=" + lastUpdateId +
                ", asks=" + asks +
                ", bids=" + bids +
                '}';
    }
}