package eu.basicairdata.graziano.gpslogger.ftp;

/** Exception thrown by the FTPHandler.
 *
 * @see FTPHandler
 */
public class FTPHandlerException extends Exception {

    /** Constructor allowing message argument.
     *
     * @param message The message of the exception
     */
    FTPHandlerException(String message) {
        super(message);
    }

    /** Constructor allowing message and cause arguments.
     *
     * @param message The message of the exception
     * @param cause The cause of the exception
     */
    FTPHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
