package net.begad666.bc.plugin.customprotocolsettings.utils;

import com.google.gson.GsonBuilder;

  public final class Gson
  {
   private static final com.google.gson.Gson gson = getGsonBuilder().create();
    
  
  
  
  
    public static com.google.gson.Gson getGson()
    {
     return gson;
    }
    
  
  
  
  
    public static GsonBuilder getGsonBuilder()
    {
     return new GsonBuilder();
    }
  }
