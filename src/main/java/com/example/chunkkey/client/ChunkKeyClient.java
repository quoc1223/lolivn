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
        // Đăng ký phím tắt theo chuẩn Fabric API mới
        keyIncrease = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chunkkey.increase", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_UP, 
            "category.chunkkey"
        ));

        keyDecrease = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chunkkey.decrease", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_DOWN, 
            "category.chunkkey"
        ));

        // Lắng nghe sự kiện click từ máy (Client)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.options == null) return;

            while (keyIncrease.wasPressed()) {
                int current = client.options.getViewDistance().getValue();
                if (current < 32) {
                    client.options.getViewDistance().setValue(current + 1);
                    client.options.write();
                    
                    // Gửi tin nhắn dạng Overlay (hiện ngay trên thanh máu) để không làm rác khung chat
                    client.player.sendMessage(Text.literal("Render Distance: ")
                        .append(Text.literal(String.valueOf(current + 1)).formatted(Formatting.GREEN)), true);
                }
            }

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
