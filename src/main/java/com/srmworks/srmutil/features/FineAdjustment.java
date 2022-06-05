package com.srmworks.srmutil.features;


import com.srmworks.srmutil.SRMUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static com.srmworks.srmutil.SRMUtil.CLIENT;

public class FineAdjustment {

    public static FineAdjustment INSTANCE = new FineAdjustment();

    public void registerControls() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Fine Adjustment",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_RIGHT_CONTROL,
                        "SRMUtil"
                )
        );
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    if (keyBinding.wasPressed()) {
                        assert CLIENT.player != null;
//                        Snap to the snap constant
                        double new_yaw = Math.round(CLIENT.player.getYaw() / SRMUtil.snap_amount) * SRMUtil.snap_amount;
                        CLIENT.player.setYaw((float) new_yaw);
                    }
                }
        );
    }
}
