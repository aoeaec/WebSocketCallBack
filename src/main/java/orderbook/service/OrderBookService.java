package orderbook.service;


import orderbook.handler.BinanceWebSocketHandler;
import orderbook.handler.WebSocketCallBackHandler;
import orderbook.model.OrderBook;
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

    public static ConcurrentHashMap<String, List<BigDecimal>> map = new ConcurrentHashMap<String, List<BigDecimal>>();



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
        orderBook.setLastUpdateId(responseDto.lastUpdateId);
        responseDto.bids.forEach(bid -> {
            if(orderBook.getBids().size()>=3) {
                System.out.println("To be removed :: " + orderBook.getBids().getLast());
                orderBook.getBids().removeLast();
            }

            orderBook.getBids().addFirst(bid);
            System.out.println("To be addded :: " + orderBook.getBids().getFirst());
        });

        responseDto.asks.forEach(ask -> {
            if(orderBook.getAsks().size()>=3) {
                orderBook.getAsks().removeLast();
            }
            orderBook.getAsks().addFirst(ask);
        });
        System.out.println(orderBook);
    }

    private void startUpProcessing(){
        connectionManager.start();
    }


    private void getDepthSnapshot() {
        OrderBookResponseDto orderBook = webClient.get().retrieve().bodyToMono(OrderBookResponseDto.class).block();
        map.put("sssss", List.of(BigDecimal.TEN));
    }

    public ConcurrentHashMap<String, List<BigDecimal>> updateOrderBook() {
        return map;
    }

}
