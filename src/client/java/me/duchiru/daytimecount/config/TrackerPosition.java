package me.duchiru.daytimecount.config;

import com.google.gson.annotations.SerializedName;

public enum TrackerPosition {
    @SerializedName("top_left")
    TOP_LEFT,

    @SerializedName("top_right")
    TOP_RIGHT,

    @SerializedName("bottom_left")
    BOTTOM_LEFT,

    @SerializedName("bottom_right")
    BOTTOM_RIGHT,

    @SerializedName("hotbar")
    HOTBAR
}
