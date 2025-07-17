package com.speedrun;

public enum SpeedrunBossData {
    VORKATH(140, 90),
    HESPORI(80, 60),
    VARDORVIS(100, 91),
    DUKE_SUCELLUS(160, 66),
    WHISPERER(100, 208),
    LEVIATHAN(140, 116, 128),
    AMOXLIATL(80, 50),
    FRAGMENT_OF_SEREN(120, 400),
    GALVEK(140, 300),
    GLOUGH(30, 150),
    HUEYCOATL(140, 250),
    NIGHTMARE(140, 1600),
    PHOSANIS_NIGHTMARE(140, 600),
    RAKS_FIRST_CHALLENGE(30, 75 - 11),  // 11 ticks are lost before Jad spawns
    ZULRAH(120, 90),
    PHANTOM_MUSPAH(120, 150);

    public final int healthBarOffset;  // height of health bar above enemy in local coordinates
    public final int healthBarWidth;  // width of health bar in pixels
    public final int speedrunTime;  // default target time for speed runs (from GMs)

    SpeedrunBossData(int healthBarWidth, int speedrunTime, int healthBarOffset)
    {
        this.healthBarOffset = healthBarOffset;
        this.healthBarWidth = healthBarWidth;
        this.speedrunTime = speedrunTime;
    }

    SpeedrunBossData(int healthBarWidth, int speedrunTime)
    {
        this(healthBarWidth, speedrunTime, 16); // Most NPCs health bar is 16 units above them
    }
}
