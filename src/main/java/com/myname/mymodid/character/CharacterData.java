package com.myname.mymodid.character;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CharacterData implements IExtendedEntityProperties {

    public static final String IDENTIFIER = "DragonBlockXCharacter";

    private Race race = Race.NONE;
    private HairStyle hairStyle = HairStyle.BASE;
    private HairColor hairColor = HairColor.BLACK;
    private BodyColor bodyColor = BodyColor.LIGHT;
    private boolean created = false;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("race", race.ordinal());
        data.setInteger("hairStyle", hairStyle.ordinal());
        data.setInteger("hairColor", hairColor.ordinal());
        data.setInteger("bodyColor", bodyColor.ordinal());
        data.setBoolean("created", created);
        compound.setTag(IDENTIFIER, data);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(IDENTIFIER)) {
            return;
        }
        NBTTagCompound data = compound.getCompoundTag(IDENTIFIER);
        race = Race.fromOrdinal(data.getInteger("race"));
        hairStyle = HairStyle.fromOrdinal(data.getInteger("hairStyle"));
        hairColor = HairColor.fromOrdinal(data.getInteger("hairColor"));
        bodyColor = BodyColor.fromOrdinal(data.getInteger("bodyColor"));
        created = data.getBoolean("created");
    }

    @Override
    public void init(Entity entity, World world) {}

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public HairStyle getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(HairStyle hairStyle) {
        this.hairStyle = hairStyle;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public BodyColor getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(BodyColor bodyColor) {
        this.bodyColor = bodyColor;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void copyFrom(CharacterData other) {
        this.race = other.race;
        this.hairStyle = other.hairStyle;
        this.hairColor = other.hairColor;
        this.bodyColor = other.bodyColor;
        this.created = other.created;
    }

    public static CharacterData get(EntityPlayer player) {
        return (CharacterData) player.getExtendedProperties(IDENTIFIER);
    }

    public static void register(EntityPlayer player) {
        if (player.getExtendedProperties(IDENTIFIER) == null) {
            player.registerExtendedProperties(IDENTIFIER, new CharacterData());
        }
    }
}
