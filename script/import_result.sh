#! /usr/bin/sh

fml_client_log_source_path=/home/alexandre/.local/share/PrismLauncher/instances/GTNHV2.8.4/.minecraft/logs/fml-client-latest.log
fml_client_log_target_path=/home/alexandre/Workspace/10-Git/GtnhDataExporter/script/output/log/fml-client-latest.log
rm $fml_client_log_target_path
cp $fml_client_log_source_path $fml_client_log_target_path

latest_log_source_path=/home/alexandre/.local/share/PrismLauncher/instances/GTNHV2.8.4/.minecraft/logs/latest.log
latest_log_target_path=/home/alexandre/Workspace/10-Git/GtnhDataExporter/script/output/log/latest.log
rm $latest_log_target_path
cp $latest_log_source_path $latest_log_target_path

export_source_path=/home/alexandre/.local/share/PrismLauncher/instances/GTNHV2.8.4/.minecraft/config/gtnhdataexporter/output
export_target_path=/home/alexandre/Workspace/10-Git/GtnhDataExporter/script/output/export
rm -r $export_target_path/*
cp -r $export_source_path $export_target_path