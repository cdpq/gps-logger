package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;

public class Ftp4jFTPAdapter extends FTPClientAdapter {

    private FTPClient client;

    public Ftp4jFTPAdapter(String host, int port, String user, String password, int security) {
        super(host, port, user, password);

        client = new FTPClient();
        client.setSecurity(security);
    }

    @Override
    public void connect() throws FTPClientAdapterException, IOException {
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
