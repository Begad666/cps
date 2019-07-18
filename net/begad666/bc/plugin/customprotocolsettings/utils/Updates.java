package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.begad666.bc.plugin.customprotocolsettings.Main;




public class Updates {
	private static String isUpdateRequired;
	private static String VersionForUpdate;
	private static String Message;
	private static String ConfigVersion =  "v1";

	   public static String getLatestVersion() {
     try {
       URL url = new URL("https://api.spiget.org/v2/resources/69385/versions/latest?" + System.currentTimeMillis());
       HttpURLConnection connection = (HttpURLConnection)url.openConnection();
       connection.setUseCaches(true);
       connection.addRequestProperty("User-Agent", "ShiftCraft Plugin Updater |" + Main.getInstance().getDescription().getName());
       connection.setDoOutput(true);
       BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
       String content = "";
       String input; while ((input = br.readLine()) != null) {
         content = content + input;
       }
      br.close();
      
      try
    {
    JsonObject updatestat;
    updatestat = (JsonObject)Gson.getGson().fromJson(content, JsonObject.class);
    return updatestat.get("name").getAsString();
      } catch (JsonParseException e) {
     e.printStackTrace();
     return null; }
		}catch (MalformedURLException e) {
       return null;
     } catch (IOException e) 
     {
    	return null;
     }
     }
      
	 public static String getCurrentVersion()
	 {
	 return (String)Main.getInstance().getDescription().getVersion();
	 }
	 public static String getConfigVersion()
	 {
	 return ConfigVersion;
	 }
	 public static void setUpdateInfo(String iur, String vfu, String m)
	 {
	isUpdateRequired = iur;
	VersionForUpdate = vfu;
	Message = m;
	 }
	 public static ArrayList<String> getUpdateInfo()
	 {
      ArrayList<String> list = new ArrayList();
      list.add(isUpdateRequired);
      list.add(VersionForUpdate);
      list.add(Message);
	  return list;
	 }
}
