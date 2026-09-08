package com.harbourpvp.fastchunks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public final class FastChunksClient implements ClientModInitializer {
    private static int timer;

    @Override
    public void onInitializeClient() {
        Config.load();
        ClientTickEvents.END_CLIENT_TICK.register(FastChunksClient::tick);
    }

    private static void tick(MinecraftClient client) {
        if (!Config.enabled || client.player == null || client.options == null) return;
        if (++timer < Config.evaluationTicks) return;
        timer = 0;

        int fps = client.getCurrentFps();
        if (fps < Config.targetFps - 15) {
            changeDistance(client.options.getViewDistance(), -Config.step);
        } else if (fps > Config.targetFps + 25) {
            changeDistance(client.options.getViewDistance(), Config.step);
        }
    }

    private static void changeDistance(SimpleOption<Integer> option, int delta) {
        int current = option.getValue();
        int next = Math.max(Config.minimumDistance,
                Math.min(Config.maximumDistance, current + delta));
        if (next != current) option.setValue(next);
    }
}
