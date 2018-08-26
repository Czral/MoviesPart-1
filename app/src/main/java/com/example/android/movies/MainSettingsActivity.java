package com.example.android.movies;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainSettingsActivity extends AppCompatActivity {

    static String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference genre = findPreference(getString(R.string.genre));
            bindPreferenceToObject(genre);

            Preference keyword = findPreference(getString(R.string.keyword));
            bindPreferenceToObject(keyword);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            search = newValue.toString();

            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(search);
                if (prefIndex > 0) {

                    CharSequence[] genre = listPreference.getEntries();
                    preference.setSummary(genre[prefIndex]);
                } else {

                    preference.setSummary(search);
                }

            } else {

                preference.setSummary(search);
            }

            return true;
        }

        private void bindPreferenceToObject(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String string = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, string);
        }

    }

}
