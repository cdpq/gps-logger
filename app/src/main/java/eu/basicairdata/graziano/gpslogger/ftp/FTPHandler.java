package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Handles a specified FTPClientAdapter. Acts like a facade to the FTPClientAdapter subclasses.
 *
 * This class is a Singleton, meaning that it has only one instance and and it's public method are
 * static. The goal of this class is to make sure that the number of adapters at a given time is less
 * or equal to the specified limit (default is 1).
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPClientAdapter
 */
public class FTPHandler {
    private static FTPHandler instance = null;
    private FTPClientAdapter adapter = null;

    private int adapterLimit = 1;
    private List<FTPClientAdapter> adapters = new ArrayList<>();

    /** Empty constructor. */
    private FTPHandler()
    {

    }

    public static FTPHandler getInstance() {
        if (instance == null) {
            instance = new FTPHandler();
        }

        return instance;
    }

    public static FTPClientAdapter getAdapter(int adapterIndex) {
        if (adapterIndex >= getInstance().adapters.size() || adapterIndex < 0) {
            throw new IllegalArgumentException("The adapter index must be between 0 and the number of adapters minus 1");
        }

        return getInstance().adapters.get(adapterIndex);
    }

    public static void setAdapterLimit(int adapterLimit) throws FTPHandlerException {
        if (adapterLimit < getInstance().adapters.size()) {
            throw new FTPHandlerException("The value is less that the current number of adapters");
        }

        getInstance().adapterLimit = adapterLimit;

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Adapter limit set to " + adapterLimit);
    }

    public static int getAdapterLimit() {
        return getInstance().adapterLimit;
    }

    /** @deprecated */
    @Deprecated
    public static FTPClientAdapter getAdapter() {
        return getInstance().adapter;
    }

    /** @deprecated */
    @Deprecated
    public static void setAdapter(FTPClientAdapter adapter) {
        getInstance().adapter = adapter;
    }

    public static int addAdapter(FTPClientAdapter adapter) throws FTPHandlerException {
        if (getInstance().adapters.contains(adapter)) {
            throw new FTPHandlerException("The adapter is already in the list of adapters");
        }

        if (getInstance().adapters.size() == getInstance().adapterLimit) {
            throw new FTPHandlerException("The adapter limit is reached, cannot add another one");
        }

        getInstance().adapters.add(adapter);

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Added adapter to the list");
        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Number of adapters is now " + getInstance().adapters.size());

        return getInstance().adapters.indexOf(adapter);
    }

    public static void removeAdapter(FTPClientAdapter adapter) throws FTPHandlerException {
        if (!getInstance().adapters.contains(adapter)) {
            throw new FTPHandlerException("The adapter is not in the list of adapters");
        }

        if (adapter.isActive()) {
            throw new FTPHandlerException("The adapter is considered active, cannot be removed");
        }

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Removed adapter from the list");
        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Number of adapters is now " + getInstance().adapters.size());

        getInstance().adapters.remove(adapter);
    }

    public static void connect(int adapterIndex) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        if (adapterIndex >= getInstance().adapters.size() || adapterIndex < 0) {
            throw new IllegalArgumentException("The adapter index must be between 0 and the number of adapters minus 1");
        }

        getInstance().adapter.connect();
    }

    /** @deprecated */
    @Deprecated
    public static void connect() throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.connect();
    }

    public static void disconnect(int adapterIndex) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).disconnect();
    }

    /** @deprecated */
    @Deprecated
    public static void disconnect() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.disconnect();
    }

    public static void forceDisconnect(int adapterIndex) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).forceDisconnect();
    }

    /** @deprecated */
    @Deprecated
    public static void forceDisconnect() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.forceDisconnect();
    }

    public static void login(int adapterIndex) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).login();
    }

    /** @deprecated */
    @Deprecated
    public static void login() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.login();
    }

    public static void logout(int adapterIndex) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).logout();
    }

    /** @deprecated */
    @Deprecated
    public static void logout() throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.logout();
    }

    public static void upload(int adapterIndex, File file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).upload(file);
    }

    /** @deprecated  */
    @Deprecated
    public static void upload(File file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.upload(file);
    }

    public static void download(int adapterIndex, String file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).download(file);
    }

    /** @deprecated  */
    @Deprecated
    public static void download(String file) throws FTPClientAdapterException, IOException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.download(file);
    }

    public static void changeDirectory(int adapterIndex, String directory) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getAdapter(adapterIndex).changeDirectory(directory);
    }

    /** @deprecated */
    @Deprecated
    public static void changeDirectory(String directory) throws FTPClientAdapterException {
        if (getInstance().adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        getInstance().adapter.changeDirectory(directory);
    }
}
