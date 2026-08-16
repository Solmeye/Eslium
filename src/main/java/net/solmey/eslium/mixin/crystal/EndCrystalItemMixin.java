package net.solmey.eslium.mixin.crystal;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.server.MixinMode;
import net.solmey.eslium.server.SimulatedLevel;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {

    @Inject(
        method = "useOn",
        at = @At(value = "HEAD"),
        cancellable = true
   )
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
        if (!Eslium.shouldWork())
            return;
        if(!MixinMode.mixinMode)
            return;

        if(!ConfigManager.getConfig().crystal.enabled)
            return;

		ClientLevel level = (ClientLevel) context.getLevel();

		BlockPos pos = context.getClickedPos();
		BlockState blockState = level.getBlockState(pos);
		if (!blockState.is(Blocks.OBSIDIAN) && !blockState.is(Blocks.BEDROCK)) {
			callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
			return;
		}

		BlockPos above = pos.above();
		if (!level.isEmptyBlock(above)) {
			callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
			return;
		}

		double x = above.getX();
		double y = above.getY();
		double z = above.getZ();
		List<Entity> entities = level.getEntities(null, new AABB(x, y, z, x + 1.0, y + 2.0, z + 1.0));
		if (!entities.isEmpty()) {
		    callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
			return;
		}

		//if (level instanceof ServerLevel serverLevel) {
			EndCrystal crystal = new EndCrystal(level, x + 0.5, y, z + 0.5);
			crystal.setShowBottom(false);
			SimulatedLevel.addEntity(level, crystal); // level.addEntity(crystal);
			/*level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, above);
			EnderDragonFight fight = serverLevel.getDragonFight();
			if (fight != null) {
				fight.tryRespawn();
			}*/
		//}

		context.getItemInHand().shrink(1);
		callbackInfoReturnable.setReturnValue(InteractionResult.SUCCESS);
		return;
	}
}
