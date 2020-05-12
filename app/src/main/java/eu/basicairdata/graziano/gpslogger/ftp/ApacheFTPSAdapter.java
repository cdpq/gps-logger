package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/** Subclass of ApacheFTPAdapter that implements FTPSClient from the Apache Commons Net library.
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.ApacheFTPAdapter
 * @see "http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPSClient.html"
 */
public class ApacheFTPSAdapter extends ApacheFTPAdapter {

    protected String protocol = "TLS";
    protected boolean isImplicit = false;

    /** Constructor allowing host, port, user and password arguments.
     *
     * @param host The host name of the FTP server (111.222.333.444, example.com...)
     * @param port The port of the FTP server
     * @param user The user used to logon to the FTP server
     * @param password The user's password used to logon to the FTP server
     * @param protocol The encryption protocol ("TLS", "SSL")
     * @param isImplicit Whether the security is implicit or explicit
     */
    ApacheFTPSAdapter (String host, int port, String user, String password, String protocol, boolean isImplicit) {
        super(host, port, user, password);

        if (protocol != "TLS" && protocol != "SSL") {
            throw new IllegalArgumentException("protocol argument must be \"TLS\" or \"SSL\"");
        }

        this.protocol = protocol;
        this.isImplicit = isImplicit;

        client = new SSLSessionReuseFTPSClient(protocol, isImplicit);
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isImplicit() {
        return isImplicit;
    }

    @Override
    public void connect() throws FTPClientAdapterException {
        super.connect();

        try {
            SSLSessionReuseFTPSClient ftpsClient = (SSLSessionReuseFTPSClient)client;
            ftpsClient.execPBSZ(0);
            ftpsClient.execPROT("P");
            ftpsClient.enterLocalPassiveMode();
        } catch (Exception e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - connect: Couldn't connect to host");
            throw new FTPClientAdapterException("Couldn't connect to host", e);
        }
    }

    @Override
    public void upload(File file) throws FTPClientAdapterException, IOException {
        client.changeWorkingDirectory("/upload");

        super.upload(file);
    }
}
