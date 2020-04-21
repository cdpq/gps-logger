package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import eu.basicairdata.graziano.gpslogger.DatabaseHandler;
import eu.basicairdata.graziano.gpslogger.EventBusMSG;
import eu.basicairdata.graziano.gpslogger.GPSApplication;
import eu.basicairdata.graziano.gpslogger.Track;

public class FTPTransferThread extends Thread {
    private List<FTPTransferTask> tasks = null;
    private FTPHandler ftpHandler = null;
    private FTPClientAdapter ftpClientAdapter = null;
    private GPSApplication app = null;

    public FTPTransferThread(List<FTPTransferTask> tasks) {
//        System.setProperty("jdk.tls.useExtendedMasterSecret", "false");

        this.tasks = tasks;
        this.ftpHandler = FTPHandler.getInstance();

        this.app = GPSApplication.getInstance();

        String host = app.getPrefFTPHost();
        int port = app.getPrefFTPPort();
        String user = app.getPrefFTPUser();
        String password = app.getPrefFTPPassword();

        switch (app.getPrefFTPEncryption()) {
            case 1: // FTPS
                ftpClientAdapter = new ApacheFTPSAdapter(host, port, user, password, "TLS", true);
                break;
            case 2: // FTPES
                ftpClientAdapter = new ApacheFTPSAdapter(host, port, user, password, "TLS", false);
                break;
            default: // FTP
                ftpClientAdapter = new ApacheFTPAdapter(host, port, user, password);
                break;
        }

        FTPHandler.setAdapter(ftpClientAdapter);
    }

    @Override
    public void run() {
        super.run();

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        if (this.app == null) {
            return;
        }

        if (this.tasks == null) {
            return;
        }

        synchronized (ftpClientAdapter) {
            try {
                FTPHandler.connect();
                FTPHandler.login();

                for (FTPTransferTask task : tasks) {
                    task.setStatus(FTPTransferTask.STATUS_STARTED);

                    if (task.getFile() == null) {
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("File is null");
                        continue;
                    }

                    if (!task.getFile().exists()) {
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("File doesn't exist");
                        continue;
                    }

                    try {
//                        if (ftpClient.currentDirectory() != ftpHandler.getPath()) {
//                            ftpClient.changeDirectory(ftpHandler.getPath());
//                        }

                        // Send file to server
                        Log.w("myApp", "FTPTransferThread.java - run - Sending file " + task.getFile().getName() + " to " + FTPHandler.getAdapter().getHost());
                        FTPHandler.upload(task.getFile());

                        Log.w("myApp", "FTPTransferThread.java - run - File " + task.getFile().getName() + " sent to " + FTPHandler.getAdapter().getHost());
                        task.setStatus(FTPTransferTask.STATUS_SUCCESS);
                        task.setMessage("File uploaded");

                        DatabaseHandler database = app.getGPSDataBase();
                        Track track = task.getTrack();
                        if (!track.getTransferred()) {
                            track.setTransferred(true);
                        }
                        database.updateTrackSync(track);

                        EventBus.getDefault().post(EventBusMSG.UPDATE_TRACKLIST);
                    } catch (Exception e) {
                        Log.w("myApp", "FTPTransferThread.java - run - Failed to send file " + task.getFile().getName() + " to " + FTPHandler.getAdapter().getHost());
                        e.printStackTrace();
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e.getClass().getName());
                    }
                }

                FTPHandler.disconnect();
            } catch (Exception e1) {
                Log.w("myApp", "FTPTransferThread.java - run - Failed to communicate with the client.");
                e1.printStackTrace();

                for (FTPTransferTask task : tasks) {
                    if (task.getStatus() == FTPTransferTask.STATUS_PENDING) {
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e1.getClass().getName());
                    }
                }

                try {
                    FTPHandler.disconnect();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            ftpClientAdapter.notify();
        }
    }
}
