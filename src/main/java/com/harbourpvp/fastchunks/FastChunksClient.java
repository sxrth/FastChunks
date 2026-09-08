package com.harbourpvp.fastchunks;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public final class FastChunksClient implements ClientModInitializer {
    public static final String MOD_ID = "fastchunks";

    private static Config config;
    private static int ticks;
    private static int cooldown;

    @Override
    public void onInitializeClient() {
        config = Config.load(FabricLoader.getInstance().getConfigDir().resolve("fastchunks.json"));
    }

    public static void tick(Minecraft client) {
        if (config == null || !config.enabled || client.player == null) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (++ticks < config.evaluationTicks) return;
        ticks = 0;

        int fps = client.getFps();
        int current = client.options.renderDistance().get();

        if (fps < config.targetFps - config.lowFpsMargin) {
            int next = Math.max(config.minimumDistance, current - config.step);
            if (next != current) {
                client.options.renderDistance().set(next);
                cooldown = config.cooldownTicks;
            }
        } else if (fps > config.targetFps + config.highFpsMargin) {
            int next = Math.min(config.maximumDistance, current + config.step);
            if (next != current) {
                client.options.renderDistance().set(next);
                cooldown = config.cooldownTicks;
            }
        }
    }

    public static final class Config {
        boolean enabled = true;
        int targetFps = 120;
        int minimumDistance = 6;
        int maximumDistance = 24;
        int step = 1;
        int evaluationTicks = 40;
        int cooldownTicks = 100;
        int lowFpsMargin = 15;
        int highFpsMargin = 30;

        static Config load(Path path) {
            Config c = new Config();
            try {
                if (Files.exists(path)) {
                    String s = Files.readString(path, StandardCharsets.UTF_8);
                    c.enabled = bool(s, "enabled", c.enabled);
                    c.targetFps = integer(s, "targetFps", c.targetFps);
                    c.minimumDistance = integer(s, "minimumDistance", c.minimumDistance);
                    c.maximumDistance = integer(s, "maximumDistance", c.maximumDistance);
                    c.step = integer(s, "step", c.step);
                    c.evaluationTicks = integer(s, "evaluationTicks", c.evaluationTicks);
                    c.cooldownTicks = integer(s, "cooldownTicks", c.cooldownTicks);
                    c.lowFpsMargin = integer(s, "lowFpsMargin", c.lowFpsMargin);
                    c.highFpsMargin = integer(s, "highFpsMargin", c.highFpsMargin);
                } else {
                    c.save(path);
                }
            } catch (Exception ignored) {
                c.save(path);
            }
            c.normalize();
            return c;
        }

        void normalize() {
            targetFps = clamp(targetFps, 30, 500);
            minimumDistance = clamp(minimumDistance, 2, 32);
            maximumDistance = clamp(maximumDistance, minimumDistance, 64);
            step = clamp(step, 1, 8);
            evaluationTicks = clamp(evaluationTicks, 20, 200);
            cooldownTicks = clamp(cooldownTicks, 20, 400);
            lowFpsMargin = clamp(lowFpsMargin, 1, 60);
            highFpsMargin = clamp(highFpsMargin, 1, 100);
        }

        void save(Path path) {
            try {
                Files.createDirectories(path.getParent());
                String json = """
                {
                  "enabled": true,
                  "targetFps": 120,
                  "minimumDistance": 6,
                  "maximumDistance": 24,
                  "step": 1,
                  "evaluationTicks": 40,
                  "cooldownTicks": 100,
                  "lowFpsMargin": 15,
                  "highFpsMargin": 30
                }
                """;
                Files.writeString(path, json, StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
        }

        static boolean bool(String s, String key, boolean fallback) {
            String v = value(s, key);
            return v == null ? fallback : Boolean.parseBoolean(v);
        }

        static int integer(String s, String key, int fallback) {
            try {
                String v = value(s, key);
                return v == null ? fallback : Integer.parseInt(v);
            } catch (NumberFormatException e) {
                return fallback;
            }
        }

        static String value(String s, String key) {
            String needle = "\"" + key + "\"";
            int i = s.indexOf(needle);
            if (i < 0) return null;
            int colon = s.indexOf(':', i + needle.length());
            if (colon < 0) return null;
            int end = s.indexOf(',', colon + 1);
            if (end < 0) end = s.indexOf('}', colon + 1);
            if (end < 0) return null;
            return s.substring(colon + 1, end).trim().replace("\"", "");
        }

        static int clamp(int value, int min, int max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
