/*
 * FTPTransferTask - Java Class for Android
 * Copyright (C) 2020 by Anthony Blanchette-Potvin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.File;

import eu.basicairdata.graziano.gpslogger.Track;

/** Base class that encapsulates all the information needed by the FTPTransferThread to upload
 * a file to an FTP server.
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPTransferThread
 */
public class FTPTransferTask {

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;

    private File file = null;
    private int status = STATUS_PENDING;
    private String message = "";
    private Track track = null;

    /** Constructor allowing file and track arguments
     *
     * @param file The file to transfer
     * @param track The track related to the file to transfer
     */
    public FTPTransferTask(File file, Track track) {
        this.setFile(file);
        this.setTrack(track);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
