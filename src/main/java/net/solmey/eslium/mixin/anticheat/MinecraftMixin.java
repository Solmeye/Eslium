package net.solmey.eslium.mixin.anticheat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.rollback.InteractionManager;

@Mixin(GameRenderer.class)
public class MinecraftMixin {

    // pick is inside renderFrame for the outlines on blocks too
    @Inject(method = "render", at = @At("HEAD"))
    private void eslium$renderFrameHEAD(
        DeltaTracker deltaTracker,
        boolean renderLevel,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.showEntities();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void eslium$renderFrameTAIl(
        DeltaTracker deltaTracker,
        boolean renderLevel,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.hideEntities();
    }
}
