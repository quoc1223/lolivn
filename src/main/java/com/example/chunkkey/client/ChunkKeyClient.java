package com.example.chunkkey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ChunkKeyClient implements ClientModInitializer {
    // Chống giữ nút bị nhảy số quá nhanh
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;

    @Override
    public void onInitializeClient() {
        // Lắng nghe sự kiện tick của game (chạy liên tục 20 lần/giây)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.options == null) return;

            long window = MinecraftClient.getInstance().getWindow().getHandle();

            // 1. Kiểm tra trực tiếp phím MŨI TÊN LÊN (GLFW_KEY_UP)
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                if (!isUpPressed) {
                    isUpPressed = true;
                    int current = client.options.getViewDistance().getValue();
                    if (current < 32) {
                        client.options.getViewDistance().setValue(current + 1);
                        client.options.write();
                        client.player.sendMessage(Text.literal("Render Distance: ")
                            .append(Text.literal(String.valueOf(current + 1)).formatted(Formatting.GREEN)), true);
                    }
                }
            } else {
                isUpPressed = false;
            }

            // 2. Kiểm tra trực tiếp phím MŨI TÊN XUỐNG (GLFW_KEY_DOWN)
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
                if (!isDownPressed) {
                    isDownPressed = true;
                    int current = client.options.getViewDistance().getValue();
                    if (current > 2) {
                        client.options.getViewDistance().setValue(current - 1);
                        client.options.write();
                        client.player.sendMessage(Text.literal("Render Distance: ")
                            .append(Text.literal(String.valueOf(current - 1)).formatted(Formatting.RED)), true);
                    }
                }
            } else {
                isDownPressed = false;
            }
        });
    }
}
