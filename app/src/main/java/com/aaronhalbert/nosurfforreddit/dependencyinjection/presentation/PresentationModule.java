package com.aaronhalbert.nosurfforreddit.dependencyinjection.presentation;

import android.os.Build;

import com.aaronhalbert.nosurfforreddit.webview.NoSurfWebViewClient;
import com.aaronhalbert.nosurfforreddit.webview.NoSurfWebViewClientApiLevelBelow24;

import androidx.fragment.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {
    private final FragmentActivity fragmentActivity;

    public PresentationModule(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Provides
    NoSurfWebViewClient provideNoSurfWebViewClient() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new NoSurfWebViewClient(fragmentActivity);
        } else {
            return new NoSurfWebViewClientApiLevelBelow24(fragmentActivity);
        }
    }
}
