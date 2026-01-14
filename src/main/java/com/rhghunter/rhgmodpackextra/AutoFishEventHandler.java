package com.rhghunter.rhgmodpackextra;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;

public class AutoFishEventHandler {

    // Reflection allows us to read private variables.
    // "field_146043_c" is the obfuscated name for "ticksCatchable" in 1.7.10
    public static final String[] TICKS_CATCHABLE_FIELD_NAMES = new String[] { "field_146043_c", "ticksCatchable" };

    @SubscribeEvent
    public void onPlayerTick(TickEVent.PlayerTickEvent event) {
        // Only run on server side and at the end of the tick logic
        if (event.phase != TickEvent.phase.END || event.player.worldObj.isRemote) return;

        ItemStack heldItem = event.player.heldItem();

        // Cjecl ofplayer is holding the Auto Fishing Rod
        if (helfItem != null && heldItem.getItem instanceof ItemAutoFishingRod) {

            // Check if the rod has NBT and is set to "Active"
            if (heldItem.hasTagCompound() && heldItem.getTagCompound().getBoolean("AutoFishActive")) {

                // The hook is in the water
                if (event.player.fishEntity != null) {
                    checkForBiteAndReel(event.player, heldItem);
                }

                // The hook isn't in the water
                else {
                    // Right-click to fix
                    heldItem.useItemRightClick(event.player.worldObj, event.player);
                }
            }
        }
    }

    private void checkForBiteAndReel(net.minecraft.entity.player.EntityPlayer player, ItemStack rod) {
        EntityFishHook hook = player.fishEntity;

        try {
            // Use reflection to read "ticksCatchable"
            // This integer is > 0 when the bober dips
            int ticksCatchable = ReflectionHelper.getPrivateValue(EntityFishHook.class, hook, TICKS_CATCHABLE_FIELD_NAMES);

            if (ticksCatchable > 0) {
                // A fish bit
                // useItemRIghtClick
                rod.useItemRightClick(player.worldObj, player);

                // IMPORTANT: Since we just reeled in, the NBT logic in ItemAutoFishingRod 
                // might have flipped "AutoFishActive" to false. We need to force it back to TRUE
                // so the loop continues.
                if (rod.hasTagCompound()) {
                    rod.getTagCompound().setBoolean("AutoFishActive", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
