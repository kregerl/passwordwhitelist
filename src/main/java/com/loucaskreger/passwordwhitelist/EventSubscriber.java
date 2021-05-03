package com.loucaskreger.passwordwhitelist;

import java.util.Arrays;

import com.loucaskreger.passwordwhitelist.commands.WhitelistCommands;
import com.loucaskreger.passwordwhitelist.networking.Networking;
import com.loucaskreger.passwordwhitelist.networking.packet.AskForPasswordPacketRequest;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = PasswordWhitelist.MOD_ID, value = Dist.DEDICATED_SERVER)
public class EventSubscriber {

	@SubscribeEvent
	public static void onCommandRun(final CommandEvent event) {
		String[] command = event.getParseResults().getReader().getString().split(" ");
		if (command.length >= 2) {
			if (command[0].substring(1).equals("whitelist") && !command[1].equals("on")) {
				event.setCanceled(true);
				StringBuilder builder = new StringBuilder(stringsToString(command));
				builder.insert(1, 'p');
				System.out.println(command[0]);
				ServerLifecycleHooks.getCurrentServer().getCommands()
						.performCommand(event.getParseResults().getContext().getSource(), command[0] + " hello");
			}
		}
	}

	private static String stringsToString(String[] strings) {
		String result = "";
		for (String string : strings) {
			string += " ";
			result += string;
		}
		return result;
	}

	@SubscribeEvent
	public static void onClientJoined(final PlayerEvent.PlayerLoggedInEvent event) {
		System.out.println("Here1");
		Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
				new AskForPasswordPacketRequest());
	}

	@SubscribeEvent
	public static void onServerStart(final FMLServerStartingEvent event) {
		CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommands().getDispatcher();
		WhitelistCommands.setPasswordCommand.register(dispatcher);
		WhitelistCommands.viewPasswordCommand.register(dispatcher);
	}

}
