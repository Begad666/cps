package net.begad666.bc.plugin.customprotocolsettings.utils;

import net.md_5.bungee.api.ProxyServer;

import java.sql.*;

public class DatabaseConnectionManager {
	private static Connection connection;
	private static Statement stmt;
	private static boolean isConnected = false;

	public static boolean getConnected() {
		return isConnected;
	}

	public static void connect() {
		if (!isConnected) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The MySQL driver was not found, cancelling mysql connection");
				return;
			}
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + Config.getconfig().getString("connectionsettings.host") + ":" + Config.getconfig().getInt("connectionsettings.port") + "/" + Config.getconfig().getString("connectionsettings.database"), Config.getconfig().getString("connectionsettings.user"), Config.getconfig().getString("connectionsettings.password"));
			} catch (SQLException e) {
				e.printStackTrace();
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error establishing connection to the database, cancelling mysql connection");
				return;
			}
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while finishing up connection, disconnecting from database");
				try {
					connection.abort(command -> {

					});
				} catch (SQLException severeerror) {
					ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " *****ERROR*****\nCannot Disconnect from the database, report it to https://github.com/begad666-mc-works/cps \nException:\n" + severeerror);
				}
				return;
			}
			isConnected = true;
		}
	}

	public static void disconnect() {
		if (isConnected) {
			try {
				connection.abort(command -> {

				});
			} catch (SQLException e) {
				ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " *****ERROR*****\nCannot Disconnect from the database, report it to https://github.com/begad666-mc-works/cps \nException:\n" + e);
			}
			isConnected = false;
		}
	}

	public static int executeUpdate(String StmtToExec) {
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while creating new statement, cancelling statement execution");
			return 5;
		}
		try {
			int result = stmt.executeUpdate(StmtToExec);
			stmt = null;
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while executing statement: \" " + StmtToExec + " \", cancelling statement execution");
			return 5;
		}
	}

	public static ResultSet executeQuery(String StmtToExec) {
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while creating new statment, cancelling statment execution");
			return null;
		}
		try {
			ResultSet result = stmt.executeQuery(StmtToExec);
			stmt = null;
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There was an error while executing statment: \" " + StmtToExec + " \", cancelling statment execution");
			return null;
		}
	}
}
