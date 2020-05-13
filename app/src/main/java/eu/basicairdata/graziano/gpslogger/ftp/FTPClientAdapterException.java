/*
 * FTPClientAdapterException - Java Class for Android
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

/** Exception thrown by any FTPClientAdapter subclasses.
 *
 * @see eu.basicairdata.graziano.gpslogger.ftp.FTPClientAdapter
 */
public class FTPClientAdapterException extends Exception {

    /** Constructor allowing message argument.
     *
     * @param message The message of the exception
     */
    FTPClientAdapterException(String message) {
        super(message);
    }

    /** Constructor allowing message and cause arguments.
     *
     * @param message The message of the exception
     * @param cause The cause of the exception
     */
    FTPClientAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
