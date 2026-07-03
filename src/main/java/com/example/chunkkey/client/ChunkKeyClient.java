package com.example.chunkkey.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChunkKeyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // ĐĂNG KÝ LỆNH /ck [số từ 2 đến 32]
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ck")
                .then(ClientCommandManager.argument("chunks", IntegerArgumentType.integer(2, 32))
                .executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null && client.options != null) {
                        int targetChunks = IntegerArgumentType.getInteger(context, "chunks");
                        
                        client.options.getViewDistance().setValue(targetChunks);
                        client.options.write();
                        
                        context.getSource().sendFeedback(Text.literal("Render Distance changed to: ")
                            .append(Text.literal(String.valueOf(targetChunks)).formatted(Formatting.GREEN)));
                    }
                    return 1;
                })));
        });
    }
}
