package net.solmey.eslium.predictions;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UseItemOnPacket {

    public static void onSentPacket(Packet<?> packet) {
        if (packet instanceof ServerboundUseItemOnPacket sPacket) {
            Player player = Minecraft.getInstance().player;
            Level level = player.level();
            InteractionHand hand = sPacket.hand();
            ItemStack itemStack = player.getItemInHand(hand);

            if (
                !itemStack.getItem().equals(Items.END_CRYSTAL) &&
                !itemStack.getItem().equals(Items.MINECART)
            ) return;

            if (itemStack.isItemEnabled(level.enabledFeatures())) {
                BlockHitResult blockHit = sPacket.hitResult();
                //Vec3 location = blockHit.getLocation();
                BlockPos pos = blockHit.getBlockPos();

                int maxY = level.getMaxY();
                int minY = level.getMinY();
                if (pos.getY() > maxY) {
                    //player.sendBuildLimitMessage(true, maxY);
                } else if (pos.getY() < minY) {
                    //player.sendBuildLimitMessage(false, minY);
                } else {
                    // Call of the method useItemOn of ServerPlayerGameMode
                    BlockState state = level.getBlockState(pos);
                    if (!state.getBlock().isEnabled(level.enabledFeatures())) {
                        //return InteractionResult.FAIL;
                        return;
                    }
                    if (player.gameMode() == GameType.SPECTATOR) {
                        return;
                    }

                    boolean haveSomethingInOurHands =
                        !player.getMainHandItem().isEmpty() ||
                        !player.getOffhandItem().isEmpty();
                    boolean suppressUsingBlock =
                        player.isSecondaryUseActive() &&
                        haveSomethingInOurHands;

                    ItemStack usedItemStack = itemStack.copy();

                    if (!suppressUsingBlock) {
                        // Always assume that this part never cancels the prediction

                        /*
                        InteractionResult itemUse = state.useItemOn(
                            player.getItemInHand(hand).copy(),
                            (Level) level,
                            (Player) player,
                            hand,
                            hitResult
                        );
                        if (itemUse.consumesAction()) {
                            return;
                        }

                        if (
                            itemUse instanceof
                                InteractionResult.TryEmptyHandInteraction &&
                            hand == InteractionHand.MAIN_HAND
                        ) {
                            InteractionResult use = state.useWithoutItem(
                                (Level) level,
                                (Player) player,
                                hitResult
                            );
                            if (use.consumesAction()) {
                                return;
                            }
                        }*/
                    }

                    if (
                        !itemStack.isEmpty() &&
                        !player.getCooldowns().isOnCooldown(itemStack)
                    ) {
                        UseOnContext context = new UseOnContext(
                            player,
                            hand,
                            blockHit
                        );
                        InteractionResult success;
                        if (player.hasInfiniteMaterials()) {
                            int count = itemStack.getCount();
                            success = itemStack.useOn(context);
                            itemStack.setCount(count);
                        } else {
                            success = itemStack.useOn(context);
                        }

                        /*if (success.consumesAction()) {
           					CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, usedItemStack);
        				}*/
                    }

                    // End of the call of useItemOn of ServerPlayerGameMode

                    // No interest at predicting this :
                    //this.send(new ClientboundBlockUpdatePacket(level, pos));
                    //this.send(new ClientboundBlockUpdatePacket(level, pos.relative(direction)));
                }
            }
        }
    }
}
