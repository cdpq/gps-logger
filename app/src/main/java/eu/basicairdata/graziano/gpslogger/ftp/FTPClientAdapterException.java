package eu.basicairdata.graziano.gpslogger.ftp;

/** Exception thrown by any FTPClientAdapter subclasses.
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPClientAdapter
 */
public class FTPClientAdapterException extends Exception {

    /** Constructor allowing message argument.
     *
     * @param message The message of the exception
     */
    FTPClientAdapterException(String message) {
        super(message);
    }

    /** Constructor allowing message and cause arguments.
     *
     * @param message The message of the exception
     * @param cause The cause of the exception
     */
    FTPClientAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
