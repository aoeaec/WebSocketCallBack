package orderbook.service;


import orderbook.handler.BinanceWebSocketHandler;
import orderbook.handler.WebSocketCallBackHandler;
import orderbook.model.OrderBook;
import orderbook.model.OrderBookOrders;
import orderbook.model.OrderBookResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class OrderBookService implements CommandLineRunner {

    @Autowired
    private WebClient webClient;

    @Autowired
    private WebSocketCallBackHandler callBackHandler;

    @Autowired
    private WebSocketConnectionManager connectionManager;

    @Autowired
    private BinanceWebSocketHandler webSocketHandler;

    private OrderBook orderBook = new OrderBook();

    private Map<String, List<OrderBookOrders>> localOrderBook = new ConcurrentHashMap<>();

    private long updateIdTracker = 0;



    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter word!");

        //Scanner scanner = new Scanner(System.in);
        //String line = scanner.nextLine();
        //System.out.println(line);
        startUpProcessing();

        getDepthSnapshot();

        continueProcessing();
    }

    private void continueProcessing() {
        //System.out.println(map);

        final Consumer<OrderBookResponseDto> processOrderBook = new Consumer<OrderBookResponseDto>() {
            @Override
            public void accept(OrderBookResponseDto orderBookResponseDto) {
                if (true) {
                    updateOrderBook(orderBookResponseDto);
                }
            }


        };
        callBackHandler.setCallback(processOrderBook);

    }

    protected void updateOrderBook(OrderBookResponseDto responseDto) {
        updateOrderBookOrders("ASKS", responseDto.getAsks());
        updateOrderBookOrders("BIDS", responseDto.getBids());
        printOrderBook();
    }

    private void printOrderBook() {
        orderBook.setLastUpdateId(this.updateIdTracker);
        localOrderBook.get("BIDS").forEach(bid -> {
            if(orderBook.getBids().size()>=3) {
                //System.out.println("To be removed :: " + orderBook.getBids().getLast());
                orderBook.getBids().removeLast();
            }

            orderBook.getBids().addFirst(bid);
            //System.out.println("To be addded :: " + orderBook.getBids().getFirst());
        });

        localOrderBook.get("ASKS").forEach(ask -> {
            if(orderBook.getAsks().size()>=3) {
                orderBook.getAsks().removeLast();
            }
            orderBook.getAsks().addFirst(ask);
        });
        System.out.println(orderBook);
    }

    private void updateOrderBookOrders(String side, List<OrderBookOrders> orderBookOrdersList) {
        List<OrderBookOrders> bookOrders = orderBookOrdersList.stream().filter(orderBookOrders -> !orderBookOrders.getNumericalValueForQuantity().equals(BigDecimal.ZERO))
                .collect(Collectors.toList());
        System.out.println("Adding for " + side + " :: " + bookOrders);
        localOrderBook.get(side).addAll(bookOrders);
    }

    private void startUpProcessing(){
        connectionManager.start();
    }


    private void getDepthSnapshot() {
        OrderBookResponseDto orderBookSnapshot = webClient.get().retrieve().bodyToMono(OrderBookResponseDto.class).block();
        this.updateIdTracker = orderBookSnapshot.getLastUpdateId();
       // orderBook.setLastUpdateId(orderBookSnapshot.getLastUpdateId());
        localOrderBook.put("ASKS", orderBookSnapshot.getAsks());
        //orderBook.setAsks(orderBookSnapshot.asks.stream().collect(Collectors.toList()));
        localOrderBook.put("BIDS", orderBookSnapshot.getBids());
    }

}
