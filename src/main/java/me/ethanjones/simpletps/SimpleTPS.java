package me.ethanjones.simpletps;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleTPS implements ModInitializer {
	private static final TPSHistory serverHistory = new TPSHistory();
	private static final Map<String, TPSHistory> histories = new LinkedHashMap<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) ->
						dispatcher.register(CommandManager.literal("tps").executes(SimpleTPS::tpsCommand))));
	}

	private static int tpsCommand(CommandContext<ServerCommandSource> context) {
		String summary = String.format("Server   %6.2f TPS   %6.2fms", serverHistory.getTPS(), serverHistory.getMsPerTick());
		context.getSource().sendFeedback(new LiteralText(summary), false);

		for (Map.Entry<String, TPSHistory> entry : histories.entrySet()) {
			String s = String.format("%s   %.2fms", entry.getKey(), entry.getValue().getMsPerTick());
			context.getSource().sendFeedback(new LiteralText(s), false);
		}
		return 0;
	}

	public static void tickTime(String id, double ms) {
		(id == null ? serverHistory : histories.computeIfAbsent(id, k -> new TPSHistory())).update(ms);
	}
}
