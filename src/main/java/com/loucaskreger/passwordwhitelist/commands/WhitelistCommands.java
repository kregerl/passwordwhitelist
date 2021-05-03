package com.loucaskreger.passwordwhitelist.commands;

import com.loucaskreger.passwordwhitelist.util.PasswordManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class WhitelistCommands {

	public static class setPasswordCommand {
		public static void register(CommandDispatcher<CommandSource> dispatcher) {
			dispatcher.register(Commands.literal("pwhitelist").requires((source) -> source.hasPermission(3))
					.then(Commands.argument("password", StringArgumentType.word()).executes((context) -> {
						System.out.println(
								PasswordManager.INSTANCE.createHash(context.getArgument("password", String.class)));
						return 1;
					})));

		}
	}

	public static class viewPasswordCommand {
		public static void register(CommandDispatcher<CommandSource> dispatcher) {
			dispatcher.register(Commands.literal("pwhitelist").requires((source) -> source.hasPermission(4))
					.then(Commands.literal("test").executes((context) -> {
						System.out.println(PasswordManager.INSTANCE.matches("hello"));
						return 1;
					})));

		}
	}
}
