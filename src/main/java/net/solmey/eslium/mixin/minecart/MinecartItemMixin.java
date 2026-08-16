package net.solmey.eslium.mixin.minecart;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.server.MixinMode;
import net.solmey.eslium.server.SimulatedLevel;

@Mixin(MinecartItem.class)
public class MinecartItemMixin {

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

        if(!ConfigManager.getConfig().minecart.enabled)
            return;



        MinecartItem minecartItem = (MinecartItem) (Object) this;

        Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockState = level.getBlockState(pos);
		if (!blockState.is(BlockTags.RAILS)) {
			callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
			return;
		}

		ItemStack itemStack = context.getItemInHand();
		RailShape shape = blockState.getBlock() instanceof BaseRailBlock
			? blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty())
			: RailShape.NORTH_SOUTH;
		double offset = 0.0;
		if (shape.isSlope()) {
			offset = 0.5;
		}

		Vec3 spawnPos = new Vec3(pos.getX() + 0.5, pos.getY() + 0.0625 + offset, pos.getZ() + 0.5);
		AbstractMinecart cart = AbstractMinecart.createMinecart(
			level, spawnPos.x, spawnPos.y, spawnPos.z, minecartItem.type, EntitySpawnReason.DISPENSER, itemStack, context.getPlayer()
		);
		if (cart == null) {
			callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
			return;
		}

		if (AbstractMinecart.useExperimentalMovement(level)) {
			for (Entity entity : level.getEntities(null, cart.getBoundingBox())) {
				if (entity instanceof AbstractMinecart) {
					callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
					return;
				}
			}
		}

		//if (level instanceof ServerLevel serverLevel) {
		    SimulatedLevel.addEntity( (ClientLevel) level, cart);
			//serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), serverLevel.getBlockState(pos.below())));
		//}

		itemStack.shrink(1);
		callbackInfoReturnable.setReturnValue(InteractionResult.SUCCESS);
		return;
	}
}
