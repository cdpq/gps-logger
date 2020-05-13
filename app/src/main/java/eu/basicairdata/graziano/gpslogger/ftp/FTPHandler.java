/*
 * FTPHandler - Java Class for Android
 * Copyright (C) 2020 by Anthony Blanchette-Potvin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Singleton that handles one or multiple instances of FTPClientAdapter.
 *
 * The goal of this class is to make sure that the number of adapters at any given time is less
 * or equal to the specified limit (default is 3). That said, it keeps tracks of how many adapters has
 * it's underlying client active. This avoid the system to be impacted by unclosed FTP communication(s).
 *
 * This class's methods should always be called over any FTPClientAdapter's instances' methods,
 * i.e., call FTPHandler.connect(adapterIndex) instead of implClientAdapter.connect().
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPClientAdapter
 */
public class FTPHandler {

    private static FTPHandler instance = null;

    private int adapterLimit = 3;
    private List<FTPClientAdapter> adapters = new ArrayList<>();

    /** Empty constructor. */
    private FTPHandler() { }

    /** Get the instance of FTPHandler.
     *
     * @return The instance of FTPHandler
     */
    public static FTPHandler getInstance() {
        if (instance == null) {
            instance = new FTPHandler();
        }

        return instance;
    }

    /** Get an adapter by index.
     *
     * @param adapterIndex The adapter's index
     *
     * @return The adapter, if found
     *
     * @throws FTPHandlerException Thrown if the adapter was not found
     */
    public static FTPClientAdapter getAdapter(long adapterIndex) throws FTPHandlerException {
        for (FTPClientAdapter adapter : getInstance().adapters) {
            if (adapter.getIndex() == adapterIndex) {
                return adapter;
            }
        }

        throw new FTPHandlerException("The adapter index was not found amongst adapters in the list");
    }

    /** Set the number of adapters limit.
     *
     * @param adapterLimit The new limit for the number of adapters
     *
     * @throws FTPHandlerException Thrown if the value is less than 0 or the current number of adapters
     */
    public static void setAdapterLimit(int adapterLimit) throws FTPHandlerException {
        if (adapterLimit < 0 || adapterLimit < getInstance().adapters.size()) {
            throw new FTPHandlerException("The value is less that the current number of adapters");
        }

        getInstance().adapterLimit = adapterLimit;

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Adapter limit set to " + adapterLimit);
    }

    /** Get the number of adapters limit.
     *
     * @return The number of adapters limit
     */
    public static int getAdapterLimit() {
        return getInstance().adapterLimit;
    }

    /** Add an adapter to the list of adapters.
     *
     * @param adapter The adapter to add to the list of adapters
     *
     * @return The adapter's index
     *
     * @throws FTPHandlerException Thrown if the adapter is already in the list of adapters or the number of
     * adapters limit is reached
     */
    public static long addAdapter(FTPClientAdapter adapter) throws FTPHandlerException {
        if (getInstance().adapters.contains(adapter)) {
            throw new FTPHandlerException("The adapter " + adapter.getIndex() + " is already in the list of adapters");
        }

        if (getInstance().adapters.size() == getInstance().adapterLimit) {
            throw new FTPHandlerException("The adapter limit is reached, cannot add another one");
        }

        getInstance().adapters.add(adapter);

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Added adapter " + adapter.getIndex() + " to the list");
        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Number of adapters is now " + getInstance().adapters.size());

        return adapter.getIndex();
    }

    /** Remove an adapter from the list of adapters.
     *
     * @param adapter The adapter to remove from the list of adapters
     *
     * @throws FTPHandlerException Thrown if the adapter is not in the list of adapters of is considered
     * active
     */
    public static void removeAdapter(FTPClientAdapter adapter) throws FTPHandlerException {
        if (!getInstance().adapters.contains(adapter)) {
            throw new FTPHandlerException("The adapter " + adapter.getIndex() + " is not in the list of adapters");
        }

        if (adapter.isActive()) {
            throw new FTPHandlerException("The adapter " + adapter.getIndex() + " is considered active, cannot be removed");
        }

        getInstance().adapters.remove(adapter);

        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Removed adapter " + adapter.getIndex() + " from the list");
        Log.w("gpslogger.ftp", getInstance().getClass().getSimpleName() + " - setAdapterLimit: Number of adapters is now " + getInstance().adapters.size());
    }

    /** Command the adapter that matches the given index to connect to an FTP server.
     *
     * @param adapterIndex The adapter's index
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void connect(long adapterIndex) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.connect();
    }

    /** Command the adapter that matches the given index to disconnect from an FTP server.
     *
     * @param adapterIndex The adapter's index
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void disconnect(long adapterIndex) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.disconnect();
    }

    /** Command the adapter that matches the given index to force disconnect from an FTP server.
     *
     * Keep in mind that this method should mainly be called in a catch block, right after an exception
     * has been thrown, or in a finally block. In that sense, this method should ensure that the
     * connection between the server and the client is closed, meaning that an exception should be
     * thrown only in fatal cases (e.g., the server was shutdown unexpectedly).
     *
     * @param adapterIndex The adapter's index
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void forceDisconnect(long adapterIndex) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.forceDisconnect();
    }

    /** Command the adapter that matches the given index to login to an FTP server.
     *
     * @param adapterIndex The adapter's index
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void login(long adapterIndex) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.login();
    }

    /** Command the adapter that matches the given index to logout from an FTP server.
     *
     * @param adapterIndex The adapter's index
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void logout(long adapterIndex) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.logout();
    }

    /** Command the adapter that matches the given index to upload a file to an FTP server.
     *
     * @param adapterIndex The adapter's index
     * @param file The file to upload
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     * @throws IOException Thrown if an exception was thrown while handling the file
     */
    public static void upload(long adapterIndex, File file) throws FTPClientAdapterException, FTPHandlerException, IOException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.upload(file);
    }

    /** Command the adapter that matches the given index to download a file from an FTP server.
     *
     * @param adapterIndex The adapter's index
     * @param file The file's name/path to download
     *
     * @return The downloaded file
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     * @throws IOException Thrown if an exception was thrown while handling the file
     */
    public static File download(long adapterIndex, String file) throws FTPClientAdapterException, FTPHandlerException, IOException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        return adapter.download(file);
    }

    /** Command the adapter that matches the given index to change the working directory on an FTP server.
     *
     * @param adapterIndex The adapter's index
     * @param directory The directory's name/path to change to
     *
     * @throws FTPClientAdapterException Thrown if an exception was thrown in the adapter's underlying client
     */
    public static void changeDirectory(long adapterIndex, String directory) throws FTPClientAdapterException, FTPHandlerException {
        FTPClientAdapter adapter = getAdapter(adapterIndex);

        if (adapter == null) {
            throw new IllegalStateException("Adapter is not set");
        }

        adapter.changeDirectory(directory);
    }
}
