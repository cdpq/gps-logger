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
    private GPSApplication app = null;
    private FTPClientAdapter ftpClientAdapter = null;
    private long ftpClientAdapterIndex = -1;

    private String directory = "/";

    public FTPTransferThread(List<FTPTransferTask> tasks) {
        this.tasks = tasks;

        this.app = GPSApplication.getInstance();

        String host = app.getPrefFTPHost();
        int port = app.getPrefFTPPort();
        String user = app.getPrefFTPUser();
        String password = app.getPrefFTPPassword();
        int security = app.getPrefFTPEncryption();

        directory = app.getPrefFTPPath();

        ftpClientAdapter = new Ftp4jFTPAdapter(host, port, user, password, security);

        try {
            ftpClientAdapterIndex = FTPHandler.addAdapter(ftpClientAdapter);
        } catch (FTPHandlerException e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - FTPTransferThread: Failed to initialize the client");
            e.printStackTrace();

            for (FTPTransferTask task : tasks) {
                if (task.getStatus() == FTPTransferTask.STATUS_PENDING) {
                    task.setStatus(FTPTransferTask.STATUS_FAILED);
                    task.setMessage("Operation ended with exception " + e.getClass().getName());
                }
            }
        }
    }

    @Override
    public void run() {
        super.run();

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        if (this.ftpClientAdapterIndex == -1) {
            return;
        }

        if (this.app == null) {
            return;
        }

        if (this.tasks == null) {
            return;
        }

        synchronized (ftpClientAdapter) {
            try {
                FTPHandler.connect(ftpClientAdapterIndex);
                FTPHandler.login(ftpClientAdapterIndex);
                FTPHandler.changeDirectory(ftpClientAdapterIndex, directory);

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
                        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - run: Sending file " + task.getFile().getName() + " to " + FTPHandler.getAdapter(ftpClientAdapterIndex).getHost());
                        FTPHandler.upload(ftpClientAdapterIndex, task.getFile());

                        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - run: File " + task.getFile().getName() + " sent to " + FTPHandler.getAdapter(ftpClientAdapterIndex).getHost());
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
                        Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - run: Failed to send file " + task.getFile().getName() + " to " + FTPHandler.getAdapter(ftpClientAdapterIndex).getHost());
                        e.printStackTrace();

                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e.getClass().getName());
                    }
                }

                FTPHandler.disconnect(ftpClientAdapterIndex);
            } catch (Exception e1) {
                Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - run: Failed to communicate with the client");
                e1.printStackTrace();

                for (FTPTransferTask task : tasks) {
                    if (task.getStatus() == FTPTransferTask.STATUS_PENDING) {
                        task.setStatus(FTPTransferTask.STATUS_FAILED);
                        task.setMessage("Operation ended with exception " + e1.getClass().getName());
                    }
                }

                try {
                    FTPHandler.forceDisconnect(ftpClientAdapterIndex);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            ftpClientAdapter.notify();
        }

        try {
            FTPHandler.removeAdapter(ftpClientAdapter);
        } catch (FTPHandlerException e) {
            Log.w("gpslogger.ftp", this.getClass().getSimpleName() + " - run: Failed to terminate the client");
            e.printStackTrace();
        }
    }
}
