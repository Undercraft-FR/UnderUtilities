package fr.skhr.loyto.UnderUtilities.proxy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.skhr.loyto.UnderUtilities.client.UnderRenderPlayer;
import fr.skhr.loyto.UnderUtilities.common.UnderUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ClientProxy extends CommonProxy {

	//Cache skin
	public static HashMap<String, BufferedImage> dlSkinCache = new HashMap<String, BufferedImage>();
	public static HashMap<String, ResourceLocation> skinCache = new HashMap<String, ResourceLocation>();
	
	//Cache head
	public static HashMap<String, BufferedImage> dlHeadCache = new HashMap();
	public static HashMap<String, BufferedImage> headCache = new HashMap();
	
	public void registerRender()
    {
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new UnderRenderPlayer());
    }
	
	public static ResourceLocation getSkinLocation(String name) {
		
		if(!skinCache.containsKey(name)) {
			if(!dlSkinCache.containsKey(name)) {
				(new Thread(new SkinDownloader(name, dlSkinCache, skinCache))).start();
			}
			else {
				if(dlSkinCache.get(name) != null) {
					skinCache.put(name, Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("", new DynamicTexture(dlSkinCache.get(name))));
					dlSkinCache.remove(name);
				}
			}
			
			return new ResourceLocation("textures/entity/steve.png");
		}
		
		return skinCache.get(name);
	}
	
	public static void tabRender(RenderGameOverlayEvent.Pre event) {
		String header = UnderUtilities.header;
		String footer = UnderUtilities.footer;
		//On annule l'affichage du tab
		event.setCanceled(true);
		
		//Liste des joueurs triés par grade
		final Minecraft mc = Minecraft.getMinecraft();
		NetHandlerPlayClient handler = mc.thePlayer.sendQueue;
		List<GuiPlayerInfo> players = handler.playerInfoList;

		List<GuiPlayerInfo> fondaList = new ArrayList();
		List<GuiPlayerInfo> coFondaList = new ArrayList();
		List<GuiPlayerInfo> respAdminList = new ArrayList();
		List<GuiPlayerInfo> respCommList = new ArrayList();
		List<GuiPlayerInfo> adminList = new ArrayList();
		List<GuiPlayerInfo> superModoList = new ArrayList();
		List<GuiPlayerInfo> superModoFormaList = new ArrayList();
		List<GuiPlayerInfo> devList = new ArrayList();
		List<GuiPlayerInfo> modoPlusList = new ArrayList();
		List<GuiPlayerInfo> modoList = new ArrayList();
		List<GuiPlayerInfo> modoTestList = new ArrayList();
		List<GuiPlayerInfo> assistantList = new ArrayList();
		List<GuiPlayerInfo> youtubeList = new ArrayList();
		List<GuiPlayerInfo> streamerList = new ArrayList();
		List<GuiPlayerInfo> ultimeList = new ArrayList();
		List<GuiPlayerInfo> platiniumList = new ArrayList();
		List<GuiPlayerInfo> cavalierList = new ArrayList();
		List<GuiPlayerInfo> otherList = new ArrayList();
		List<GuiPlayerInfo> joueurList = new ArrayList();

		List<GuiPlayerInfo> joueursList = new ArrayList();
		
		for(GuiPlayerInfo player : players) {
			ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(player.name);
			String name = ScorePlayerTeam.formatPlayerName((Team)team, player.name);
			
			if(name.matches(".*❖.*")) {
				fondaList.add(player);
			}
			else if(name.matches(".*✯.*")) {
				coFondaList.add(player);
			}
			else if(name.matches(".*\\[RespAdmin\\].*")) {
				respAdminList.add(player);
			}
			else if(name.matches(".*\\[RespComm\\].*")) {
				respCommList.add(player);
			}
			else if(name.matches(".*\\[Admin\\].*")) {
				adminList.add(player);
			}
			else if(name.matches(".*\\[SuperModo.*Forma.*\\].*")) {
				superModoList.add(player);
			}
			else if(name.matches(".*\\[SuperModo\\].*")) {
				superModoList.add(player);
			}
			else if(name.matches(".*\\[Dev\\].*")) {
				devList.add(player);
			}
			else if(name.matches(".*\\[Modo§4\\+.*\\].*")) {
				modoPlusList.add(player);
			}
			else if(name.matches(".*\\[Modo\\].*")) {
				modoList.add(player);
			}
			else if(name.matches(".*\\[Modo.*T.*\\].*")) {
				modoTestList.add(player);
			}
			else if(name.matches(".*\\[.*Assistant.*\\].*")) {
				assistantList.add(player);
			}
			else if(name.matches(".*\\[Youtubeur\\].*")) {
				youtubeList.add(player);
			}
			else if(name.matches(".*\\[Streamer\\].*")) {
				streamerList.add(player);
			}
			else if(name.matches(".*\\[Ultime\\].*")) {
				ultimeList.add(player);
			}
			else if(name.matches(".*\\[Platinium\\].*")) {
				platiniumList.add(player);
			}
			else if(name.matches(".*\\[Cavalier\\].*")) {
				cavalierList.add(player);
			}
			else if(name.matches(".*\\[Joueur\\].*")) {
				joueurList.add(player);
			}
			else {
				otherList.add(player);
			}
		}

		joueursList.addAll(fondaList);
		joueursList.addAll(coFondaList);
		joueursList.addAll(respAdminList);
		joueursList.addAll(respCommList);
		joueursList.addAll(adminList);
		joueursList.addAll(superModoList);
		joueursList.addAll(superModoFormaList);
		joueursList.addAll(devList);
		joueursList.addAll(modoPlusList);
		joueursList.addAll(modoList);
		joueursList.addAll(modoTestList);
		joueursList.addAll(assistantList);
		joueursList.addAll(youtubeList);
		joueursList.addAll(streamerList);
		joueursList.addAll(ultimeList);
		joueursList.addAll(platiniumList);
		joueursList.addAll(cavalierList);
		joueursList.addAll(otherList);
		joueursList.addAll(joueurList);
		
		joueursList = joueursList.subList(0, Math.min(joueursList.size(), 100));

		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();
		
		int columnWidth = 140;
		int slotHeight = 10;
		
		//fond
		//-calcule
		//--Nombre de slot
		int slots = 20;
		if(joueursList.size() < 20) { slots = joueursList.size(); }
		else if(joueursList.size() > 80) { slots = 25; }
		
		//--Nombre de colonne
		int columns = joueursList.size() / slots; 
		if(joueursList.size()%slots > 0) { columns++; }
		
		if(columns == 1) { columnWidth = (int) (columnWidth*1.5); }
		int fondWidth = columns * (columnWidth+1) + 1;
		
		//--Taille des colonnes en fonction de la taille de l'écran
		while(fondWidth > width & columnWidth > 90){
			columnWidth -= 10;
			fondWidth = columns * (columnWidth+1)+1;
		}
		
		int fondHeight = 30 + (slotHeight+1) * slots + 1;
		
		//-rendu
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3042);
		

		int headerWidth = mc.fontRenderer.getStringWidth(header);
		int footerWidth = mc.fontRenderer.getStringWidth(footer);
		
		int start = width/2 - fondWidth/2;
		int bgWidth = Math.max(fondWidth, Math.max(headerWidth, footerWidth) + 4);
		int hStart = width/2 - bgWidth/2;
		GuiIngame.drawRect(hStart, 2, hStart + bgWidth, fondHeight+2, -2147483648);
		
		//Header
		mc.fontRenderer.drawString(header, width/2 - headerWidth/2, 6, Color.WHITE.getRGB());
		
		//Footer
		mc.fontRenderer.drawString(footer, width/2 - footerWidth/2, fondHeight-15+6, Color.WHITE.getRGB());
		
		//slot
		int startSlotX = start;
		int startSlotY = 17;
		
		int x;
		int y;
		
		int p = 0;
		
		for(int i = 0; i < columns ; i++) {
			for(int k = 0; k < slots ; k++) {
				x = startSlotX + i * (columnWidth+1) + 1;
				y = startSlotY + k * (slotHeight+1) + 1;
				ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(joueursList.get(p).name);
    			String name = ScorePlayerTeam.formatPlayerName((Team)team, joueursList.get(p).name);
    			
				GuiIngame.drawRect(x, y, x + columnWidth, y + slotHeight, (new Color(158, 152, 152, 100)).getRGB());
				mc.fontRenderer.drawString(name, x + 11 , y + 1, Color.WHITE.getRGB());
				
				GuiPlayerInfo player = joueursList.get(p);
				
				//Affichage ping
    			int ms = player.responseTime;
				
				int ping = 4;
				if(ms < 0) {
					ping = 5;
				}
				else if(ms < 150) {
					ping = 0;
				}
				else if(ms < 300) {
					ping = 1;
				}
				else if(ms < 600) {
					ping = 2;
				}
				else if(ms < 1000) {
					ping = 3;
				}
				
	    		mc.getTextureManager().bindTexture(Gui.icons);
	    		GuiIngame ingameGui = new GuiIngame(mc);
	    		ingameGui.drawTexturedModalRect(x + columnWidth - 11, y + 1, 0, 176 + ping * 8, 10, 8);
				
	    		//Afichage tete
	    		String nameHead = joueursList.get(p).name;
	    		if(nameHead.contains(" ")) {
	    			nameHead = nameHead.split(" ")[1];
	    		}
	    		BufferedImage headImage = null;

				if(!headCache.containsKey(nameHead) && !dlHeadCache.containsKey(nameHead)) {
					(new Thread(new HeadDownloader(nameHead, dlHeadCache, headCache))).start();
				}
				
				if(headCache.containsKey(nameHead)) {
					GL11.glBindTexture(3553, (new DynamicTexture(headCache.get(nameHead))).getGlTextureId());
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
				else {
					ResourceLocation head = AbstractClientPlayer.getLocationSkin(joueursList.get(p).name);
    	    		AbstractClientPlayer.getDownloadImageSkin(head, joueursList.get(p).name);
    	    		
    	    		mc.getTextureManager().bindTexture(head);
				}
				ingameGui.func_146110_a(x + 1, y + 1, 0, 0, 8, 8, 8, 8);
				
				p++;
			}
		}
	}
}

class SkinDownloader implements Runnable {
	HashMap<String, BufferedImage> dlSkinCache;
	HashMap<String, ResourceLocation> skinCache;
	String name;
	
	public SkinDownloader(String name, HashMap<String, BufferedImage> dlSkinCache, HashMap<String, ResourceLocation> skinCache) {
		this.name = name;
		this.dlSkinCache = dlSkinCache;
		this.skinCache = skinCache;
	}
	
	public void run() {
		dlSkinCache.put(name, null);
		BufferedImage dlSkin = null;
		try {
			URL url = new URL("https://www.undercraft.fr/api/skin-api/skins/%player%".replace("%player%", name));
			dlSkin = ImageIO.read(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage skin = new BufferedImage(64, 32, 2);
		BufferedImage partSkin = dlSkin.getSubimage(0, 0, 64, 32);
		Graphics g = skin.getGraphics();
		g.drawImage(partSkin, 0, 0, null);

		dlSkinCache.replace(name, skin);
	}
}

class HeadDownloader implements Runnable {
	HashMap<String, BufferedImage> dlHeadCache;
	HashMap<String, BufferedImage> headCache;
	String name;
	
	public HeadDownloader(String name, HashMap<String, BufferedImage> dlHeadCache, HashMap<String, BufferedImage> headCache) {
		this.name = name;
		this.dlHeadCache = dlHeadCache;
		this.headCache = headCache;
	}
	
	public void run() {
		dlHeadCache.put(name, null);
		BufferedImage headImage = null;
		try {
			URL url = new URL("https://www.undercraft.fr/api/skin-api/avatars/face/%player%".replace("%player%", name));
			headImage = ImageIO.read(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage merged = new BufferedImage(64, 64, 2);
		BufferedImage face = headImage.getSubimage(0, 0, 64, 64);
		Graphics g = merged.getGraphics();
		g.drawImage(face, 0, 0, null);
		
		headCache.put(name, merged);
		dlHeadCache.remove(name);
	}
}
