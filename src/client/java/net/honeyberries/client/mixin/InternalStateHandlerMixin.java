package net.honeyberries.client.mixin;

import journeymap.client.InternalStateHandler;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Neutralizes only the server-side "disable JourneyMap" kill switch
 * ({@code jm.server.allow_journeymap} / {@code GlobalProperties#journeymapEnabled}).
 * Every other value read in {@link InternalStateHandler#setStates} (permissions,
 * radar, teleport, waypoint sync, etc.) passes through untouched.
 */
@Mixin(InternalStateHandler.class)
public class InternalStateHandlerMixin {

	@Redirect(
			method = "setStates",
			at = @At(
					value = "INVOKE",
					target = "Ljourneymap/common/properties/config/BooleanField;get()Ljava/lang/Boolean;",
					ordinal = 0
			)
	)
	private Boolean journeymapbypass$forceEnabled(BooleanField instance) {
		return Boolean.TRUE;
	}
}
