package com.kyxbob.gtnhdataexporter.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyxbob.gtnhdataexporter.GtnhDataExporter;
import com.kyxbob.gtnhdataexporter.exporter.models.Resource;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemExporter {

    public static void export(final ICommandSender sender) {
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc != null) {
                // func_152344_a(Runnable) is the RetroFuture main-thread task scheduler:
                // it runs the runnable on the client render thread (the thread that owns
                // the current GL context), which is exactly where item.getIcon() is safe.
                mc.func_152344_a(new Runnable() {

                    @Override
                    public void run() {
                        runExport(sender);
                    }
                });
                return;
            }
        }

        GtnhDataExporter.LOG.warn("Item icons require a client GL context; running the export without icon data.");
    }

    private static void runExport(ICommandSender sender) {
        List<Resource> registeredResource = computeRegisteredResources();
        saveRegisteredResources(registeredResource);

        debugCheckRegisteredResources(registeredResource);

        GtnhDataExporter.LOG.info("Item export completed");
        notify(sender, "Item export completed");
    }

    private static void notify(ICommandSender sender, String message) {
        if (sender == null) {
            return;
        }

        try {
            sender.addChatMessage(new ChatComponentText(message));
        } catch (Exception e) {
            GtnhDataExporter.LOG.warn("Could not notify the command sender", e);
        }
    }

    private static List<Resource> computeRegisteredResources() {
        List<Resource> registeredResource = new ArrayList<Resource>();

        for (Object obj : Item.itemRegistry) {
            Item item = (Item) obj;

            List<ItemStack> itemStackUnderSameId = new ArrayList<ItemStack>();
            try {
                item.getSubItems(item, null, itemStackUnderSameId);
            } catch (Exception e) {
                GtnhDataExporter.LOG.warn("getSubItems failed for " + item, e);
                continue;
            }

            for (ItemStack itemStack : itemStackUnderSameId) {
                try {
                    Resource resource = new Resource(itemStack);
                    registeredResource.add(resource);
                } catch (Exception e) {
                    GtnhDataExporter.LOG.warn("Failed to build a resource for " + itemStack, e);
                }
            }
        }

        return registeredResource;
    }

    private static void saveRegisteredResources(List<Resource> registeredResource) {
        Gson gson = new GsonBuilder().setPrettyPrinting()
            .create();

        String jsonValue = gson.toJson(registeredResource);
        File output = new File(GtnhDataExporter.OUTPUTFOLDER, "save.json");

        FileWriter writer = null;

        try {
            writer = new FileWriter(output);
            writer.write(jsonValue);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void debugCheckRegisteredResources(List<Resource> registeredResource) {
        HashMap<String, Integer> IdToOccurenceMaping = new HashMap<String, Integer>();
        for (Resource r : registeredResource) {
            if (IdToOccurenceMaping.containsKey(r.getId())) {
                IdToOccurenceMaping.replace(r.getId(), IdToOccurenceMaping.get(r.getId()) + 1);
            } else {
                IdToOccurenceMaping.put(r.getId(), +1);
            }
        }

        GtnhDataExporter.LOG
            .info("registered=" + registeredResource.size() + " vs " + "unique=" + IdToOccurenceMaping.size());
        for (Entry<String, Integer> idToOccurenceMappingEntry : IdToOccurenceMaping.entrySet()) {
            if (idToOccurenceMappingEntry.getValue() != 1) {
                GtnhDataExporter.LOG
                    .info(idToOccurenceMappingEntry.getKey() + "=" + idToOccurenceMappingEntry.getValue());
            }
        }
    }
}
