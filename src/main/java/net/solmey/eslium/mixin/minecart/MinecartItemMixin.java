package net.solmey.eslium.mixin.minecart;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
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
		if (shape.isAscending()) {
			offset = 0.5;
		}


        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.0625 + offset;
        double f = pos.getZ() + 0.5;

        AbstractMinecart abstractMinecart = (AbstractMinecart)(switch (minecartItem.type) {

            case CHEST -> new MinecartChest(level, d, e, f);
            case FURNACE -> new MinecartFurnace(level, d, e, f);
            case TNT -> new MinecartTNT(level, d, e, f);
            case SPAWNER -> new MinecartSpawner(level, d, e, f);
            case HOPPER -> new MinecartHopper(level, d, e, f);
            case COMMAND_BLOCK -> new MinecartCommandBlock(level, d, e, f);
            default -> new Minecart(level, d, e, f);
        });
        //EntityType.createDefaultStackConfig(level, itemStack, context.getPlayer()).accept(abstractMinecart);


		//if (level instanceof ServerLevel serverLevel) {
		    SimulatedLevel.addEntity( (ClientLevel) level, abstractMinecart);
			//serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), serverLevel.getBlockState(pos.below())));
		//}

		itemStack.shrink(1);
		callbackInfoReturnable.setReturnValue(InteractionResult.SUCCESS);
		return;
	}
}
