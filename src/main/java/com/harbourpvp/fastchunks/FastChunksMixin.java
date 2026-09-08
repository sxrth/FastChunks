package com.harbourpvp.fastchunks;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class FastChunksMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void fastchunks$clientTick(CallbackInfo ci) {
        FastChunksClient.tick((Minecraft) (Object) this);
    }
}
