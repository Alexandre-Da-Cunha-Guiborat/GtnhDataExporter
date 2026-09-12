package com.kyxbob.gtnhdataexporter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.kyxbob.gtnhdataexporter.exporter.ItemExporter;

public class ExportCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return GtnhDataExporter.MODID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText("Executing the command : " + getCommandName()));
        ItemExporter.export();
        sender.addChatMessage(new ChatComponentText("Done"));
    }
}
