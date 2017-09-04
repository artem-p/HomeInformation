package ru.artempugachev.homeinformation;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Just initialize Stetho
 */

public class HomeInformationApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
