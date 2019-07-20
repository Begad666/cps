package net.begad666.bc.plugin.customprotocolsettings.features;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.Processarraylists;
import net.begad666.bc.plugin.customprotocolsettings.utils.ProcessStrings;

public class ChangePingData implements Listener {

	
	@EventHandler
	public void Ping(ProxyPingEvent event) 
	{
	ServerPing serverPing = event.getResponse();
	serverPing.setVersion(new ServerPing.Protocol( 
	Config.getconfig().getString("protocol-name"), Processarraylists.processversion(serverPing.getVersion().getProtocol())));
	serverPing.setPlayers(new ServerPing.Players(Config.getconfig().getInt("max-players"),serverPing
	.getPlayers().getOnline(), Processarraylists.processhovermessage()));
	serverPing.setDescriptionComponent(new TextComponent(ProcessStrings.replacecodesandcolors(ProcessStrings.replaceplaceholders(Config.getconfig().getString("motd.1"))) + "\n§r" + 
	ProcessStrings.replacecodesandcolors(ProcessStrings.replaceplaceholders(Config.getconfig().getString("motd.2")))));
	serverPing.setFavicon(serverPing.getFaviconObject());
	event.setResponse(serverPing); 
	serverPing = null;
  }


	
}
    
        



    


