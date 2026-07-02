package com.example.chunkkey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ChunkKeyClient implements ClientModInitializer {
    private static KeyBinding keyIncrease;
    private static KeyBinding keyDecrease;

    @Override
    public void onInitializeClient() {
        // 1. Tạo và đăng ký phím mũi tên LÊN để tăng render distance
        keyIncrease = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Tăng Render Distance", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_UP, // Mặc định là nút mũi tên LÊN (UP ARROW)
            "ChunkKey"
        ));

        // 2. Tạo và đăng ký phím mũi tên XUỐNG để giảm render distance
        keyDecrease = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Giảm Render Distance", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_DOWN, // Mặc định là nút mũi tên XUỐNG (DOWN ARROW)
            "ChunkKey"
        ));

        // 3. Lắng nghe sự kiện bấm nút trong game
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Xử lý khi bấm nút Tăng
            while (keyIncrease.wasPressed()) {
                int current = client.options.getViewDistance().getValue();
                if (current < 32) {
                    client.options.getViewDistance().setValue(current + 1);
                    client.options.write();
                    client.player.sendMessage(Text.literal("Render Distance: ")
                        .append(Text.literal(String.valueOf(current + 1)).formatted(Formatting.GREEN)), true);
                }
            }

            // Xử lý khi bấm nút Giảm
            while (keyDecrease.wasPressed()) {
                int current = client.options.getViewDistance().getValue();
                if (current > 2) {
                    client.options.getViewDistance().setValue(current - 1);
                    client.options.write();
                    client.player.sendMessage(Text.literal("Render Distance: ")
                        .append(Text.literal(String.valueOf(current - 1)).formatted(Formatting.RED)), true);
                }
            }
        });
    }
}
