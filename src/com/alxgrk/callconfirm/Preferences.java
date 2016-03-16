/*
 * Created on 10.03.2016
 *
 * author Alex
 */
package com.alxgrk.callconfirm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

public final class Preferences {

    private static final String TAG = Preferences.class.getSimpleName();

    private Context context = null;

    private SharedPreferences sharedPref = null;

    private SharedPreferences.Editor sharedEditor = null;

    private LanguageControl langControl;

    public Preferences(@NonNull Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences("main", Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

        langControl = LanguageControl.getInstance(context, getLanguage());
    }

    // ------------ LANGUAGE --------------

    public Preferences setLanguage(@NonNull Language lang) {
        langControl.updateLanguage(lang);

        sharedEditor.putString(Language.LANG_TAG, lang.name()).commit();
        Log.d(TAG, "Language is " + lang);

        return this;
    }

    public final Language getLanguage() {
        String lang = sharedPref.getString(Language.LANG_TAG, Language.ENGLISH.name());
        Log.d(TAG, "Language is " + lang);

        return Language.valueOf(lang);
    }

    public enum Language {
        GERMAN("deutsch"), ENGLISH("english");

        private static final String LANG_TAG = Language.class.getSimpleName();

        private String language;

        private Language(@NonNull String language) {
            this.language = language;
        }

        @Override
        public String toString() {
            return language;
        }
    }

    // ------------ ACTIVATION STATE --------------

    public Preferences setActivationState(@NonNull ActivationState state) {
        sharedEditor.putString(ActivationState.STATE_TAG, state.name()).commit();
        Log.d(TAG, "ActivationState is " + state);

        return this;
    }

    public ActivationState getActivationState() {
        String state = sharedPref.getString(ActivationState.STATE_TAG, ActivationState.ON.name());
        Log.d(TAG, "ActivationState is " + state);

        return ActivationState.valueOf(state);
    }

    public enum ActivationState {
        ON, OFF;

        private static final String STATE_TAG = ActivationState.class.getSimpleName();
    }
}
