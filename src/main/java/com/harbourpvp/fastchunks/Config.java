package com.harbourpvp.fastchunks;

import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

final class Config {
    static boolean enabled = true;
    static int targetFps = 120;
    static int minimumDistance = 6;
    static int maximumDistance = 24;
    static int step = 2;
    static int evaluationTicks = 40;

    private static final Path FILE =
        FabricLoader.getInstance().getConfigDir().resolve("fastchunks.json");

    private Config() {}

    static void load() {
        try {
            if (!Files.exists(FILE)) {
                save();
                return;
            }
            String json = Files.readString(FILE, StandardCharsets.UTF_8);
            Values v = new GsonBuilder().create().fromJson(json, Values.class);
            if (v != null) {
                enabled = v.enabled;
                targetFps = v.targetFps;
                minimumDistance = v.minimumDistance;
                maximumDistance = v.maximumDistance;
                step = v.step;
                evaluationTicks = v.evaluationTicks;
            }
        } catch (Exception ignored) {
            save();
        }
        normalize();
    }

    private static void normalize() {
        targetFps = Math.max(30, Math.min(500, targetFps));
        minimumDistance = Math.max(2, Math.min(32, minimumDistance));
        maximumDistance = Math.max(minimumDistance, Math.min(64, maximumDistance));
        step = Math.max(1, Math.min(8, step));
        evaluationTicks = Math.max(20, Math.min(200, evaluationTicks));
    }

    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Values v = new Values();
            v.enabled = enabled;
            v.targetFps = targetFps;
            v.minimumDistance = minimumDistance;
            v.maximumDistance = maximumDistance;
            v.step = step;
            v.evaluationTicks = evaluationTicks;
            Files.writeString(FILE,
                new GsonBuilder().setPrettyPrinting().create().toJson(v),
                StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
    }

    private static final class Values {
        boolean enabled = true;
        int targetFps = 120;
        int minimumDistance = 6;
        int maximumDistance = 24;
        int step = 2;
        int evaluationTicks = 40;
    }
}
