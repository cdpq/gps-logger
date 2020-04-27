package eu.basicairdata.graziano.gpslogger.ftp;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import eu.basicairdata.graziano.gpslogger.EventBusMSG;
import eu.basicairdata.graziano.gpslogger.GPSApplication;

public class Ftp4jFTPTestConnection extends AsyncTask<Void, Void, Boolean> {

    private GPSApplication app = null;
    private FTPClientAdapter ftpClientAdapter = null;

    private String directory = "/";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.app = GPSApplication.getInstance();

        String host = app.getPrefFTPHost();
        int port = app.getPrefFTPPort();
        String user = app.getPrefFTPUser();
        String password = app.getPrefFTPPassword();
        int security = app.getPrefFTPEncryption();

        directory = app.getPrefFTPPath();

        ftpClientAdapter = new Ftp4jFTPAdapter(host, port, user, password, security);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        synchronized (ftpClientAdapter) {
            try {
                Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - logout: Testing FTP connection...");
                ftpClientAdapter.connect();
                ftpClientAdapter.login();
                ftpClientAdapter.changeDirectory(directory);
                ftpClientAdapter.logout();
                ftpClientAdapter.disconnect();
            } catch (Exception e1) {
                Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - logout: FTP connection failed");
                e1.printStackTrace();

                try {
                    ftpClientAdapter.forceDisconnect();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                return false;
            }
        }

        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - logout: FTP connection succeeded");

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        Log.w("gpslogger.ftp", "onPostExecute: " + result);

        if (result) {
            EventBus.getDefault().post(EventBusMSG.TOAST_FTP_CONNECTION_TEST_SUCCEEDED);
        } else {
            EventBus.getDefault().post(EventBusMSG.TOAST_FTP_CONNECTION_TEST_FAILED);
        }
    }
}
