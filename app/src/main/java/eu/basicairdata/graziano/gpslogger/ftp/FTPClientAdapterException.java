package eu.basicairdata.graziano.gpslogger.ftp;

public class FTPClientAdapterException extends Exception {

    FTPClientAdapterException(String message) {
        super(message);
    }

    FTPClientAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
