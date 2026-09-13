package com.kyxbob.gtnhdataexporter;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = GtnhDataExporter.MODID,
    version = Tags.VERSION,
    name = GtnhDataExporter.DISPLAYNAME,
    acceptedMinecraftVersions = "[1.7.10]")
public class GtnhDataExporter {

    public static final String MODID = "gtnhdataexporter";
    public static final String DISPLAYNAME = "GTNH Data Exporter";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static File CONFIGFOLDER;
    public static File OUTPUTFOLDER;

    @SidedProxy(
        clientSide = "com.kyxbob.gtnhdataexporter.ClientProxy",
        serverSide = "com.kyxbob.gtnhdataexporter.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        CONFIGFOLDER = new File(event.getModConfigurationDirectory(), MODID);
        if (!CONFIGFOLDER.exists()) {
            CONFIGFOLDER.mkdirs();
        }

        OUTPUTFOLDER = new File(CONFIGFOLDER, "output");
        if (!OUTPUTFOLDER.exists()) {
            OUTPUTFOLDER.mkdirs();
        }

    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about.
    // Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on
    // this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);

        event.registerServerCommand(new ExportCommand());
    }
}
