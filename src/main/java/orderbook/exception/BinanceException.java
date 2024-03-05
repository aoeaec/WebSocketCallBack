package orderbook.exception;

public class BinanceException extends RuntimeException{

    public BinanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinanceException() {
        super();
    }

}
