package eu.basicairdata.graziano.gpslogger.ftp;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import eu.basicairdata.graziano.gpslogger.GPSApplication;
import eu.basicairdata.graziano.gpslogger.R;

public class NetFTPHandler {
    private static final NetFTPHandler instance = new NetFTPHandler();
    private FTPClient ftpClient = null;
    private FTPSClient ftpsClient = null;

    private String host = "";
    private int port = 22;
    private String user = "";
    private String password = "";
    private String path = "";
    private int security = 0;

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private NetFTPHandler() {
        GPSApplication app = GPSApplication.getInstance();

        this.host = app.getPrefFTPHost();
        this.port = app.getPrefFTPPort();
        this.user = app.getPrefFTPUser();
        this.password = app.getPrefFTPPassword();
        this.path = app.getPrefFTPPath();
        this.security = app.getPrefFTPEncryption();

        setupOnSharedPreferenceChangeListener();

        ftpsClient = new FTPSClient("SSL", false);
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

    public static NetFTPHandler getInstance() { return instance; }

    public FTPSClient getFtpsClient() { return this.ftpsClient; }

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

    public static void ClientConnect(boolean forceReconnect) throws IOException {
        if (instance.ftpsClient.isConnected()) {
            if (forceReconnect) {
                ClientDisconnect(true);
            } else {
                return;
            }
        }

        instance.ftpsClient.connect(instance.getHost(), instance.getPort());
    }

    public static void ClientLogin() throws IOException {
        if (!instance.ftpsClient.isConnected()) {
            throw new IllegalStateException("Client must be connected in order to login.");
        }

        instance.ftpsClient.login(instance.getUser(), instance.getPassword());
    }

    public static boolean ClientUpload(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            return instance.ftpsClient.storeFile(file.getName(), inputStream);
        } catch (IOException e) {
            throw e;
        } finally {
            inputStream.close();
        }
    }

    public static void ClientLogout() throws IOException {
        instance.ftpsClient.logout();
    }

    public static void ClientDisconnect(boolean sendQuitCommand) throws IOException {
        if (!instance.ftpsClient.isConnected()) {
            throw new IllegalStateException("Client must be connection in order to disconnect.");
        }

        instance.ftpsClient.disconnect();
    }
}
