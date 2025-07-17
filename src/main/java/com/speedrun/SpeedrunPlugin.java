package com.speedrun;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;

import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Speedrun Timer"
)
public class SpeedrunPlugin extends Plugin
{
	public static final int VORKATH_ID = 8061;
	public static final int WHISPERER_ID = 12204;
	public static final int LEVIATHAN_ID = 12214;
	public static final int ZULRAH_ID = 2042;
	public static final int DUKE_SUCELLUS_ID = 12191;
	public static final int PHANTOM_MUSPAH_ID = 12078;
	public static final int AMOXLIATL_ID = 13685;
	public static final int FRAGMENT_OF_SEREN_ID = 8917;
	public static final int GALVEK_ID = 8095;
	public static final int JAD_ID = 10623;
	public static final int NIGHTMARE_ID = 9425;
	public static final int PHOSANIS_NIGHTMARE_ID = 9416;

	@Inject
	OverlayManager overlayManager;

	@Inject
	SpeedrunOverlay speedrunOverlay;

	@Inject
	SpeedrunConfig config;

	// Counts how many Jads spawned this tick
	// I couldn't find any other way to tell the one-jad challenge
	// apart from the other challenges :)
	private int jadsSpawnedThisTick;

	// The last Jad that was spawned
	private NPC lastJadSpawned;

	@Provides
	SpeedrunConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpeedrunConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		speedrunOverlay.setupSpeedrunOverlay();
		overlayManager.add(speedrunOverlay);
		log.info("Speedrun timer started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Speedrun timer stopped!");
		overlayManager.remove(speedrunOverlay);
	}

	private int getSpeedrunTime(int defaultTime)
	{
		return Math.max(1, config.useCustomTime() ? config.customTime() : defaultTime);
	}

	private SpeedrunTimer buildTimer(NPC npc, SpeedrunBossData data)
	{
		int time = getSpeedrunTime(data.speedrunTime);
		return new SpeedrunTimer(npc, data.healthBarWidth, time, data.healthBarOffset);
	}

	@Subscribe
	protected void onNpcChanged(NpcChanged event)
	{
		NPC npc = event.getNpc();
		int npcId = npc.getId();
		switch(npcId) {
			case VORKATH_ID:
				speedrunOverlay.addTimer(buildTimer(npc, SpeedrunBossData.VORKATH));
				break;
			case WHISPERER_ID:
				speedrunOverlay.addTimer(buildTimer(npc, SpeedrunBossData.WHISPERER));
				break;
			case DUKE_SUCELLUS_ID:
				speedrunOverlay.addTimer(buildTimer(npc, SpeedrunBossData.DUKE_SUCELLUS));
				break;
			case NIGHTMARE_ID:
				speedrunOverlay.addTimer(buildTimer(npc, SpeedrunBossData.NIGHTMARE));
				break;
			case PHOSANIS_NIGHTMARE_ID:
				speedrunOverlay.addTimer(buildTimer(npc, SpeedrunBossData.PHOSANIS_NIGHTMARE));
				break;
		}
		log.info("NPC changed {}, {}", npc.getName(), npc.getId());
	}

	@Subscribe
	protected void onHitsplatApplied(HitsplatApplied event)
	{
		if(event.getActor().getName().equals("Vardorvis"))
		{
			NPC npc = (NPC)event.getActor();
			if(speedrunOverlay.isRunning(npc))
			{
				return;
			}
			SpeedrunTimer newTimer = buildTimer(npc, SpeedrunBossData.VARDORVIS);
			speedrunOverlay.addTimer(newTimer);
		}
	}

	@Subscribe
	protected void onNpcSpawned(NpcSpawned event)
	{
		log.info("NPC spawned {}, {}", event.getNpc().getName(), event.getNpc().getId());
		int npcID = event.getNpc().getId();
		SpeedrunTimer newTimer = null;
		switch(npcID) {
			case LEVIATHAN_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.LEVIATHAN);
				break;
			case ZULRAH_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.ZULRAH);
				break;
			case PHANTOM_MUSPAH_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.PHANTOM_MUSPAH);
				break;
			case AMOXLIATL_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.AMOXLIATL);
				break;
			case FRAGMENT_OF_SEREN_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.FRAGMENT_OF_SEREN);
				break;
			case GALVEK_ID:
				newTimer = buildTimer(event.getNpc(), SpeedrunBossData.GALVEK);
				break;
			case JAD_ID:
				jadsSpawnedThisTick++;
				lastJadSpawned = event.getNpc();
				break;
		}
		if(newTimer != null)
		{
			speedrunOverlay.addTimer(newTimer);
		}
	}

	@Subscribe
	protected void onNpcDespawned(NpcDespawned event)
	{
		speedrunOverlay.onNpcDespawned(event);
	}

	@Subscribe
	protected void onGameTick(GameTick event)
	{
		speedrunOverlay.onGameTick(event);
		if(jadsSpawnedThisTick == 1)
		{
			SpeedrunTimer newTimer = buildTimer(lastJadSpawned, SpeedrunBossData.RAKS_FIRST_CHALLENGE);
			speedrunOverlay.addTimer(newTimer);
		}
		jadsSpawnedThisTick = 0;
	}

}
