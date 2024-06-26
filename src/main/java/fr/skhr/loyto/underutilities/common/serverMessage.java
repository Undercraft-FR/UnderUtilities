package fr.skhr.loyto.UnderUtilities.common;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.skhr.loyto.UnderUtilities.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class serverMessage implements IMessage {
	private String msg;
	
	public serverMessage() {}
	
	public serverMessage(String msg) {
		this.msg = msg;
	}
	
	public static String encode(String msg) {
		return Base64.getEncoder().encodeToString(msg.getBytes());
	}
	
	public static String decode(String msg) {
		try {
			return new String(Base64.getDecoder().decode(msg), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		msg = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, msg);
	}

	public static class Handler implements IMessageHandler <serverMessage, IMessage> {
		 
        @Override
        public IMessage onMessage(serverMessage message, MessageContext ctx) {
        	try {
        		String[] type = message.msg.split("\\[");
	        	if(type[0].equalsIgnoreCase("conf")) {
					String[] feature = type[1].split("\\|");

					if (feature[0].equalsIgnoreCase("tab")) {
						String[] opts = feature[1].split(";");
						UnderUtilities.header = decode(opts[0].split(":")[1]);
						UnderUtilities.header1 = decode(opts[1].split(":")[1]);
						UnderUtilities.footer = decode(opts[2].split(":")[1]);
						UnderUtilities.footer1 = decode(opts[3].split(":")[1]);
					}
				}
	        	else if(type[0].equalsIgnoreCase("cache")) {
	        		String[] opts = type[1].split(":");
	        		
	        		if(opts[0].equalsIgnoreCase("remove")) {
		        		String pseudo = decode(opts[1]);
		        		
	        			if(ClientProxy.headCache.containsKey(pseudo)) {
	        				ClientProxy.headCache.remove(pseudo);
		        		}
	        			if(ClientProxy.skinCache.containsKey(pseudo)) {
	        				ClientProxy.skinCache.remove(pseudo);
		        		}
	        		}
	        	}
        	}
        	catch(java.util.regex.PatternSyntaxException e){
        		e.printStackTrace();
        	}

            return null;
        }
    }
}
