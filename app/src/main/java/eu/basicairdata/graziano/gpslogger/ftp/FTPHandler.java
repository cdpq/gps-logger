package eu.basicairdata.graziano.gpslogger.ftp;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import eu.basicairdata.graziano.gpslogger.GPSApplication;
import eu.basicairdata.graziano.gpslogger.R;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class FTPHandler {
    private static final FTPHandler instance = new FTPHandler();
    private final FTPClient client = new FTPClient();

    private String host = "";
    private int port = 22;
    private String user = "";
    private String password = "";
    private String path = "";
    private int security = 0;

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private FTPHandler() {
        GPSApplication app = GPSApplication.getInstance();

        this.host = app.getPrefFTPHost();
        this.port = app.getPrefFTPPort();
        this.user = app.getPrefFTPUser();
        this.password = app.getPrefFTPPassword();
        this.path = app.getPrefFTPPath();
        this.security = app.getPrefFTPEncryption();

        setupOnSharedPreferenceChangeListener();
    }

    protected void setupOnSharedPreferenceChangeListener() {
        GPSApplication app = GPSApplication.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
        onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                GPSApplication app = GPSApplication.getInstance();
                Resources resources = app.getResources();

                if (key.equals(resources.getString(R.string.key_prefs_ftp_host))) {
                    setHost(sharedPreferences.getString(key, getHost()));
                } else if (key.equals(resources.getString(R.string.key_prefs_ftp_user))) {
                    setUser(sharedPreferences.getString(key, getUser()));
                } else if (key.equals(resources.getString(R.string.key_prefs_ftp_password))) {
                    setPassword(sharedPreferences.getString(key, getPassword()));
                } else if (key.equals(resources.getString(R.string.key_prefs_ftp_path))) {
                    setPath(sharedPreferences.getString(key, getPath()));
                } else if (key.equals(resources.getString(R.string.key_prefs_ftp_encryption))) {
                    setSecurity(Integer.parseInt(sharedPreferences.getString(key, String.valueOf(getSecurity()))));
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static FTPHandler getInstance() { return instance; }

    public FTPClient getClient() { return this.client; }

    public String getHost() { return this.host; }

    protected void setHost(String host) { this.host = host; }

    public int getPort() { return this.port; }

    protected void setPort(int port) { this.port = port; }

    public String getUser() { return this.user; }

    protected void setUser(String user) { this.user = user; }

    public String getPassword() { return this.password; }

    protected void setPassword(String password) { this.password = password; }

    public String getPath() { return this.path; }

    protected void setPath(String path) { this.path = path; }

    public int getSecurity() { return this.security; }

    protected void setSecurity(int security) { this.security = security; }

    public static void ClientConnect(boolean forceReconnect) throws FTPException, IOException, FTPIllegalReplyException {
        if (instance.client.isConnected()) {
            if (forceReconnect) {
                ClientDisconnect(true);
            } else {
                return;
            }
        }

        if (instance.client.getSecurity() != instance.getSecurity()) {
            instance.client.setSecurity(instance.getSecurity());
        }

        instance.client.setCharset("UTF-8");
        instance.client.connect(instance.getHost(), instance.getPort());
    }

    public static void ClientLogin() throws FTPException, IOException, FTPIllegalReplyException, IllegalStateException {
        if (!instance.client.isConnected()) {
            throw new IllegalStateException("Client must be connected in order to login.");
        }

        if (instance.client.isAuthenticated()) {
            throw new IllegalStateException("Client is already logged in.");
        }

        instance.client.login(instance.getUser(), instance.getPassword());
    }

    public static void ClientUpload(File file) throws FTPIllegalReplyException, FTPDataTransferException, FTPException, FTPAbortedException, IOException, IllegalAccessException {
        if (!instance.client.isAuthenticated()) {
            throw new IllegalAccessException("Client must be authenticated in order to upload.");
        }

        instance.client.upload(file);
    }

    public static void ClientLogout() throws FTPException, IOException, FTPIllegalReplyException, IllegalStateException {
        if (!instance.client.isAuthenticated()) {
            throw new IllegalStateException("Client must be authenticated in order to logout.");
        }

        instance.client.logout();
    }

    public static void ClientDisconnect(boolean sendQuitCommand) throws FTPException, IOException, FTPIllegalReplyException, IllegalStateException {
        if (!instance.client.isConnected()) {
            throw new IllegalStateException("Client must be connection in order to disconnect.");
        }

        instance.client.disconnect(sendQuitCommand);
    }
}
