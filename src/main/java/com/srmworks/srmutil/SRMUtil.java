package com.srmworks.srmutil;

import com.srmworks.srmutil.config.SimpleConfig;
import com.srmworks.srmutil.features.FineAdjustment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SRMUtil implements ModInitializer {
    public static final String MODID = "srmutil";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static final SimpleConfig CONFIG = SimpleConfig.of("SRMUtil").provider(SRMUtil::provider).request();
    public static final String date_format = CONFIG.getOrDefault("date_format", "yyyy-MM-dd HH:mm:ss");
    public static final float snap_amount = (float) CONFIG.getOrDefault("snap_amount", 45.0);

    private static String provider(String fname) {
        return """
                # SRMUtil config
                                
                # How much to snap by with fine adjustment
                snap_constant=45.0
                                
                # Screenshot date format (don't touch if you don't know what you're doing)
                # Docs here: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
                date_format="yyyy-MM-dd HH:mm:ss"
                """;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("SRMUtil loaded!");
        FineAdjustment.INSTANCE.registerControls();
    }

    public Identifier id(String name) {
        return new Identifier(MODID, name);
    }
}
