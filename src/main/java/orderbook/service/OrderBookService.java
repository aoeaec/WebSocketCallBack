package orderbook.service;


import orderbook.handler.BinanceWebSocketHandler;
import orderbook.handler.WebSocketCallBackHandler;
import orderbook.model.OrderBook;
import orderbook.model.OrderBookOrders;
import orderbook.model.OrderBookResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static orderbook.constant.AppConstant.ASKS;
import static orderbook.constant.AppConstant.BIDS;

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

    @Autowired
    @Value("${app.config.showLatestOrders}")
    private boolean showLatestOrders;

    @Autowired
    @Value("${app.config.numberOfEntriesToPrint}")
    private int numberOfEntriesToPrint;

    private OrderBook orderBook = new OrderBook();

    private final Map<String, List<OrderBookOrders>> localOrderBook = new ConcurrentHashMap<>();

    private long lastUpdateIdTracker = 0;

    private long lastEventId = 0;



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

        final Consumer<OrderBookResponseDto> processOrderBook = new Consumer<OrderBookResponseDto>() {
            @Override
            public void accept(OrderBookResponseDto orderBookResponseDto) {
                if (orderBookResponseDto.getLastUpdateId() > lastUpdateIdTracker && (lastEventId + 1 == orderBookResponseDto.getInitialUpdateId() || lastEventId == 0)) {
                    lastUpdateIdTracker = orderBookResponseDto.getLastUpdateId();
                    lastEventId = orderBookResponseDto.getLastUpdateId();;
                    updateOrderBook(orderBookResponseDto);
                }
                else {
                    System.out.println("Not updating ");
                }
            }
        };
        callBackHandler.setCallback(processOrderBook);
    }

    protected void updateOrderBook(OrderBookResponseDto responseDto) {
        updateOrderBookOrders(ASKS, responseDto.getAsks());
        updateOrderBookOrders(BIDS, responseDto.getBids());
        printOrderBook();
    }

    protected void printOrderBook() {
        orderBook.setLastUpdateId(this.lastUpdateIdTracker);
        if(showLatestOrders) {
            localOrderBook.get(BIDS).forEach(bid -> {
                if (orderBook.getBids().size() >= numberOfEntriesToPrint) {
                    orderBook.getBids().removeLast();
                }
                orderBook.getBids().addFirst(bid);
            });

            localOrderBook.get(ASKS).forEach(ask -> {
                if (orderBook.getAsks().size() >= numberOfEntriesToPrint) {
                    orderBook.getAsks().removeLast();
                }
                orderBook.getAsks().addFirst(ask);
            });
        } else {
            orderBook.getBids().clear();
            orderBook.getAsks().clear();
            orderBook.getBids().addAll(localOrderBook.get(BIDS).subList(0,numberOfEntriesToPrint).stream().collect(Collectors.toList()));
            orderBook.getAsks().addAll(localOrderBook.get(ASKS).subList(0,numberOfEntriesToPrint).stream().collect(Collectors.toList()));
        }
        orderBook.getBids().sort(Comparator.comparing(OrderBookOrders::getPrice));
        orderBook.getAsks().sort(Comparator.comparing(OrderBookOrders::getPrice).reversed());
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
        this.lastUpdateIdTracker = orderBookSnapshot.getLastUpdateId();
       // orderBook.setLastUpdateId(orderBookSnapshot.getLastUpdateId());
        localOrderBook.put(ASKS, orderBookSnapshot.getAsks());
        //orderBook.setAsks(orderBookSnapshot.asks.stream().collect(Collectors.toList()));
        localOrderBook.put(BIDS, orderBookSnapshot.getBids());
    }

}
