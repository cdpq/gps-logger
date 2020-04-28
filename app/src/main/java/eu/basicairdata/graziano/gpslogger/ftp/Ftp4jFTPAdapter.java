package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;

/** Subclass of FTPClientAdapter that implements FTPClient from the ftp4j library.
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPClientAdapter
 * @see "http://www.sauronsoftware.it/projects/ftp4j/"
 */
public class Ftp4jFTPAdapter extends FTPClientAdapter {

    protected int security = 0;

    protected FTPClient client;

    /** Constructor allowing host, port, user, password and security arguments.
     *
     * @param host The host name of the FTP server (111.222.333.444, example.com...)
     * @param port The port of the FTP server
     * @param user The user used to logon to the FTP server
     * @param password The user's password used to logon to the FTP server
     * @param security The encryption method (0 - standard, 1 - implicit TSL/SSL, 2 - explicit TSL/SSL)
     */
    Ftp4jFTPAdapter(String host, int port, String user, String password, int security) {
        super(host, port, user, password);

        if (security != 0 && security != 1 && security != 2) {
            throw new IllegalArgumentException("security argument must be 0, 1 or 2");
        }

        this.security = security;

        client = new FTPClient();
        client.setSecurity(security);
    }

    public int getSecurity() {
        return security;
    }

    @Override
    public boolean isActive() {
        return client.isConnected();
    }

    @Override
    public void connect() throws FTPClientAdapterException {
        if (client.isConnected()) {
            throw new IllegalStateException("Client is already connected to host");
        }

        client.setCharset("UTF-8");

        try {
            client.connect(getHost(), getPort());
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Couldn't connect to host");
            throw new FTPClientAdapterException("Couldn't connect to host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Connected to host");
    }

    @Override
    public void disconnect() throws FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected in order to disconnect");
        }

        try {
            client.disconnect(true);
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Couldn't disconnect from host");
            throw new FTPClientAdapterException("Couldn't disconnect from host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Disconnected from host");
    }

    public void forceDisconnect() throws FTPClientAdapterException {
        try {
            client.disconnect(false);
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Couldn't force disconnect from host");
            throw new FTPClientAdapterException("Couldn't disconnect from host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Force disconnected from host");
    }

    @Override
    public void login() throws FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to login");
        }

        if (client.isAuthenticated()) {
            return;
        }

        try {
            client.login(getUser(), getPassword());
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - login: Couldn't login to host");
            throw new FTPClientAdapterException("Couldn't login to host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - login: Logged in to host");
    }

    @Override
    public void logout() throws FTPClientAdapterException {
        if (!client.isConnected() || !client.isAuthenticated()) {
            throw new IllegalStateException("Client must be connected/authenticated to host in order to login");
        }

        try {
            client.logout();
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - logout: Couldn't logged out from host");
            throw new FTPClientAdapterException("Couldn't logout from host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - logout: Logged out from host");
    }

    @Override
    public void upload(File file) throws FTPClientAdapterException, IOException {
        if (!client.isConnected() || !client.isAuthenticated()) {
            throw new IllegalStateException("Client must be connected to host in order to upload a file");
        }

        if (file == null) {
            throw new IllegalArgumentException("file argument is null");
        }

        try {
            client.upload(file);
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: Couldn't upload file " + file.getName() + " to host");
            throw new FTPClientAdapterException("Couldn't upload file " + file.getName() + " to host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: File " + file.getName() + " uploaded to host");
    }

    @Override
    public File download(String file) throws FTPClientAdapterException, IOException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public void changeDirectory(String directory) throws FTPClientAdapterException {
        if (!client.isConnected() || !client.isAuthenticated()) {
            throw new IllegalStateException("Client must be connected/authenticated in order to upload a file");
        }

        try {
            client.changeDirectory(directory);
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: Couldn't change directory in host");
            throw new FTPClientAdapterException("Couldn't change directory in host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: Directory in host changed");
    }
}
