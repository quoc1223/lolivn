package com.example.chunkkey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ChunkKeyClient implements ClientModInitializer {
    // Biến dùng để chặn việc giữ nút bị nhảy số quá nhanh (chống spam nút)
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;

    @Override
    public void onInitializeClient() {
        // Lắng nghe sự kiện tick của game (chạy liên tục 20 lần/giây)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.options == null) return;

            long window = MinecraftClient.getInstance().getWindow().getHandle();

            // 1. Kiểm tra trực tiếp phím MŨI TÊN LÊN (GLFW_KEY_UP) từ bàn phím phần cứng
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                if (!isUpPressed) { // Chỉ xử lý 1 lần duy nhất khi vừa bấm xuống
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
                isUpPressed = false; // Thả nút ra thì reset
            }

            // 2. Kiểm tra trực tiếp phím MŨI TÊN XUỐNG (GLFW_KEY_DOWN) từ bàn phím phần cứng
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
                if (!isDownPressed) { // Chỉ xử lý 1 lần duy nhất khi vừa bấm xuống
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
                isDownPressed = false; // Thả nút ra thì reset
            }
        });
    }
}
