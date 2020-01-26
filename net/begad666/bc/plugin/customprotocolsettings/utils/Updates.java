package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import net.begad666.bc.plugin.customprotocolsettings.Main;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class Updates {
	private static String ConfigVersion =  "v6";
	private static String CurrentVersion = "v2.0";
	private static String Message = "No Updates Required";

	public static String getLatestVersion() 
	{
		try 
		{
			URL url = new URL("https://api.spiget.org/v2/resources/69385/versions/latest?" + System.currentTimeMillis());
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setUseCaches(true);
			connection.addRequestProperty("User-Agent", Main.getInstance().getDescription().getName() + " Plugin Updater | " + getCompileCurrentVersion());
			connection.setDoOutput(true);
			BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
			String content = "";
			String input; while ((input = br.readLine()) != null) 
			{
				content = content + input;
			}
			br.close();    
			try
			{
			    JsonObject updatestat;
			    updatestat = (JsonObject)new Gson().fromJson(content, JsonObject.class);
			    return updatestat.get("name").getAsString();
			} 
			catch (JsonParseException e) 
			{
				e.printStackTrace();
				return null; 
			}
		}
		catch (MalformedURLException e) 
		{
			return null;
		} 
		catch (IOException e) 
		{
			return null;
		}
     }
      
	 public static String getCompileCurrentVersion()
	 {
		 return CurrentVersion;
	 }
	 
	 public static String getCompileConfigVersion()
	 {
		 return ConfigVersion;
	 }
	 
	 public static void setMessage(String msg) 
	 {
		 Message = msg;
	 }
	 
	 public static String getMessage() 
	 {
		 return Message;
	 }
	 
}
