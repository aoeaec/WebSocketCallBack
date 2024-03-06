package orderbook.service;


import jakarta.annotation.PostConstruct;
import orderbook.exception.BinanceException;
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



    private final Map<String, List<OrderBookOrders>> localOrderBook = new ConcurrentHashMap<>();

    private long lastUpdateIdTracker = 0;

    private long lastEventId = 0;

@PostConstruct
    public void init(){
    localOrderBook.put(ASKS, new LinkedList<>());
    localOrderBook.put(BIDS,new LinkedList<>());
}

    @Override
    public void run(String... args) throws Exception {

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
        updateLocalOrderBook(localOrderBook.get(ASKS), responseDto.getAsks());
        updateLocalOrderBook(localOrderBook.get(BIDS), responseDto.getBids());
        printOrderBook(localOrderBook, responseDto.getLastUpdateId());
    }

    protected void printOrderBook(Map<String, List<OrderBookOrders>> localOrderBook, long lastUpdatedId) {
        OrderBook orderBook = new OrderBook();
        orderBook.setLastUpdateId(lastUpdatedId);
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
            orderBook.getBids().addAll(localOrderBook.get(BIDS).stream().limit(3).toList());
            orderBook.getAsks().addAll(localOrderBook.get(ASKS).stream().limit(3).toList());
        }
        orderBook.getBids().sort(Comparator.comparing(OrderBookOrders::getPrice));
        orderBook.getAsks().sort(Comparator.comparing(OrderBookOrders::getPrice).reversed());
        System.out.println(orderBook);
    }

    private void updateLocalOrderBook(List<OrderBookOrders> currentLocalBook, List<OrderBookOrders> orderBookOrdersSet) {
        List<OrderBookOrders> bookOrdersToAdd = orderBookOrdersSet.stream().filter(orderBookOrders -> !(orderBookOrders.getNumericalValueForQuantity().compareTo(BigDecimal.ZERO) == 0))
                .toList();

        for (OrderBookOrders entry : orderBookOrdersSet) {
            currentLocalBook.remove(entry);
        }

        currentLocalBook.addAll(bookOrdersToAdd);


    }

    private void startUpProcessing(){
        connectionManager.start();
    }


    private void getDepthSnapshot() {
        OrderBookResponseDto orderBookSnapshot;
    try {
        orderBookSnapshot = webClient.get().retrieve().bodyToMono(OrderBookResponseDto.class).block();
    } catch (Exception e) {
        throw new BinanceException("Pease verify server connectivity and the argument passed", e);
    }

        this.lastUpdateIdTracker = orderBookSnapshot.getLastUpdateId();
        localOrderBook.put(ASKS, orderBookSnapshot.getAsks());
        localOrderBook.put(BIDS, orderBookSnapshot.getBids());
    }

    protected Map<String, List<OrderBookOrders>> getLocalOrderBook(){
    return localOrderBook;
    }

}
