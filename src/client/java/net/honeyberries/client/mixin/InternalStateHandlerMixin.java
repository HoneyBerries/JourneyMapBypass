package net.honeyberries.client.mixin;

import journeymap.client.InternalStateHandler;
import journeymap.common.properties.GlobalProperties;
import journeymap.common.properties.ServerOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Lifts every server restriction that JourneyMap enforces on the client. The server's per-player
 * payload is parsed into a {@link GlobalProperties} at the top of {@code setStates}; that object is
 * rewritten in place before it fans out to the state setters and {@code FeatureManager}.
 * <p>
 * Deliberately untouched, because the server enforces them itself and lifting them client-side
 * would achieve nothing: teleport, expanded/world player radar (server-filtered), and
 * global-waypoints-only.
 */
@Mixin(InternalStateHandler.class)
public class InternalStateHandlerMixin {

	@ModifyVariable(method = "setStates", at = @At("STORE"), name = "prop")
	private GlobalProperties journeymapbypass$liftRestrictions(GlobalProperties prop) {
		prop.journeymapEnabled.set(true);
		prop.minimapEnabled.set(true);
		prop.hideCoordinates.set(false);

		prop.surfaceMapping.set(ServerOption.ALL);
		prop.topoMapping.set(ServerOption.ALL);
		prop.biomeMapping.set(ServerOption.ALL);
		prop.caveMapping.set(ServerOption.ALL);
		prop.surfaceRenderRange.set(0);
		prop.caveRenderRange.set(0);

		prop.allowWaypoints.set(true);
		prop.showInGameBeacons.set(true);
		prop.allowDeathPoints.set(true);
		prop.allowMultiplayerSettings.set(ServerOption.ALL);

		prop.radarEnabled.set(ServerOption.ALL);
		prop.playerRadarEnabled.set(true);
		prop.playerRadarNamesEnabled.set(true);
		prop.villagerRadarEnabled.set(true);
		prop.animalRadarEnabled.set(true);
		prop.mobRadarEnabled.set(true);
		prop.radarLateralDistance.set(512);
		prop.radarVerticalDistance.set(320);
		prop.maxPlayersData.set(128);
		prop.maxVillagersData.set(128);
		prop.maxAnimalsData.set(128);
		prop.maxAmbientCreaturesData.set(128);
		prop.maxMobsData.set(128);
		return prop;
	}
}
