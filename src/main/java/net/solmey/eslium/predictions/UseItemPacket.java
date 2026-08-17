package net.solmey.eslium.predictions;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class UseItemPacket {

    public static void onSentPacket(Packet<?> packet) {
        if (packet instanceof ServerboundUseItemPacket sPacket) {
            Player player = Minecraft.getInstance().player;

            //this.ackBlockChangesUpTo(sPacket.getSequence());
			Level level = player.level();
			InteractionHand hand = sPacket.getHand();
			ItemStack itemStack = player.getItemInHand(hand);
			//player.resetLastActionTime();

			if (
                !itemStack.getItem().equals(Items.ENDER_PEARL) &&
                !itemStack.getItem().equals(Items.WIND_CHARGE) &&
                !itemStack.getItem().equals(Items.CHORUS_FRUIT)
            ) return;
			if (!itemStack.isEmpty() && itemStack.isItemEnabled(level.enabledFeatures())) {
				/*float targetYRot = Mth.wrapDegrees(sPacket.getYRot());
				float targetXRot = Mth.wrapDegrees(sPacket.getXRot());
				if (targetXRot != player.getXRot() || targetYRot != player.getYRot()) {
					player.absSnapRotationTo(targetYRot, targetXRot);
			    }*/


				if (/*player.gameMode.*/useItem(player, level, itemStack, hand) instanceof InteractionResult.Success success
					&& success.swingSource() == InteractionResult.SwingSource.SERVER) {
					//player.swing(hand, true);
				}
			}
        }
    }

    private static InteractionResult useItem(Player player, Level level, ItemStack itemStack, InteractionHand hand) {
        if (player.gameMode() == GameType.SPECTATOR) {
			return InteractionResult.PASS;
		}

		if (player.getCooldowns().isOnCooldown(itemStack)) {
			return InteractionResult.PASS;
		}

		int oldCount = itemStack.getCount();
		int oldDamage = itemStack.getDamageValue();
		InteractionResult result = use(itemStack, level, player, hand); // MODIFIED VERSION OF itemStack.use to avoid real impact
		/*ItemStack resultStack;
		if (result instanceof InteractionResult.Success success) {
			resultStack = Objects.requireNonNullElse(success.heldItemTransformedTo(), player.getItemInHand(hand));
		} else {
			resultStack = player.getItemInHand(hand);
		}

		if (resultStack == itemStack && resultStack.getCount() == oldCount && resultStack.getUseDuration(player) <= 0 && resultStack.getDamageValue() == oldDamage) {
			return result;
		}

		if (result instanceof InteractionResult.Fail && resultStack.getUseDuration(player) > 0 && !player.isUsingItem()) {
			return result;
		}

		if (itemStack != resultStack) {
			player.setItemInHand(hand, resultStack);
		}

		if (resultStack.isEmpty()) {
			player.setItemInHand(hand, ItemStack.EMPTY);
		}

		if (!player.isUsingItem()) {
			player.inventoryMenu.sendAllDataToRemote();
		}*/

		return result;
    }

   	private static InteractionResult use(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
		ItemStack stackBeforeUse = itemStack.copy();
		boolean isInstantlyUsed = itemStack.getUseDuration(player) <= 0;
		InteractionResult result = InteractionResult.SUCCESS; //itemStack.getItem().use(level, player, hand);
		// ^ with vanilla items, only ender pearls, wind charges and chorus fruts have a cooldown. Good thing for me, they always return success so I don't have to rollback haha
		//
		//itemStack.set(DataComponents.USE_REMAINDER, null);
		if(isInstantlyUsed && result instanceof InteractionResult.Success success) {
		    itemStack.applyAfterUseComponentSideEffects((LivingEntity) player, stackBeforeUse);
		}
		return result;
		/*return isInstantlyUsed && result instanceof InteractionResult.Success success
			? success.heldItemTransformedTo(
				success.heldItemTransformedTo() == null
					? itemStack.applyAfterUseComponentSideEffects(player, stackBeforeUse)
					: success.heldItemTransformedTo().applyAfterUseComponentSideEffects(player, stackBeforeUse)
			)
			: result;*/
	}
}
