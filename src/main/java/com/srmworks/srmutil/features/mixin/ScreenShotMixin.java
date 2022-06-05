package com.srmworks.srmutil.features.mixin;

import com.srmworks.srmutil.SRMUtil;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.srmworks.srmutil.SRMUtil.CLIENT;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenShotMixin {

    @Inject(at = @At("HEAD"), method = "getScreenshotFilename", cancellable = true)
    private static void getScreenshotFileNameMixin(File directory, CallbackInfoReturnable<File> cir) {
//      Make a new folder if it doesn't exist with the name of the world / serverk
        File new_dir =
                new File(directory.getAbsolutePath() + "/" +
                        (CLIENT.isInSingleplayer() ?
                                CLIENT.getServer().getSaveProperties().getLevelName() :
                                CLIENT.getCurrentServerEntry().name));
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
//		Make a new date-time string in the format YYYY-MM-DD\ HH:MM:SS\ X\ Y\ Z\ Dimension\ Biome.png
        StringBuilder sb = new StringBuilder();


//		Get the dateTime formatted
        DateFormat dateFormat = new SimpleDateFormat(SRMUtil.date_format);
        sb.append(dateFormat.format(System.currentTimeMillis()));
//		Get the player's position
        PlayerEntity player = CLIENT.world.getPlayerByUuid(CLIENT.player.getUuid());
        if (player != null) {
            sb.append(" (");
            sb.append((int) player.getX());
            sb.append(", ");
            sb.append((int) player.getZ());
            sb.append(") ");
        }
        sb.append(CLIENT.player.world.getRegistryKey().getValue().toString().split(":")[1]);
        sb.append(".png");

        cir.setReturnValue(new File(new_dir, sb.toString()));
    }
}