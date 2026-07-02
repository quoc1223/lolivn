package com.example.chunkkey.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChunkKeyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ck")
                // Lệnh gốc: /ck (Hiển thị Render Distance hiện tại)
                .executes(context -> {
                    var client = context.getSource().getClient();
                    int currentDistance = client.options.getViewDistance().getValue();
                    
                    context.getSource().sendFeedback(Text.literal("Render Distance hiện tại: ")
                        .append(Text.literal(String.valueOf(currentDistance)).formatted(Formatting.GREEN))
                        .append(" chunks."));
                    return 1;
                })
                // Lệnh con: /ck <number> (Thay đổi Render Distance)
                .then(ClientCommandManager.argument("distance", IntegerArgumentType.integer(2, 32))
                    .executes(context -> {
                        var client = context.getSource().getClient();
                        int newDistance = IntegerArgumentType.getInteger(context, "distance");
                        
                        // Thay đổi giá trị trong Game Options
                        client.options.getViewDistance().setValue(newDistance);
                        // Lưu lại cấu hình vào file options.txt
                        client.options.save();
                        
                        context.getSource().sendFeedback(Text.literal("Đã đặt Render Distance thành: ")
                            .append(Text.literal(String.valueOf(newDistance)).formatted(Formatting.GREEN))
                            .append(" chunks."));
                        return 1;
                    })
                )
            );
        });
    }
}
