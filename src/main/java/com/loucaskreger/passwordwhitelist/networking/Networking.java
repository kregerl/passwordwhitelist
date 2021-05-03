package com.loucaskreger.passwordwhitelist.networking;

import com.loucaskreger.passwordwhitelist.PasswordWhitelist;
import com.loucaskreger.passwordwhitelist.networking.packet.AskForPasswordPacketRequest;
import com.loucaskreger.passwordwhitelist.networking.packet.AskForPasswordPacketResponse;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

	public static SimpleChannel INSTANCE;
	private static int id = 0;

	public static int nextId() {
		return id++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(PasswordWhitelist.MOD_ID, "whitelist"),
				() -> "1.0", s -> true, s -> true);
		INSTANCE.registerMessage(nextId(), AskForPasswordPacketRequest.class, AskForPasswordPacketRequest::toBytes,
				AskForPasswordPacketRequest::new, AskForPasswordPacketRequest::handle);

		INSTANCE.registerMessage(nextId(), AskForPasswordPacketResponse.class, AskForPasswordPacketResponse::toBytes,
				AskForPasswordPacketResponse::new, AskForPasswordPacketResponse::handle);
	}

}
