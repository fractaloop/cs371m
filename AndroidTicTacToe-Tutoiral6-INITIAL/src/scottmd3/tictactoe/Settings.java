package scottmd3.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName("ttt_prefs");
		addPreferencesFromResource(R.xml.preferences);


		final SharedPreferences prefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
		final ListPreference difficultyLevelPref = (ListPreference) findPreference("difficulty_level");
		String difficulty = prefs.getString("difficulty_level",
				getResources().getString(R.string.difficulty_expert));
		difficultyLevelPref.setSummary((CharSequence) difficulty);
		difficultyLevelPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				difficultyLevelPref.setSummary((CharSequence) newValue);
				// Since we are handling the pref, we must save it
				SharedPreferences.Editor ed = prefs.edit();
				ed.putString("difficulty_level", newValue.toString());
				ed.commit();
				return true;
			}
		});
		
		
		final SharedPreferences p = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
		final EditTextPreference victoryPref = (EditTextPreference) findPreference("victory_message");
		//Log.d(tag, msg);
		String victory = p.getString("victory_message",
				getResources().getString(R.string.result_human_wins));
		victoryPref.setSummary((CharSequence) victory);
		victoryPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				victoryPref.setSummary((CharSequence) newValue);
				SharedPreferences.Editor ed = p.edit();
				ed.putString("victory_message", newValue.toString());
				ed.commit();
				return true;
			}
		});
	}
}
