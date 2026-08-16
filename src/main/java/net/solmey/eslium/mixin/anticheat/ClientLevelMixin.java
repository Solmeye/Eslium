package net.solmey.eslium.mixin.anticheat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.rollback.InteractionManager;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {


    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void eslium$tickEntitiesHEAD(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.showEntities();
    }

    @Inject(method = "tickEntities", at = @At("TAIL"))
    private void eslium$tickEntitiesTAIL(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.hideEntities();
    }


    /*@Inject(method = "tickEntities", at = @At("TAIL"))
    private void eslium$tickEntities(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        ClientLevel clientLevel = (ClientLevel) (Object) this;




        List<Entity> realEntities = new ArrayList<>();
        for (Entity entity : clientLevel.entitiesForRendering()) {
            realEntities.add(entity);
        }


        for (Entity entity : realEntities) {
            clientLevel.entityStorage.removeEntity(entity);
        }



        InteractionManager.showEntities();

        // If incompatibility with another mod I might just call tickEntites itself
        clientLevel.tickingEntities.forEach(entity -> {
			if (!entity.isRemoved() && !entity.isPassenger() && !clientLevel.tickRateManager.isEntityFrozen(entity)) {
				clientLevel.guardEntityTick(clientLevel::tickNonPassenger, entity);
			}
		});

        InteractionManager.hideEntities();


        for (Entity entity : realEntities) {
            clientLevel.entityStorage.addEntity(entity);
        }


        }*/
}
