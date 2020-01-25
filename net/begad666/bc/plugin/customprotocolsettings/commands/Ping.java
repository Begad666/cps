package net.begad666.bc.plugin.customprotocolsettings.commands;

import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.MainUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping extends Command {

	public Ping() 
	{
		super("ping" , "cps.ping" , new String[0]);
	}

	@Override
	public void execute(CommandSender sender, final String[] args) 
	{
		if (args.length == 0)
		{
			if (sender instanceof ProxiedPlayer)
			{
				ProxiedPlayer player = (ProxiedPlayer)sender;
				int ping = player.getPing();
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.ping")) + " Your Ping is " + ping + " ms"));
			}
			if (!(sender instanceof ProxiedPlayer))
			{
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.ping")) + " You Must Be a Player!"));
			}
		}
		if (args.length == 1)
		{
			try 
			{
				int ping = ProxyServer.getInstance().getPlayer(args[0]).getPing();
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.ping")) + args[0] + " Ping is " + ping + " ms"));
			} 
			catch (Exception e)
			{
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.ping")) + " That Player is not online!"));
			}
		}

	}

}
