package eu.basicairdata.graziano.gpslogger.ftp;

import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.IOException;

public class ApacheFTPSAdapter extends ApacheFTPAdapter {

    private String protocol = "TLS";
    private boolean isImplicit = false;

    ApacheFTPSAdapter () {
        super();

        client = new SSLSessionReuseFTPSClient();
    }

    ApacheFTPSAdapter (String host, int port, String user, String password, String protocol, boolean isImplicit) {
        super(host, port, user, password);

        this.protocol = protocol;
        this.isImplicit = isImplicit;

        client = new SSLSessionReuseFTPSClient(protocol, isImplicit);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isImplicit() {
        return isImplicit;
    }

    @Override
    public void connect() throws FTPClientAdapterException, IOException {
        super.connect();

        SSLSessionReuseFTPSClient ftpsClient = (SSLSessionReuseFTPSClient)client;
        ftpsClient.execPBSZ(0);
        ftpsClient.execPROT("P");
        ftpsClient.enterLocalPassiveMode();
    }

    @Override
    public void upload(File file) throws FTPClientAdapterException, IOException {
        client.changeWorkingDirectory("/upload");

        super.upload(file);
    }
}
