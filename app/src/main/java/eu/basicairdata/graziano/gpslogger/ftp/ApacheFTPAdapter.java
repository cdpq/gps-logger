package eu.basicairdata.graziano.gpslogger.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ApacheFTPAdapter extends FTPClientAdapter {

    FTPClient client;

    ApacheFTPAdapter() {
        super();
    }

    ApacheFTPAdapter(String host, int port, String user, String password) {
        super(host, port, user, password);

        FTPClient client = new FTPClient();
    }

    @Override
    public void connect() throws IllegalStateException, FTPClientAdapterException {
        if (client.isConnected()) {
            throw new IllegalStateException("Client is already connected to host");
        }

        client.setCharset(Charset.forName("UTF-8"));

        try {
            client.connect(getHost(), getPort());
        } catch (Exception e) {
            throw new FTPClientAdapterException("Couldn't connect to host", e);
        }
    }

    @Override
    public void disconnect() throws IllegalStateException, FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected in order to disconnect");
        }

        try {
            client.disconnect();
        } catch (Exception e) {
            throw new FTPClientAdapterException("Couldn't disconnect from host", e);
        }
    }

    @Override
    public void login() throws IllegalStateException, FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to login");
        }

        try {
            client.login(getUser(), getPassword());
        } catch (Exception e) {
           throw new FTPClientAdapterException("Couldn't login to host", e);
        }
    }

    @Override
    public void logout() throws IllegalStateException, FTPClientAdapterException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to login");
        }

        try {
            client.logout();
        } catch (Exception e) {
            throw new FTPClientAdapterException("Couldn't logout from host", e);
        }
    }

    @Override
    public void upload(File file) throws IllegalStateException, FTPClientAdapterException, IOException {
        if (!client.isConnected()) {
            throw new IllegalStateException("Client must be connected to host in order to upload a file");
        }

        InputStream inputStream = new FileInputStream(file);

        try {
            client.storeFile(file.getName(), inputStream);
        } catch (Exception e) {
            throw new FTPClientAdapterException("Couldn't upload file " + file.getName() + " to host", e);
        } finally {
            inputStream.close();
        }
    }

    @Override
    public File download(String file) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
