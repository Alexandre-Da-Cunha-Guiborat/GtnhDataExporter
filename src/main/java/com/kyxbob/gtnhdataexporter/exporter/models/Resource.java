package com.kyxbob.gtnhdataexporter.exporter.models;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import com.kyxbob.gtnhdataexporter.GtnhDataExporter;
import com.kyxbob.gtnhdataexporter.exporter.enums.ResourceType;

public class Resource {

    public Resource(String id, String displayName, ResourceType type) {
        _id = id;
        _displayName = displayName;
        _type = type;
    }

    public Resource(ItemStack itemStack) {
        _id = computeId(itemStack);
        _displayName = itemStack.getDisplayName();
        _type = ResourceType.ITEM;
    }

    public String getId() {
        return _id;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public ResourceType getType() {
        return _type;
    }

    private final String _id;
    private final String _displayName;
    private final ResourceType _type;

    private static String computeId(ItemStack itemStack) {
        // Kind of unique, for some reasons some items have duplicate names.
        Item item = itemStack.getItem();
        String registryName = (String) Item.itemRegistry.getNameForObject(item);

        String id = registryName + ":" + itemStack.getItemDamage();
        if (itemStack.hasTagCompound()) {
            id += ":" + hashNBT(itemStack.getTagCompound());
        }

        try {
            IIcon itemIcon = item.getIcon(itemStack, 0);
            if (itemIcon != null && itemIcon.getIconName() != null) {
                id += ":" + itemIcon.getIconName();
            }
        } catch (Exception e) {
            GtnhDataExporter.LOG
                    .warn("Failed to get icon for " + registryName + " (meta " + itemStack.getItemDamage() + ")", e);
        }

        return id;
    }

    private static String hashNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return "0";
        }

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(byteStream);

            CompressedStreamTools.write(nbt, dataStream);
            dataStream.flush();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(byteStream.toByteArray());

            StringBuilder result = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                result.append(String.format("%02x", b & 0xff));
            }

            return result.toString();

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash NBT", e);
        }
    }

}
