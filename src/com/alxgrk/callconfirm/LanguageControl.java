/*
 * Created on 10.03.2016
 *
 * author Alex
 */
package com.alxgrk.callconfirm;

import com.alxgrk.callconfirm.Preferences.Language;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Offers methods corresponding to the language selection in strings.xml.
 * 
 * @author alxgrk
 *
 */
public class LanguageControl {

    private static LanguageControl INSTANCE;

    private Context context;

    private volatile Language lang;

    private LanguageControl(Context context, Language lang) {
        this.context = context;
        this.lang = lang;
    }

    public static final LanguageControl getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            Preferences prefs = new Preferences(context);
            INSTANCE = new LanguageControl(context, prefs.getLanguage());
        }
        return INSTANCE;
    }

    public static final LanguageControl getInstance(@NonNull Context context,
            @NonNull Language lang) {
        if (INSTANCE == null) {
            INSTANCE = new LanguageControl(context, lang);
        }
        return INSTANCE;
    }

    public final void updateLanguage(Language lang) {
        this.lang = lang;
    }

    private String convertId(int resId) {
        return context.getString(resId);
    }

    public final String on() {
        String on = "";
        switch (lang) {
            case GERMAN:
                on = convertId(R.string.on_DE);
                break;
            case ENGLISH:
                on = convertId(R.string.on_EN);
                break;
        }
        return on;
    }

    public final String off() {
        String off = "";
        switch (lang) {
            case GERMAN:
                off = convertId(R.string.off_DE);
                break;
            case ENGLISH:
                off = convertId(R.string.off_EN);
                break;
        }
        return off;
    }

    public final String toggleLabel() {
        String label = "";
        switch (lang) {
            case GERMAN:
                label = convertId(R.string.toggle_label_DE);
                break;
            case ENGLISH:
                label = convertId(R.string.toggle_label_EN);
                break;
        }
        return label;
    }

    public final String confirmation() {
        String conf = "";
        switch (lang) {
            case GERMAN:
                conf = convertId(R.string.confirmation_DE);
                break;
            case ENGLISH:
                conf = convertId(R.string.confirmation_EN);
                break;
        }
        return conf;
    }

    public final String yes() {
        String yes = "";
        switch (lang) {
            case GERMAN:
                yes = convertId(R.string.yes_DE);
                break;
            case ENGLISH:
                yes = convertId(R.string.yes_EN);
                break;
        }
        return yes;
    }

    public final String no() {
        String no = "";
        switch (lang) {
            case GERMAN:
                no = convertId(R.string.no_DE);
                break;
            case ENGLISH:
                no = convertId(R.string.no_EN);
                break;
        }
        return no;
    }

    public final String error() {
        String error = "";
        switch (lang) {
            case GERMAN:
                error = convertId(R.string.error_DE);
                break;
            case ENGLISH:
                error = convertId(R.string.error_EN);
                break;
        }
        return error;
    }
}
