package com.aaronhalbert.nosurfforreddit;

import android.app.Application;

import com.aaronhalbert.nosurfforreddit.dependencyinjection.application.ApplicationComponent;
import com.aaronhalbert.nosurfforreddit.dependencyinjection.application.ApplicationModule;
import com.aaronhalbert.nosurfforreddit.dependencyinjection.application.DaggerApplicationComponent;
import com.squareup.leakcanary.LeakCanary;

public class NoSurfApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //initLeakCanary();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
