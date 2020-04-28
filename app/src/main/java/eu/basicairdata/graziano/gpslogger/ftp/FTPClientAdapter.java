package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.File;
import java.io.IOException;

/** Base class for any FTP client adapter.
 *
 * Mainly used to make any FTP library like Apache Commons Net or ftp4j work with the FTPHandler
 * class, hence the word "adapter".
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPHandler
 */
public abstract class FTPClientAdapter {

    private long index = 0;

    protected String host = "";
    protected int port = 21;
    protected String user = "";
    protected String password = "";

    /** Constructor allowing host, port, user and password arguments.
     *
     * @param host The host name of the FTP server (111.222.333.444, example.com...)
     * @param port The port of the FTP server
     * @param user The user used to logon to the FTP server
     * @param password The user's password used to logon to the FTP server
     */
    FTPClientAdapter(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;

        createIndex();
    }

    public long getIndex() {
        return index;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    /** Create the index for this adapter.
     *
     * The index is created using the current timestamp in milliseconds.
     */
    private void createIndex() {
        this.index = System.currentTimeMillis();
    }

    /** Whether or not the FTP client is active (e.g., is still connected/authenticated to the FTP server).
     *
     * @return True if the FTP client is active, false otherwise
     */
    public abstract boolean isActive();

    /** Connect to the FTP server.
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     */
    public abstract void connect() throws FTPClientAdapterException;

    /** Disconnect from the FTP server.
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     */
    public abstract void disconnect() throws FTPClientAdapterException;

    /** Force disconnect from the FTP server.
     *
     * Keep in mind that this method should mainly be called in a catch block, right after an exception
     * has been thrown, or in a finally block. In that sense, this method should ensure that the
     * connection between the server and the client is closed, meaning that an exception should be
     * thrown only in fatal cases (e.g., the server was shutdown unexpectedly).
     *
     * @throws FTPClientAdapterException
     */
    public abstract void forceDisconnect() throws FTPClientAdapterException;

    /** Login to the FTP server.
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     */
    public abstract void login() throws FTPClientAdapterException;

    /** Logout from the FTP server.
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     */
    public abstract void logout() throws FTPClientAdapterException;

    /** Upload a file to the FTP server.
     *
     * @param file The file to upload
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     * @throws IOException Thrown when an I/O exception was thrown while manipulating the file
     */
    public abstract void upload(File file) throws FTPClientAdapterException, IOException;

    /** Download a file from the FTP server.
     *
     * @param file The file name/path to download
     *
     * @return The file that has been downloaded
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     * @throws IOException Thrown when an I/O exception was thrown while manipulating the file
     */
    public abstract File download(String file) throws FTPClientAdapterException, IOException;

    /** Change the working directory on the FTP server.
     *
     * @param directory The directory name/path to change to
     *
     * @throws FTPClientAdapterException Thrown when an exception was thrown by the underlying library
     */
    public abstract void changeDirectory(String directory) throws FTPClientAdapterException;
}
