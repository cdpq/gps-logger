package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.File;

import eu.basicairdata.graziano.gpslogger.Track;

public class FTPTransferTask {
    // Constants
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;

    // Variables
    private File file = null;
    private int status = 0;
    private String message = "";
    private Track track = null;

    // Constructors
    public FTPTransferTask() { }

    public FTPTransferTask(File file, Track track) {
        this.setFile(file);
        this.setTrack(track);
    }

    // Accessors/mutators
    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() { return this.file; }

    public void setTrack(Track track) {this.track = track; }

    public Track getTrack() { return this.track; }

    public void setStatus(int status) { this.status = status; }

    public int getStatus() { return this.status; }

    public void setMessage(String message) { this.message = message; }

    public String getMessage() { return this.message; }
}
