package com.aaronhalbert.nosurfforreddit;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aaronhalbert.nosurfforreddit.adapters.PostsAdapter;
import com.aaronhalbert.nosurfforreddit.fragments.GifvFragment;
import com.aaronhalbert.nosurfforreddit.fragments.HomePostsFragment;
import com.aaronhalbert.nosurfforreddit.fragments.ImageFragment;
import com.aaronhalbert.nosurfforreddit.fragments.LinkPostFragment;
import com.aaronhalbert.nosurfforreddit.fragments.NoSurfWebViewFragment;
import com.aaronhalbert.nosurfforreddit.fragments.SelfPostFragment;
import com.aaronhalbert.nosurfforreddit.fragments.ViewPagerFragment;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LinkPostFragment.OnFragmentInteractionListener, PostsAdapter.RecyclerViewOnClickCallback, HomePostsFragment.HomePostsLoginCallback {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoSurfViewModel viewModel = ViewModelProviders.of(this).get(NoSurfViewModel.class);

        viewModel.initApp();

        getSupportFragmentManager().beginTransaction().add(R.id.main_activity_frame_layout, ViewPagerFragment.newInstance("abc", "def")).commit();

        Intent intent = getIntent();
        if ((intent.getAction()).equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            String error = uri.getQueryParameter("error");
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");

            Log.e(getClass().toString(), "error: " + error + "code: " + code + "state: " + state);

            viewModel.requestUserOAuthToken(code);

        } else {
            launchLoginScreen();
        }

        /* Disable StrictMode due to Untagged socket detected errors
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
        */

    }


    public void launchWebView(String url) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_frame_layout, NoSurfWebViewFragment.newInstance(url))
                .addToBackStack(null)
                .commit();
    }


    public void launchSelfPost(String title, String selfText) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_frame_layout, SelfPostFragment.newInstance(title, selfText))
                .addToBackStack(null)
                .commit();
    }

    public void launchLinkPost(String title, String imageUrl, String url, String gifUrl) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_frame_layout, LinkPostFragment.newInstance(title, imageUrl, url, gifUrl))
                .addToBackStack(null)
                .commit();
    }

    public void launchLoginScreen() {
        final String CLIENT_ID = "jPF59UF5MbMkWg";
        final String RESPONSE_TYPE = "code";
        final String STATE = generateRandomAlphaNumericString();
        final String REDIRECT_URI = "nosurfforreddit://oauth";
        final String DURATION = "permanent";
        final String SCOPE = "identity mysubreddits read";

        final String loginUrl = "https://www.reddit.com/api/v1/authorize.compact?client_id="
                + CLIENT_ID
                + "&response_type="
                + RESPONSE_TYPE
                + "&state="
                + STATE
                + "&redirect_uri="
                + REDIRECT_URI
                + "&duration="
                + DURATION
                + "&scope="
                + SCOPE;

        Log.e(getClass().toString(), STATE);

        launchWebView(loginUrl);


    }

    private String generateRandomAlphaNumericString() {
        return UUID.randomUUID().toString();
    }

}
