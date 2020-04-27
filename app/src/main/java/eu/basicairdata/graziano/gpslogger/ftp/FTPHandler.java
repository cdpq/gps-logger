package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.File;
import java.io.IOException;

public class FTPHandler {
    private static FTPHandler instance = null;
    private FTPClientAdapter adapter = null;

    private FTPHandler() {}

    public static FTPHandler getInstance() {
        if (instance == null) {
            instance = new FTPHandler();
        }

        return instance;
    }

    public static FTPClientAdapter getAdapter() {
        return getInstance().adapter;
    }

    public static void setAdapter(FTPClientAdapter adapter) {
        getInstance().adapter = adapter;
    }

    public static void connect() throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.connect();
    }

    public static void disconnect() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.disconnect();
    }

    public static void forceDisconnect() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.forceDisconnect();
    }

    public static void login() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.login();
    }

    public static void logout() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.logout();
    }

    public static void upload(File file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.upload(file);
    }

    public static void download(String file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.download(file);
    }

    public static void changeDirectory(String directory) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.changeDirectory(directory);
    }
}
