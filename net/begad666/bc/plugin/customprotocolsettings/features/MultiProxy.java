package net.begad666.bc.plugin.customprotocolsettings.features;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;

public class MultiProxy {
	
	public static void ApplyData(Gson config, boolean autosaveconfig) 
	{
		Main.getInstance().getLogger().info("AutoPull data ready, starting config changes");
		try 
		{
		Gson gson = new Gson();
		JsonObject configobject = gson.fromJson(config.toString(), JsonObject.class);
		Config.getconfig().set("update-checker-enabled", configobject.get("update-checker-enabled").getAsBoolean());
		Config.getconfig().set("password",configobject.get("password").getAsString());
		JsonObject netinfo = configobject.get("network-info").getAsJsonObject();
		Config.getconfig().set("network-info.name", netinfo.get("name").getAsString());
		Config.getconfig().set("network-info.max-players", netinfo.get("max-players").getAsInt());
		JsonObject settings = configobject.get("settings").getAsJsonObject();
		ArrayList<Integer> allowed_protocols = new ArrayList<Integer>();
		JsonArray ap = settings.get("allowed-protocols").getAsJsonArray();
		for (int i = 0; i < ap.size(); i++) 
		{ 
			allowed_protocols.add(ap.get(i).getAsInt());   
		} 
		Config.getconfig().set("settings.allowed-protocols", allowed_protocols);
		Config.getconfig().set("settings.maintenance-enabled", settings.get("maintenance-enabled").getAsBoolean());
		}
		catch (JsonParseException e) 
		{
			Main.getInstance().getLogger().severe("CustomProtocolSettings: \n*****ERRROR*****\nThere was an error while parsing the config from the database, reloading the config from file, Exception:\n" + e);
			Config.check();
			Main.getInstance().getLogger().info("Done reloading from config file");
		}
		
		
	}
}
