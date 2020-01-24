package net.begad666.bc.plugin.customprotocolsettings.features;

import java.util.ArrayList;

import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.MainUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectNotAllowedUsers implements Listener {
	
	 @EventHandler
	 public void beforeLogin(PreLoginEvent event) 
	 {
		 int protocol = event.getConnection().getVersion();
		 ArrayList<Integer> allowedprotocols = (ArrayList<Integer>)Config.getconfig().getIntList("settings.allowed-protocols");
		 ArrayList<String> allowedplayers = (ArrayList<String>)Config.getconfig().getStringList("allowed-players");
		 if (Config.getconfig().getBoolean("settings.maintenance-enabled")) 
		 {
			 if (allowedplayers.contains(event.getConnection().getName())) 
			 {
				 if (allowedprotocols.contains(protocol)) 
				 {

				 }
				 else 
				 {
					 event.getConnection().disconnect(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("messages.not-supported-client-message"))));
					 return;
				 }
			 }
			 else 
			 {
				 event.getConnection().disconnect(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("messages.maintenance-message"))));
				 return;
			 }
		 }
		 else 
		 {
			 if (ProxyServer.getInstance().getOnlineCount() >= Config.getconfig().getInt("network-info.max-players")) 
			 {
				 if (allowedplayers.contains(event.getConnection().getName())) 
				 {

				 }
				 else 
				 {
					 event.getConnection().disconnect(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("messages.full-message"))));
					 return; 
				 }
			 } 
		 }
	 }
}
