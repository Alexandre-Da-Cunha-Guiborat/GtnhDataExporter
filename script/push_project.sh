#! /usr/bin/sh

mod_source_path=/home/alexandre/Workspace/10-Git/GtnhDataExporter/build/libs/_gtnhdataexporter-1.0.0.jar
mod_target_path=/home/alexandre/.local/share/PrismLauncher/instances/GTNHV2.8.4/.minecraft/mods/_gtnhdataexporter-1.0.0.jar

rm -r $mod_target_path
cp $mod_source_path $mod_target_path