package net.honeyberries.client;

import net.fabricmc.api.ClientModInitializer;

public class JourneyMapBypassClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Intentionally empty. Info slots are registered via JourneyMapBypassPlugin (JourneyMap API).
	}
}
