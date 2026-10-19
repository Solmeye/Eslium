package net.solmey.eslium.mixin.vanillainteraction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.interactions.InteractionManager;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {


    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void eslium$tickEntitiesHEAD(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.blockthread();
        InteractionManager.addAllPredictions();
    }

    @Inject(method = "tickEntities", at = @At("TAIL"))
    private void eslium$tickEntitiesTAIL(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.unblockthread();
    }
}
