package net.honeyberries.client;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.common.JourneyMapPlugin;
import journeymap.api.v2.common.event.ClientEventRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/** JourneyMap API entrypoint ("journeymap" in fabric.mod.json). */
@JourneyMapPlugin(apiVersion = "2.0.0")
public class JourneyMapEnhancementsPlugin implements IClientPlugin {
	public static final String MOD_ID = "journeymapenhancements";
	/** Lang key; also the id JourneyMap stores in the info-slot setting. Tooltip is this + ".tooltip". */
	public static final String SPEED_3D_KEY = "journeymapenhancements.info_slot.movement_speed_3d";

	@Override
	public void initialize(IClientAPI api) {
		// The first register() arg is the slot's owner id. JourneyMap prefixes tooltips of slots that have
		// one ("journeymapenhancements:Displays..."); the empty id is what JourneyMap's built-in slots use.
		ClientEventRegistry.INFO_SLOT_REGISTRY_EVENT.subscribe(MOD_ID, event ->
				event.register("", Component.translatable(SPEED_3D_KEY), 10L, JourneyMapEnhancementsPlugin::speed3d));
	}

	@Override
	public String getModId() {
		return MOD_ID;
	}

	/** Like JourneyMap's movement speed (blocks/sec from per-tick position delta) but includes the Y axis. */
	private static Component speed3d() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return Component.literal("");
		}
		double dx = player.getX() - player.xOld;
		double dy = player.getY() - player.yOld;
		double dz = player.getZ() - player.zOld;
		double blocksPerSecond = Math.sqrt(dx * dx + dy * dy + dz * dz) / 0.05;
		return Component.translatable(SPEED_3D_KEY + ".game", String.format("%.2f", blocksPerSecond));
	}
}
