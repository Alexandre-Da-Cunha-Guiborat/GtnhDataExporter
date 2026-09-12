package com.kyxbob.gtnhdataexporter.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyxbob.gtnhdataexporter.GtnhDataExporter;
import com.kyxbob.gtnhdataexporter.exporter.models.Resource;

public class ItemExporter {

    public static final void export() {
        List<Resource> registeredResource = new ArrayList<Resource>();
        for (Object obj : Item.itemRegistry) {
            Item item = (Item) obj;

            List<ItemStack> itemStackUnderSameId = new ArrayList<ItemStack>();
            item.getSubItems(item, null, itemStackUnderSameId);

            for (ItemStack itemStack : itemStackUnderSameId) {
                registeredResource.add(new Resource(itemStack));
            }
        }

        // GOOD CODE
        // HashMap<String, Integer> IdToOccurenceMaping = new HashMap<String, Integer>();
        // for (Resource r : registeredResource) {
        // if (IdToOccurenceMaping.containsKey(r.getId())) {
        // IdToOccurenceMaping.replace(r.getId(), IdToOccurenceMaping.get(r.getId()) + 1);
        // } else {
        // IdToOccurenceMaping.put(r.getId(), +1);
        // }
        // }

        // GtnhDataExporter.LOG
        // .info("registered=" + registeredResource.size() + " vs " + "unique=" + IdToOccurenceMaping.size());
        // for (Entry<String, Integer> idToOccurenceMappingEntry : IdToOccurenceMaping.entrySet()) {
        // if (idToOccurenceMappingEntry.getValue() != 1) {
        // GtnhDataExporter.LOG
        // .info(idToOccurenceMappingEntry.getKey() + "=" + idToOccurenceMappingEntry.getValue());
        // }
        // }

        // TEMP CODE
        HashMap<String, List<Resource>> resourcesById = new HashMap<String, List<Resource>>();

        for (Resource r : registeredResource) {
            String id = r.getId();

            List<Resource> resources = resourcesById.get(id);

            if (resources == null) {
                resources = new ArrayList<Resource>();
                resourcesById.put(id, resources);
            }

            resources.add(r);
        }

        GtnhDataExporter.LOG.info("registered=" + registeredResource.size() + " vs unique=" + resourcesById.size());

        for (Entry<String, List<Resource>> entry : resourcesById.entrySet()) {
            if (entry.getValue()
                .size() > 1) {
                GtnhDataExporter.LOG.info(
                    "DUPLICATE " + entry.getKey()
                        + " x"
                        + entry.getValue()
                            .size());

                for (Resource r : entry.getValue()) {
                    GtnhDataExporter.LOG.info("  Resource: " + r);
                }
            }
        }

        save(registeredResource);
    }

    private static void save(List<Resource> registeredResource) {
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

}
