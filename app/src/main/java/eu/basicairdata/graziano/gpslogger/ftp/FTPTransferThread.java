package eu.basicairdata.graziano.gpslogger.ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTPSClient;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import eu.basicairdata.graziano.gpslogger.DatabaseHandler;
import eu.basicairdata.graziano.gpslogger.EventBusMSG;
import eu.basicairdata.graziano.gpslogger.GPSApplication;
import eu.basicairdata.graziano.gpslogger.Track;
import it.sauronsoftware.ftp4j.FTPClient;

public class FTPTransferThread extends Thread {
    private List<FTPTransferTask> tasks = null;
    private NetFTPHandler ftpHandler = null;
    private FTPSClient ftpClient = null;
    private GPSApplication app = null;

    public FTPTransferThread(List<FTPTransferTask> tasks) {
        this.tasks = tasks;
        this.ftpHandler = NetFTPHandler.getInstance();
        this.ftpClient = ftpHandler.getFtpsClient();
        this.app = GPSApplication.getInstance();
    }

    @Override
    public void run() {
        super.run();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        if (this.app == null) {
            return;
        }

        if (this.tasks == null) {
            return;
        }

        synchronized (ftpClient) {
            try {
                NetFTPHandler.ClientConnect(true);
                NetFTPHandler.ClientLogin();
//                if (!ftpClient.isConnected()) {
//                    ftpClient.connect(ftpHandler.getHost(), ftpHandler.getPort());
//                }
//
//                if (!ftpClient.isAuthenticated()) {
//                    ftpClient.login(ftpHandler.getUser(), ftpHandler.getPassword());
//                }

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
                        Log.w("myApp", "FTPTransferThread.java - run - Sending file " + task.getFile().getName() + " to " + ftpHandler.getHost());

                        if (!NetFTPHandler.ClientUpload(task.getFile())) {
                            Log.w("myApp", "FTPTransferThread.java - run - Failed to send file " + task.getFile().getName() + " to " + ftpHandler.getHost());
                            task.setStatus(FTPTransferTask.STATUS_FAILED);
                            task.setMessage("Couldn't upload file");
                            continue;
                        }
//                        ftpClient.upload(task.getFile());

                        Log.w("myApp", "FTPTransferThread.java - run - File " + task.getFile().getName() + " sent to " + ftpHandler.getHost());
                        task.setStatus(FTPTransferTask.STATUS_SUCCESS);
                        task.setMessage("File uploaded");

                        DatabaseHandler database = app.getGPSDataBase();
                        Track track = task.getTrack();
                        if (!track.getTransferred()) {
                            track.setTransferred(true);
                        }
                        database.updateTrackSync(track);
                        EventBus.getDefault().post(EventBusMSG.REFRESH_TRACKLIST);
                    } catch (Exception e) {
                        Log.w("myApp", "FTPTransferThread.java - run - Failed to send file " + task.getFile().getName() + " to " + ftpHandler.getHost());
                        e.printStackTrace();
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e.getClass().getName());
                    }
                }

//                if (ftpClient.isConnected()) {
//                    ftpClient.disconnect(true);
//                }
                NetFTPHandler.ClientDisconnect(true);
            } catch (Exception e) {
                Log.w("myApp", "FTPTransferThread.java - run - Failed to communicate with the client.");
                e.printStackTrace();

                for (FTPTransferTask task : tasks) {
                    if (task.getStatus() == FTPTransferTask.STATUS_PENDING) {
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e.getClass().getName());
                    }
                }

                try {
                    NetFTPHandler.ClientDisconnect(false);
//                    if (ftpClient.isConnected()) {
//                        ftpClient.disconnect(false);
//                    }
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
            ftpClient.notify();
        }
    }
}
