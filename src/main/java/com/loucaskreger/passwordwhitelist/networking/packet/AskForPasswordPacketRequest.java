package com.loucaskreger.passwordwhitelist.networking.packet;

import java.util.function.Supplier;
import com.loucaskreger.passwordwhitelist.client.gui.screen.PasswordScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AskForPasswordPacketRequest {

	public AskForPasswordPacketRequest(PacketBuffer buffer) {

	}

	public AskForPasswordPacketRequest() {

	}

	public void toBytes(PacketBuffer buffer) {

	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(this::processResponse);
		context.get().setPacketHandled(true);
	}

	public void processResponse() {
		System.out.println("Here");
		PasswordScreen.open();
	}
}
