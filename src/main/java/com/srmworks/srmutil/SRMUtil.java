package com.srmworks.srmutil;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SRMUtil implements ModInitializer {
    public static final String MODID = "srmutil";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static File most_recent_screenshot;

    @Override
    public void onInitialize() {
        LOGGER.info("SRMUtil loaded!");
    }

    public Identifier id(String name) {
        return new Identifier(MODID, name);
    }
}
