package com.aaronhalbert.nosurfforreddit.reddit;

import com.google.gson.annotations.SerializedName;

public class RedditT3DataObject {
    private String subreddit;
    private String title;
    private String thumbnail;
    private int score;
    @SerializedName("is_self") private boolean isSelf;

    public String getSubreddit() {
        return subreddit;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() { return thumbnail; }

    public int getScore() { return score; }

    public boolean getIsSelf() { return isSelf; }
}
