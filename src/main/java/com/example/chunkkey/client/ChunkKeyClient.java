package com.example.chunkkey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChunkKeyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // ĐĂNG KÝ DUY NHẤT LỆNH /ck KHÔNG KÈM THAM SỐ
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ck").executes(context -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.options != null) {
                    int current = client.options.getViewDistance().getValue();
                    
                    // Nếu đang từ 18 chunk trở lên thì hạ xuống 2, ngược lại thì bật lên 32
                    int target = (current >= 18) ? 2 : 32; 
                    
                    client.options.getViewDistance().setValue(target);
                    client.options.write();
                    
                    // Đổi màu thông báo: 32 = Xanh lá, 2 = Đỏ
                    Formatting color = (target == 32) ? Formatting.GREEN : Formatting.RED;
                    context.getSource().sendFeedback(Text.literal("Render Distance: ")
                        .append(Text.literal(String.valueOf(target)).formatted(color)));
                }
                return 1;
            }));
        });
    }
}
