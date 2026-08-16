package net.solmey.eslium.mixin.entrypoint;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.solmey.eslium.Eslium;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At("RETURN")) // When the game loads
    private void eslium$onInit(GameConfig gameConfig, CallbackInfo callback) {
        Eslium.commonInit();
    }
}
