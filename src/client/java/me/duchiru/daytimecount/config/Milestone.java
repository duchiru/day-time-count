package me.duchiru.daytimecount.config;

import com.google.gson.annotations.SerializedName;

public class Milestone {
    @SerializedName("title")
    public String title;
    @SerializedName("subtitle")
    public String subtitle;
    @SerializedName("title_color")
    public String titleColor;
    @SerializedName("subtitle_color")
    public String subtitleColor;
    @SerializedName("sound")
    public String sound;

    public Milestone(String title, String subtitle, String titleColor, String subtitleColor, String sound) {
        this.title = title;
        this.subtitle = subtitle;
        this.titleColor = titleColor;
        this.subtitleColor = subtitleColor;
        this.sound = sound;
    }
}
