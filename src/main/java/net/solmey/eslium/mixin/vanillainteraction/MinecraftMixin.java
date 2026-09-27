package net.solmey.eslium.mixin.vanillainteraction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.interactions.InteractionManager;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    // pick is inside renderFrame for the outlines on blocks too
    @Inject(method = "renderFrame", at = @At("HEAD"))
    private void eslium$renderFrameHEAD(
        boolean advanceGameTime,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.blockthread();
        InteractionManager.addAllPredictions();
    }

    @Inject(method = "renderFrame", at = @At("TAIL"))
    private void eslium$renderFrameTAIl(
        boolean advanceGameTime,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.removeAllPredictions();
        InteractionManager.unblockthread();
    }
}
