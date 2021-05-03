package com.loucaskreger.passwordwhitelist.client;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import com.loucaskreger.passwordwhitelist.PasswordWhitelist;
import com.loucaskreger.passwordwhitelist.client.gui.screen.PasswordScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PasswordWhitelist.MOD_ID, value = Dist.CLIENT)
public class ClientEventSubscriber {

	public static final KeyBinding open = new KeyBinding(PasswordWhitelist.MOD_ID + ".key.pickTool", GLFW_KEY_P,
			PasswordWhitelist.MOD_ID + ".key.categories");

	@SubscribeEvent
	public static void onClientTick(final ClientTickEvent event) {
		if (open.isDown()) {
			PasswordScreen.open();
		}
	}

}
