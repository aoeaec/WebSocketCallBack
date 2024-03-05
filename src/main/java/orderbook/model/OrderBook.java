package orderbook.model;

import lombok.Data;

import java.util.LinkedList;

@Data
public class OrderBook {
    private long lastUpdateId;

    private LinkedList<OrderBookOrders> bids = new LinkedList<>();

    private LinkedList<OrderBookOrders> asks = new LinkedList<>();

    @Override
    public String toString() {
        return "OrderBook{" +
                "lastUpdateId=" + lastUpdateId +
                ", bids=" + bids +
                ", asks=" + asks +
                '}';
    }
}
