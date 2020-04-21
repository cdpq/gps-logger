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
        return instance.adapter;
    }

    public static void setAdapter(FTPClientAdapter adapter) {
        instance.adapter = adapter;
    }

    public static void connect() throws FTPClientAdapterException, IOException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.connect();
    }

    public static void disconnect() throws FTPClientAdapterException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.disconnect();
    }

    public static void login() throws FTPClientAdapterException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.login();
    }

    public static void logout() throws FTPClientAdapterException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.logout();
    }

    public static void upload(File file) throws FTPClientAdapterException, IOException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.upload(file);
    }

    public static void download(String file) throws FTPClientAdapterException, IOException {
        if (instance.adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        instance.adapter.download(file);
    }
}
