1. This is a new order book program written in Java. 
It creates a local snapshot using the RestWebservice 
and then update the local snapshot by accessing partial 
updates from WebSocket. This local snapshot is shown at regular intervals

-------------
To run the app, either open the project in any IDE and run the main method in Application.java
Or
Create a jar file and run with the command
 java -jar <JAR FILE NAME> <MARKET_SYMBOL>

MARKET_SYMBOL is optional and if not provided, value from the application.yml will be taken.
Other things which can we controlled from yml file are
1. Depth of the snapshot
2. Number of bids and asks, shows in the printout, using attribute :: numberOfEntriesToPrint
3. We can configure to either show first N orders, or last N orders, using attribute ::showLatestOrders
--------------
Todo List
1. Connect to the Rest API - Done
2. Connect to the WebSocket API - Done
3. fetch the most recent Order Book from the Binance Exchange in real time - Done
4. User should be able to specify the market for which Order book is fetched -- Done
5. The order book should be updated with events from Binance websocket API. - Done
6. displays first 3 orders on both buy and sell sides on a single line - Done
7. Orders should be arranged by price - Done
8. App should be terminated by Ctrl + C - Done
9. Move all config out to yml file -- Done
--------

Next Steps :
Updating of the localOrderBook should be better, a Treemap or similar sorted data structure will be cleaner.



