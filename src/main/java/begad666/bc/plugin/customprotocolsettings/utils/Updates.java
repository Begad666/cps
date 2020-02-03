package begad666.bc.plugin.customprotocolsettings.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updates {
	private String PluginName;
	private String PluginID;
	private String ConfigVersion;
	private String CurrentVersion;
	private String Link;
	private String Message = "You are up to date";

	public Updates(String pluginName, String pluginID, String configVersion, String currentVersion) {
		PluginName = pluginName;
		PluginID = pluginID;
		ConfigVersion = configVersion;
		CurrentVersion = currentVersion;
		Link = "https://api.spiget.org/v2/resources/" + PluginID + "/";
	}

	public JsonObject getInfo() {
		try {
			URL url = new URL(Link + "versions/latest?" + System.currentTimeMillis());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
