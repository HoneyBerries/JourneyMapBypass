package net.honeyberries.client.mixin;

import journeymap.client.JourneymapClient;
import journeymap.client.render.draw.DrawEntityStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Fixes player names shrinking to a tiny size once a player is outside the minimap and gets pinned
 * to its edge. In {@code drawOffscreen}, JourneyMap hardcodes the {@code fontScale} passed to
 * {@code drawPlayer} as {@code 1.0}, whereas on-map entities receive the minimap's configured font
 * scale ({@code DisplayVars#getFontScale()}, i.e. {@code MiniMapProperties#getFontScale()}).
 */
@Mixin(DrawEntityStep.class)
public class DrawEntityStepMixin {

	/** Index of the {@code fontScale} double in {@code drawPlayer(g, x, y, renderer, alpha, heading, fontScale, rotation)}. */
	@ModifyArg(
		method = "drawOffscreen(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/awt/geom/Point2D;Ljourneymap/client/render/map/Renderer;DZ)V",
		at = @At(
			value = "INVOKE",
			target = "Ljourneymap/client/render/draw/DrawEntityStep;drawPlayer(Lnet/minecraft/client/gui/GuiGraphicsExtractor;DDLjourneymap/client/render/map/Renderer;FDDD)V"
		),
		index = 6
	)
	private double journeymapbypass$useMinimapFontScale(double hardcodedFontScale) {
		return JourneymapClient.getInstance().getActiveMiniMapProperties().getFontScale();
	}
}
