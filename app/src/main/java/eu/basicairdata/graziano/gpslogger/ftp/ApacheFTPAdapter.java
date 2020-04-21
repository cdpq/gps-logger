package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ApacheFTPAdapter extends FTPClientAdapter {

    protected FTPClient client = null;

    ApacheFTPAdapter() {
        super();

        client = new FTPClient();
    }

    ApacheFTPAdapter(String host, int port, String user, String password) {
        super(host, port, user, password);

        client = new FTPClient();
    }

    @Override
    public void connect() throws FTPClientAdapterException, IOException {
        if (client.isConnected()) {
            throw new IllegalStateException("Client is already connected to host");
        }

        client.setCharset(Charset.forName("UTF-8"));

        try {
            client.connect(getHost(), getPort());
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Couldn't connect to host");
            throw new FTPClientAdapterException("Couldn't connect to host", e);
        }

        int replyCode = client.getReplyCode();
        String replyString = client.getReplyString();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Couldn't connect to host");
            throw new FTPClientAdapterException("Negative reply from host : " + replyCode + " - " + replyString);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Connected to host");
    }

    @Override
    public void disconnect() throws FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected in order to disconnect");
        }

        try {
            client.disconnect();
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Couldn't disconnect from host");
            throw new FTPClientAdapterException("Couldn't disconnect from host", e);
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - disconnect: Disconnected from host");
    }

    @Override
    public void login() throws FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to login");
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
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to login");
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
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to upload a file");
        }

        if (file == null) {
            throw new IllegalArgumentException("file argument is null");
        }

        InputStream inputStream = new FileInputStream(file);

        try {
            client.storeFile(file.getName(), inputStream);
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: Couldn't upload file " + file.getName() + " to host");
            throw new FTPClientAdapterException("Couldn't upload file " + file.getName() + " to host", e);
        } finally {
            inputStream.close();
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - upload: File " + file.getName() + " uploaded to host");
    }

    @Override
    public File download(String file) throws FTPClientAdapterException, IOException {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
