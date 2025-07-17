package com.speedrun;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpeedrunOverlay extends Overlay {

    @Inject
    Client client;

    // Store timers indexed by NPC; this makes it possible to have multiple
    // bars on-screen at once, but this capability is currently not used.
    private Map<NPC, SpeedrunTimer> timers;

    public void setupSpeedrunOverlay()
    {
        // Run configuration code on the Overlay
        setLayer(OverlayLayer.ABOVE_SCENE);
        setMovable(false);
        setSnappable(false);
        setPreferredLocation(new java.awt.Point(0, 0));
        timers = new HashMap<>();
    }

    public void addTimer(SpeedrunTimer timer)
    {
        // Add a new speedrun timer bar
        timers.put(timer.getNpc(), timer);
    }

    public boolean isRunning(NPC npc)
    {
        // Whether a speedrun timer bar is already running for the given NPC
        return timers.containsKey(npc);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Render all speedrun timer bars
        for(SpeedrunTimer timer : timers.values())
        {
            timer.renderBar(graphics, client);
        }
        return null;
    }

    public void onGameTick(GameTick event)
    {
        // Update speedrun timer bars
        for(SpeedrunTimer timer : timers.values())
        {
            timer.onGameTick(event);
            if(timer.isFinished())
            {
                timers.remove(timer.getNpc());
            }
        }
    }

    public void onNpcDespawned(NpcDespawned event)
    {
        // Remove speedrun timer bars for despawned NPCs
        timers.remove(event.getNpc());
    }
}
