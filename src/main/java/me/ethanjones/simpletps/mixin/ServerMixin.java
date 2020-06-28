package me.ethanjones.simpletps.mixin;

import me.ethanjones.simpletps.SimpleTPS;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerMixin {
	private long startTime;

	@Inject(at = @At("HEAD"), method = "tick(Ljava/util/function/BooleanSupplier;)V")
	private void tickStart(CallbackInfo info) {
		startTime = System.nanoTime();
	}

	@Inject(at = @At("RETURN"), method = "tick(Ljava/util/function/BooleanSupplier;)V")
	private void tickEnd(CallbackInfo info) {
		SimpleTPS.tickTime(null, (System.nanoTime() - startTime) / 1_000_000d);
	}

}
