# GPS Logger<br>[![Releases](https://img.shields.io/github/v/release/anthonyblanchettepotvin/gps-logger)](https://github.com/anthonyblanchettepotvin/gps-logger/releases) [![GitHub license](https://img.shields.io/github/license/anthonyblanchettepotvin/gps-logger)](https://raw.githubusercontent.com/anthonyblanchettepotvin/gps-logger/master/LICENSE)
A GPS logger for Android mobile devices.<br>
Offered by [BasicAirData](http://www.basicairdata.eu) - Open and free DIY air data instrumentation and telemetry
Modified by [CDPQ](http://cdpq.ca) - Centre de développement du porc du Québec

![alt tag](https://github.com/anthonyblanchettepotvin/gps-logger/blob/master/screenshots/Image_01.png)

## Disclaimer

This is a custom version of the official application made by [BasicAirData](https://www.basicairdata.eu/).
This version is not downloadable on Google Play and will receive specific features based on CDPQ's needs.
That said, most of the screenshots doesn't reflect the current and future state of the application.
For more information on the official application, see the [official BasicAirData GPS Logger repository](https://github.com/BasicAirData/GPSLogger).

## Description

GPS Logger is a simple App to record your position and your path.<br>
It's a basic and lightweight GPS tracker focused on accuracy, with an eye to power saving.<br>
This app is very accurate in determining your altitude: enable EGM96 automatic altitude correction on settings!<br>
You can record all your trips, view them in your preferred Viewer (it must be installed) directly from the in-app tracklist, and share them in KML, GPX, and TXT format in many ways.<br>
Finally, the application supports transfer via FTP. Simply setup the connection in the settings and you are good to go!

## Reference documents

[Code of conduct](CODE_OF_CONDUCT.md)

[Contributing Information](CONTRIBUTING.md)

[Repository License](LICENSE)

## Frequently Asked Questions
<b>Q</b> - <i>I've just installed the App, but it doesn't read the GPS Signal.</i><br>
<b>A</b> - Please try to reboot your Device. Check also that the GPS Signal is strong enough and, if not, go in an open Area.

<b>Q</b> - <i>The Location is active, but the App sees "GPS disabled".</i><br>
<b>A</b> - Please go on Location Section of your Android Settings: the Phone could be set to use the "Battery saving" Locating Method. This Method uses Wi-Fi & Mobile Networks to estimate your Location, without turn on the GPS. In case please switch to "Phone only" or "High accuracy" Method to enable the GPS Sensor.

<b>Q</b> - <i>How can I view my recorded Tracks?</i><br>
<b>A</b> - You can view your Tracks by going on Tracklist Tab and clicking on it. An Actionbar will appear, that should contain an Eye Icon. A KML/GPX viewer must be installed on your device; if not (in this case the Eye Icon will not be visible), please install it. In GPS Logger's Settings you can choose to use a GPX or a KML Viewer. A good Viewer for Android is GPX Viewer, but there are lots of good Alternatives around.

<b>Q</b> - <i>The "View" Icon is not visible on Actionbar.</i><br>
<b>A</b> - The "View" Icon is visible, by selecting one single Track of the Tracklist, if you have at least one external Viewer installed on your Device. In GPS Logger's Settings you can choose to use a GPX or a KML Viewer. A good Viewer for Android is GPX Viewer, but there are lots of good Alternatives around.

<b>Q</b> - <i>The "Share" Icon is not visible on Actionbar.</i><br>
<b>A</b> - The "Share" Icon is visible, by selecting some Tracks of the Tracklist, if you have at least one Application installed on your Device with which to Share the Files. The Formats you will share are set on Exporting Section of GPS Logger's Settings, please check that at least one Exportation Format is selected.

<b>Q</b> - <i>My track is not shown (or partially shown) in Google Earth.</i><br>
<b>A</b> - 1) Please check in Android System Settings that Earth has the Storage Permission granted; 2) GPS Logger might be set to show the Track in 3D, and the Track may be hidden under the Terrain. Please go in the Exportation Settings, switch the Altitude Mode to "Projected to ground" and try again.

<b>Q</b> - <i>The App sometimes stops recording when running in Background.</i><br>
<b>A</b> - The App could be closed by the System during the Background Recording. To avoid it, you have to go on the Android Settings and turn off all Battery Monitoring and Optimizations for GPS Logger. On Android 9 check also that the Application is NOT Background Restricted. Sadly any Device Brand implemented in a different Way the Settings to keep safe the Background Apps, so a small Research must be done. Furthermore, Anti-Viruses (like Avast) are very aggressive with long running Apps, and must be correctly set.
