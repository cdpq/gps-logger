package eu.basicairdata.graziano.gpslogger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SayHelloReceiver extends BroadcastReceiver {
    private Context context = null;
    private File[] filesToUpload;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        File externalStorageDirectory = new File(Environment.getExternalStorageDirectory() + "/GPSLogger");
        this.filesToUpload = externalStorageDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file)
            {
                return file.getName().endsWith(".kml");
            }
        });

        new SendFileViaFTPTask().execute();

        Intent newIntent = new Intent();
        newIntent.setAction(Intent.ACTION_VIEW);
        newIntent.addCategory(Intent.CATEGORY_TEST);

        setResult(Activity.RESULT_OK, "Sending data via FTP...", null);
        this.context.sendOrderedBroadcast(newIntent, null);
    }

    class SendFileViaFTPTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String host = "swd.cdpq.ca";
            String username = "administrateur";
            String password = "g1v*4M7";

            FTPClient ftp = new FTPClient();
            try {
                Log.w("SendFileViaFTPTask", "doInBackground: Connecting to " + host);
                ftp.connect(host);
                Log.w("SendFileViaFTPTask", "doInBackground: Logging in to " + host);
                ftp.login(username, password);

                int progress = 0;
                for (File file: filesToUpload)
                {
                    Log.w("SendFileViaFTPTask", "doInBackground: Sending file " + file.getName());
                    ftp.upload(file);
                    progress++;
                    publishProgress(progress, filesToUpload.length);
                }

                Log.w("SendFileViaFTPTask", "doInBackground: Disconnecting from " + host);
                ftp.disconnect(true);

                return true;
            } catch (Exception e) {
                Log.w("SendFileViaFTPTask", "doInBackground: Exception: " + e.toString());
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Intent newIntent = new Intent();
            newIntent.setAction(Intent.ACTION_VIEW);
            newIntent.addCategory(Intent.CATEGORY_TEST);
            newIntent.putExtra(context.getPackageName()+".progress", (int)(values[0] / (float)values[1] * 100.0f));
            context.sendBroadcast(newIntent);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result)
            {
                Intent newIntent = new Intent();
                newIntent.setAction(Intent.ACTION_VIEW);
                newIntent.addCategory(Intent.CATEGORY_TEST);
                newIntent.putExtra(context.getPackageName()+".result", "FTP transfer failed.");
                context.sendBroadcast(newIntent);
            }
            else
            {
                Intent newIntent = new Intent();
                newIntent.setAction(Intent.ACTION_VIEW);
                newIntent.addCategory(Intent.CATEGORY_TEST);
                newIntent.putExtra(context.getPackageName()+".result", "FTP transfer success.");
                context.sendBroadcast(newIntent);
            }
        }
    }
}
