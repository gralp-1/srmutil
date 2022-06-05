package com.srmworks.srmutil.features.mixin;

import com.srmworks.srmutil.SRMUtil;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;

import static com.srmworks.srmutil.SRMUtil.CLIENT;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotMixin {

    @Shadow
    private File file;

    @Inject(at = @At("HEAD"), method = "getScreenshotFilename", cancellable = true)
    private static void getScreenshotFileNameMixin(File directory, CallbackInfoReturnable<File> cir) {
        File file_name = formatFile(directory);
        cir.setReturnValue(file_name);
        ClickEvent openFile = new ClickEvent(ClickEvent.Action.OPEN_FILE, file_name.getAbsolutePath());
        Style style = Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(openFile);
        CLIENT.player.sendMessage(new LiteralText("Click here to open screenshot!").styled(st -> style), false);
        cir.cancel();
    }

    @Inject(at = @At("HEAD"), method = "saveScreenshotInner", cancellable = true)
    private static void saveScreenshotInnerMixin(File gameDirectory, String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
    }

    private static File formatFile(File directory) {

//      Make a new folder if it doesn't exist with the name of the world / server
        File new_dir =
                new File(directory.getAbsolutePath() + "/" +
                        (CLIENT.isInSingleplayer() ?
                                CLIENT.getServer().getSaveProperties().getLevelName() :
                                CLIENT.getCurrentServerEntry().name));
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
//		Make a new date-time string in the format YYYY-MM-DD\ HH:MM:SS\ X\ Y\ Z\ Dimension\ Biome.png
        StringBuilder file_name = new StringBuilder();


//		Get the dateTime formatted
        DateFormat dateFormat = new SimpleDateFormat(SRMUtil.date_format);
        file_name.append(dateFormat.format(System.currentTimeMillis()));
//		Get the player's position
        PlayerEntity player = CLIENT.world.getPlayerByUuid(CLIENT.player.getUuid());
        if (player != null) {
            file_name.append(" (");
            file_name.append((int) player.getX());
            file_name.append(", ");
            file_name.append((int) player.getZ());
            file_name.append(") ");
        }
        file_name.append(CLIENT.player.world.getRegistryKey().getValue().toString().split(":")[1]);
        file_name.append(".png");
        return new File(new_dir, file_name.toString());
    }
}