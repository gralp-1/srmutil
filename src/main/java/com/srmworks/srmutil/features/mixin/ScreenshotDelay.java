package com.srmworks.srmutil.features.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

import static net.minecraft.client.util.ScreenshotRecorder.saveScreenshot;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotDelay {

    @Inject(at = @At("HEAD"), method = "saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V", cancellable = true)
    private static void addScreenshotDelay(File gameDirectory, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
//        Start a new thread
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saveScreenshot(gameDirectory, null, framebuffer, messageReceiver);
        });
        thread.start();
        ci.cancel();
    }
}
