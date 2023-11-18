package fr.skhr.loyto.UnderUtilities.common;

import java.awt.image.BufferedImage;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.skhr.loyto.UnderUtilities.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = "UnderUtilities", name = "UnderUtilities", version = "1.0.0")
public class UnderUtilities {
	public static final String MODID = "UnderUtilities";
	
	@SidedProxy(clientSide = "fr.skhr.loyto.UnderUtilities.proxy.ClientProxy", serverSide = "fr.skhr.loyto.UnderUtilities.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("modtutoriel")
    public static UnderUtilities instance;
	
	//Default tab header-footer
	public static String header = "Voici le Header";
	public static String header1 = "Header : <size>";
	public static String footer = "Voici le Footer";
	public static String footer1 = "Voici la deuxième ligne du Footer";
	public static BufferedImage test = null;
	
	//Network
	public static SimpleNetworkWrapper network;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		//Configuration
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
		    cfg.load();
		}
		catch(Exception ex)
		{
		    System.out.println("[SKHR-Tab] Impossible de charger le fichier de configuration");
		}
		finally
		{
		    if(cfg.hasChanged())
		    {
		        cfg.save();
		    }
		}

		header = cfg.get("tab" , "header", header).getString();
		header1 = cfg.get("tab" , "header1", header1).getString();
		footer = cfg.get("tab" , "footer", footer).getString();
		footer1 = cfg.get("tab" , "footer1", footer1).getString();
		cfg.save();
		
		//Network
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		network.registerMessage(serverMessage.Handler.class, serverMessage.class, 0, Side.CLIENT);
		
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerRender();
    	
    	FMLCommonHandler.instance().bus().register(new UnderUtilitiesEventHandler());
        MinecraftForge.EVENT_BUS.register(new UnderUtilitiesEventHandler());
    }
    	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
}
