package com.speedrun;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;

import java.awt.*;

public class SpeedrunTimer {
    public static final int BAR_HEIGHT = 5;  // height of timer bar in pixels

    private final int barOffset; // height of the health bar above the enemy in local coordinates

    @Getter
    private final NPC npc;  // NPC this timer is attached to

    @Getter
    private final int barWidth;  // width of timer bar in pixels

    @Getter
    private final int totalTicks;  // initial number of ticks on timer

    @Getter
    private int ticksUntilFinished;  // number of ticks until the timer expires

    public SpeedrunTimer(NPC npc, int barWidth, int ticks, int barOffset) {
        this.npc = npc;
        this.barWidth = barWidth;
        this.barOffset = barOffset;
        totalTicks = ticks;
        ticksUntilFinished = ticks;
    }

    public void renderBar(Graphics2D graphics, Client client) {
        LocalPoint npcLocation = npc.getLocalLocation();
        var wv = client.getWorldView(npcLocation.getWorldView());
        int plane = wv.getPlane();
        int xNPC = npcLocation.getX();
        int yNPC = npcLocation.getY();

        int zBar = npc.getLogicalHeight() + barOffset;
        int tileHeight = Perspective.getTileHeight(client, npcLocation, plane);

        Point snapLocation = Perspective.localToCanvas(client, xNPC, yNPC, tileHeight - zBar);
        Point imageLocation = new Point(
                snapLocation.getX() - barWidth / 2,
                snapLocation.getY() + BAR_HEIGHT / 2
        );

        int fillWidth = (int)Math.ceil((double)barWidth * (double)ticksUntilFinished / (double)totalTicks);

        graphics.setColor(Color.BLUE);
        graphics.fillRect(imageLocation.getX(), imageLocation.getY(), fillWidth, BAR_HEIGHT);
        graphics.setColor(Color.YELLOW);
        if(barWidth - fillWidth > 0) {
            graphics.fillRect(imageLocation.getX() + fillWidth, imageLocation.getY(), barWidth - fillWidth, BAR_HEIGHT);
        }
    }

    public void onGameTick(GameTick event)
    {
        ticksUntilFinished--;
    }

    public boolean isFinished()
    {
        return ticksUntilFinished == 0;
    }
}
