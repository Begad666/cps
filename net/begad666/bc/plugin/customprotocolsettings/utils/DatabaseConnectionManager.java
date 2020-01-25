package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.sql.*;

import net.md_5.bungee.api.ProxyServer;

public class DatabaseConnectionManager {
	private static Connection connection;
	private static Statement stmt;
	private static boolean isConnected;
	public static Connection getConnection()
	{
		return connection;
	}
	public static boolean getConnected()
	{
		return isConnected;	
	}
	public static void connect()
	{
		if (isConnected == false)
		{
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");
			} 
			catch (ClassNotFoundException e) 
			{
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The MySQL Driver was not found, please make sure you compiled bungeecord with it (because every pre-compiled version of bungeecord have it), cancelling mysql connection");
				return;
			}  
			try 
			{
				connection = DriverManager.getConnection("jdbc:mysql://" + Config.getconfig().getString("connectionsettings.host") + ":" + Config.getconfig().getInt("connectionsettings.port") + "/" + Config.getconfig().getString("connectionsettings.database"),Config.getconfig().getString("connectionsettings.user"),Config.getconfig().getString("connectionsettings.password"));
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error establishing connection to the database, read the stack trace carefuly to check what is wrong , cancelling mysql connection");
				return;
			}
			try 
			{
				connection.setAutoCommit(true);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while setting up autocommit, read the stack trace carefuly to check what is wrong, disconnecting from database");
				connection = null;
				return;
			}
			isConnected = true;
		}
	}
	public static void disconnect()
	{
		if (isConnected == true)
		{
			connection = null;
			isConnected = false;
		}
	}
	public static int executeUpdate(String StmtToExec)
	{
		try 
		{
			stmt = connection.createStatement();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while creating new statment , read the stack trace carefuly to check what is wrong , cancelling statment execution");
			return 5;
		}
		try 
		{
			int result = stmt.executeUpdate(StmtToExec);
			stmt = null;
			return result;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while executing statment: \" " + StmtToExec + " \" , read the stack trace carefuly to check what is wrong , cancelling statment execution");
			return 5;
		}  
	}
	public static ResultSet executeQuery(String StmtToExec)
	{
		try 
		{
			stmt = connection.createStatement();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while creating new statment , read the stack trace carefuly to check what is wrong , cancelling statment execution");
			return null;
		}
		try 
		{
			ResultSet result = stmt.executeQuery(StmtToExec);
			stmt = null;
			return result;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while executing statment: \" " + StmtToExec + " \" , read the stack trace carefuly to check what is wrong , cancelling statment execution");
			return null;
		}  
	}
}
