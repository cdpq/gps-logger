/**
 * SettingsActivity - Java Class for Android
 * Created by G.Capelli (BasicAirData) on 23/7/2016
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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("prefColorTheme", "2")));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.id_toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.menu_settings);

        FragmentSettings wvf = new FragmentSettings();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.id_preferences, wvf);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EventBus.getDefault().isRegistered(this)) {
            //Log.w("myApp", "[#] GPSActivity.java - EventBus: GPSActivity already registered");
            EventBus.getDefault().unregister(this);
        }

        EventBus.getDefault().register(this);

        Log.w("myApp", "[#] SettingsActivity.java - onResume()");
    }

    @Override
    public void onPause() {
        Log.w("myApp", "[#] SettingsActivity.java - onPause()");

        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEvent(Short msg) {
        switch (msg) {
            case EventBusMSG.TOAST_FTP_CONNECTION_TEST_FAILED:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getString(R.string.toast_ftp_connection_test_failed), Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case EventBusMSG.TOAST_FTP_CONNECTION_TEST_SUCCEEDED:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getString(R.string.toast_ftp_connection_test_succeeded), Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
