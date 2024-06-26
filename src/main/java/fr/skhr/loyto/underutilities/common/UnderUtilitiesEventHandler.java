package fr.skhr.loyto.UnderUtilities.common;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import fr.skhr.loyto.UnderUtilities.proxy.ClientProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class UnderUtilitiesEventHandler {
	//Cote serveur
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		String msg = "conf[tab|header:" + serverMessage.encode(UnderUtilities.header) + ";header1:" + serverMessage.encode(UnderUtilities.header1) + ";footer:" + serverMessage.encode(UnderUtilities.footer) + ";footer1:" + serverMessage.encode(UnderUtilities.footer1);
		
		UnderUtilities.network.sendTo(new serverMessage(msg), (EntityPlayerMP) event.player);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		String msg = "cache[remove:" + serverMessage.encode(event.player.getDisplayName());
		UnderUtilities.network.sendToAll(new serverMessage(msg));
	}
	
	//Cote client
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
			ClientProxy.tabRender(event);
		}
	}
	
	@SubscribeEvent
	public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) {
		ClientProxy.dlHeadCache = new HashMap<String, BufferedImage>();
		ClientProxy.headCache = new HashMap<String, BufferedImage>();

		ClientProxy.dlSkinCache = new HashMap<String, BufferedImage>();
		ClientProxy.skinCache = new HashMap<String, ResourceLocation>();
	}
}
