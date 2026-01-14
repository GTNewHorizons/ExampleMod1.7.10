package com.rhghunter.rhgmodpackextra;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAutoFishingRod extends ItemFishingRod {

    public ItemAutoFishingRod() {
        super();
        this.setUnlocalizedName("auto_fishing_rod");
        this.setTextureName("rhgmodpackextra:auto_fishing_rod");
    }

    // Toggle Auto_Mode on Right Click
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntitPlayer Player) {
        // We let thevanilla logic handle the actual casting/reeling
        super.onItemRightClick(stack, world, player);

        // NBT data to track if "Auto Mode" is currently ON
        if (!world.isRemote) {
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
            }

            // If we just cast the line (fishEntity exists), turn Auto ON.
            // If we just reeled in (fishEntity is null), turn Auto OFF.
            boolean isFishing = (player.fishEntity != null);
            stack.getTagCompount().setBoolean("AutoFishActive", isFishing);

            if (isFishing) {
                player.addChatMessage(new ChatComponentText("Auto_Fisher enabled!"));
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("AutoFishActive")) {
            list.add(EnumChatFormatting.GREEN + "Status: ACTIVE");
        } else {
            list.add(EnumChatFormatting.RED + "Status: INACTIVE");
        }
    }
}
