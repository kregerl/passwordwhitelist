package com.loucaskreger.passwordwhitelist.networking.packet;

import java.util.function.Supplier;

import com.loucaskreger.passwordwhitelist.util.PasswordManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class AskForPasswordPacketResponse {

	private ITextComponent password;

	public AskForPasswordPacketResponse(PacketBuffer buffer) {
		this.password = buffer.readComponent();
	}

	public AskForPasswordPacketResponse(ITextComponent password) {
		this.password = password;
	}

	public void toBytes(PacketBuffer buffer) {
		buffer.writeComponent(this.password);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> this.processRequest(context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	public void processRequest(ServerPlayerEntity player) {
		if (!PasswordManager.INSTANCE.matches(this.password.getContents())) {
			player.connection.disconnect(new StringTextComponent("Wrong password"));
		} else if (PasswordManager.INSTANCE.matches((this.password.getContents()))) {
			
		}

	}
}
