package com.loucaskreger.passwordwhitelist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.loucaskreger.passwordwhitelist.client.ClientEventSubscriber;
import com.loucaskreger.passwordwhitelist.networking.Networking;
import com.loucaskreger.passwordwhitelist.util.PasswordManager;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PasswordWhitelist.MOD_ID)
public class PasswordWhitelist {
	public static final String MOD_ID = "passwordwhitelist";
	public static final Logger LOGGER = LogManager.getLogger();

	public PasswordWhitelist() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setupCommon);
		bus.addListener(this::setupClient);
		Networking.registerMessages();
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
		PasswordManager.init();
	}

	private void setupClient(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(ClientEventSubscriber.open);
	}

}
