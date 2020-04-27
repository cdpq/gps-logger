package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.File;
import java.io.IOException;

public abstract class FTPClientAdapter {

    private String host = "";
    private int port = 21;
    private String user = "";
    private String password = "";

    FTPClientAdapter() {}

    FTPClientAdapter(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract void connect() throws FTPClientAdapterException, IOException;

    public abstract void disconnect() throws FTPClientAdapterException;

    public abstract void forceDisconnect() throws FTPClientAdapterException;

    public abstract void login() throws FTPClientAdapterException;

    public abstract void logout() throws FTPClientAdapterException;

    public abstract void upload(File file) throws FTPClientAdapterException, IOException;

    public abstract File download(String file) throws FTPClientAdapterException, IOException;

    public abstract void changeDirectory(String directory) throws  FTPClientAdapterException;
}
