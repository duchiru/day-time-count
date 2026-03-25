package me.duchiru.daytimecount.config;

import com.google.gson.annotations.SerializedName;

public enum TrackerStyle {
    @SerializedName("default")
    DEFAULT,

    @SerializedName("compact")
    COMPACT,

    @SerializedName("day_only")
    DAY_ONLY,

    @SerializedName("time_only")
    TIME_ONLY
}
