package begad.bc.plugin.customprotocolsettings.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.md_5.bungee.api.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updates {
	private Plugin Plugin;
	private String PluginName;
	private String PluginID;
	private String ConfigVersion;
	private String CurrentVersion;
	private String Link;
	private String Message = "You are up to date";

	public Updates(Plugin plugin, String pluginName, String pluginID, String configVersion, String currentVersion) {
		Plugin = plugin;
		PluginName = pluginName;
		PluginID = pluginID;
		ConfigVersion = configVersion;
		CurrentVersion = currentVersion;
		Link = "https://api.spiget.org/v2/resources/" + PluginID + "/";
	}

	public boolean IsUpdateRequired() {
		String latest = getLatestVersion();
		if (latest.equals(CurrentVersion)) {
			return false;
		} else {
			return true;
		}
	}

	public JsonObject getInfo() {
		try {
			URL url = new URL(Link + "versions/latest?" + System.currentTimeMillis());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setUseCaches(true);
			connection.addRequestProperty("User-Agent", PluginName + " Plugin Updater | " + getCompileCurrentVersion());
			connection.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder content = new StringBuilder();
			String input;
			while ((input = br.readLine()) != null) {
				content.append(input);
			}
			br.close();
			try {
				JsonObject updateinfo;
				updateinfo = new Gson().fromJson(content.toString(), JsonObject.class);
				return updateinfo;
			} catch (JsonParseException e) {
				e.printStackTrace();
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getLatestVersion() {
		try {
			URL url = new URL(Link + "versions/latest?" + System.currentTimeMillis());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setUseCaches(true);
			connection.addRequestProperty("User-Agent", PluginName + " Plugin Updater | " + getCompileCurrentVersion());
			connection.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder content = new StringBuilder();
			String input;
			while ((input = br.readLine()) != null) {
				content.append(input);
			}
			br.close();
			try {
				JsonObject updateinfo;
				updateinfo = new Gson().fromJson(content.toString(), JsonObject.class);
				return updateinfo.get("name").getAsString();
			} catch (JsonParseException e) {
				e.printStackTrace();
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean DownloadAndInstall() {
		File jar = new File(Plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		try {
			URL url = new URL(Link + "download");
			ReadableByteChannel channel = Channels.newChannel(url.openStream());
			FileOutputStream stream = new FileOutputStream(jar, false);
			stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public String getCompileCurrentVersion() {
		return CurrentVersion;
	}

	public String getCompileConfigVersion() {
		return ConfigVersion;
	}

	public void setMessage(String msg) {
		Message = msg;
	}

	public String getMessage() {
		return Message;
	}

}
