package com.loucaskreger.passwordwhitelist.client.gui.screen;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

import java.util.function.Consumer;

import com.loucaskreger.passwordwhitelist.networking.Networking;
import com.loucaskreger.passwordwhitelist.networking.packet.AskForPasswordPacketResponse;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class PasswordScreen extends Screen {

	private static final Minecraft mc = Minecraft.getInstance();

	private static final int WIDTH = 100;
	private static final int HEIGHT = 10;

	private TextFieldWidget passwordField;

	public PasswordScreen() {
		super(new StringTextComponent("Password Screen"));
		this.width = mc.getWindow().getGuiScaledWidth();
		this.height = mc.getWindow().getGuiScaledHeight();
		this.passwordField = new TextFieldWidget(mc.font, (this.width / 2) - (WIDTH / 2),
				(this.height / 2) - (HEIGHT / 2), WIDTH, HEIGHT, new StringTextComponent(""));
		this.passwordField.setResponder(text -> passwordField.setMessage(new StringTextComponent(text)));
	}

	public static void open() {
		System.out.println("Here");
		mc.forceSetScreen(new PasswordScreen());
	}

	@Override
	protected void init() {
		super.init();
		this.addWidget(this.passwordField);
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.passwordField.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean res = super.keyPressed(keyCode, scanCode, modifiers);
		if (keyCode == GLFW_KEY_ENTER) {
			Networking.INSTANCE
					.sendToServer(new AskForPasswordPacketResponse(PasswordScreen.this.passwordField.getMessage()));
			System.out.println(this.passwordField.getMessage());
			res = true;
		}
		return res;
	}

	@Override
	public void onClose() {
		super.onClose();
		Networking.INSTANCE.sendToServer(new AskForPasswordPacketResponse(this.passwordField.getMessage()));
	}

}
