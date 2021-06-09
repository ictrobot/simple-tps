package me.ethanjones.simpletps.mixin;

import me.ethanjones.simpletps.SimpleTPS;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	private RegistryKey<World> key;
	private long startTime;

	@Inject(at = @At("HEAD"), method = "tick(Ljava/util/function/BooleanSupplier;)V")
	private void tickStart(CallbackInfo info) {
		key = getRegistryKey();
		startTime = System.nanoTime();
	}

	@Inject(at = @At("RETURN"), method = "tick(Ljava/util/function/BooleanSupplier;)V")
	private void tickEnd(CallbackInfo info) {
		if (key != getRegistryKey()) throw new RuntimeException("Simple-TPS: No world tickStart?");
		SimpleTPS.tickTime(key.getValue().toString(), (System.nanoTime() - startTime) / 1_000_000d);
	}
}
