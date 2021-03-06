/**
 * EventBusMSG - Java Class for Android
 * Created by G.Capelli (BasicAirData) on 05/08/17.
 * Modified by Anthony Blanchette-Potvin (CDPQ) in 2020
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

package eu.basicairdata.graziano.gpslogger;

public class EventBusMSG {

    static final short APP_RESUME                       =   1;  // Sent to components on app resume
    static final short APP_PAUSE                        =   2;  // Sent to components on app pause
    static final short NEW_TRACK                        =   3;  // Request to create a new track
    static final short UPDATE_FIX                       =   4;  // Notify that a new fix is available
    static final short UPDATE_TRACK                     =   5;  // Notify that the current track stats are updated
    public static final short UPDATE_TRACKLIST          =   6;  // Ask the application to update the tracklist
    public static final short TRACKLIST_UPDATED         =   7;  // Notify that the tracklist is changed
    static final short UPDATE_SETTINGS                  =   8;  // Tell that settings are changed
    static final short REQUEST_ADD_PLACEMARK            =   9;  // The user ask to add a placemark
    static final short ADD_PLACEMARK                    =  10;  // The placemark is available
    static final short APPLY_SETTINGS                   =  11;  // The new settings must be applied
    static final short TOAST_TRACK_EXPORTED             =  12;  // The exporter has finished to export the track, shows toast
    static final short TOAST_STORAGE_PERMISSION_REQUIRED=  13;  // The Storage permission is required
    static final short UPDATE_JOB_PROGRESS              =  14;  // Update the progress of the current Job
    static final short NOTIFY_TRACKS_DELETED            =  15;  // Notify that some tracks are deleted
    static final short UPDATE_ACTIONBAR                 =  16;  // Notify that the actionbar must be updated
    public static final short REFRESH_TRACKLIST         =  17;  // Refresh the tracklist, without update it from DB
    static final short TOAST_TRACK_SEND_FTP_SUCCESS     =  18;  // The FTPTransferThread has successfully sent the tracks, show toast
    static final short TOAST_TRACK_SEND_FTP_FAILED      =  19;  // The FTPTransferThread has failed to send the tracks, show toast
    public static final short TOAST_FTP_CONNECTION_TEST_FAILED     =   20;  // The FTP connection test has failed, show toast
    public static final short TOAST_FTP_CONNECTION_TEST_SUCCEEDED  =   21;  // The FTP connection test has succeeded, show toast

    static final short TRACKLIST_DESELECT               =  24;  // The user deselect (into the tracklist) the track with a given id
    static final short TRACKLIST_SELECT                 =  25;  // The user select (into the tracklist) the track with a given id
    static final short INTENT_SEND                      =  26;  // Request to share
    static final short TOAST_UNABLE_TO_WRITE_THE_FILE   =  27;  // Exporter fails to export the Track (given id)

    static final short ACTION_BULK_DELETE_TRACKS        =  40;  // Delete the selected tracks
    static final short ACTION_BULK_EXPORT_TRACKS        =  41;  // Export the selected tracks
    static final short ACTION_BULK_VIEW_TRACKS          =  42;  // View the selected tracks
    static final short ACTION_BULK_SHARE_TRACKS         =  43;  // Share the selected tracks
    static final short ACTION_BULK_SEND_FTP_TRACKS      =  44;  // Send, via FTP, the selected tracks
    static final short ACTION_FTP_TEST_CONNECTION       =  45;  // Test the FTP connection
}
