package com.speedrun;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("speedrun")
public interface SpeedrunConfig extends Config
{
    @ConfigItem(
            position = 0,
            keyName = "useCustomTime",
            name = "Use Custom Time",
            description = "Whether to override default speedrun times with a custom time."
    )
    default boolean useCustomTime()
    {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "customTime",
            name = "Custom Time (ticks)",
            description = "A custom speed run time (in ticks) to use with timers"
    )
    default int customTime()
    {
        return 1;
    }
}
