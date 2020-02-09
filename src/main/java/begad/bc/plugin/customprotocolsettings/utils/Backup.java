package begad.bc.plugin.customprotocolsettings.utils;

import begad.bc.plugin.customprotocolsettings.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Backup {
	/**
	 * 0 - completed
	 * 1 - cannot create directory
	 * 2 - cannot write to backup file
	 * 3 - no config file found
	 **/
	public static int SaveBackup(String name) {
		File dir = new File(Main.getInstance().getDataFolder() + "/backup");
		boolean result;
		if (!dir.exists()) {
			result = dir.mkdir();
		} else {
			result = true;
		}
		if (!result) {
			return 1;
		}
		File backupfile = new File(dir, name + ".yml");
		File configfile = new File(Main.getInstance().getDataFolder(), "config.yml");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(configfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 3;
		}
		try {
			Files.copy(inputStream, backupfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		return 0;
	}

	/**
	 * 0 - completed
	 * 1 - couldn't create
	 * 2 - directory created but no files
	 * 3 - file not found
	 * 4 - cannot write to config
	 **/
	public static int LoadBackup(String name) {
		File dir = new File(Main.getInstance().getDataFolder() + "/backup");
		if (!dir.exists()) {
			boolean result = dir.mkdir();
			if (result) {
				return 2;
			} else {
				return 1;
			}
		}
		File backup_file = new File(dir, name + ".yml");
		if (!backup_file.exists()) {
			return 3;
		}
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(backup_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 3;
		}
		try {
			Files.copy(inputStream, Paths.get(Main.getInstance().getDataFolder().toPath() + "/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}
		return 0;
	}
}
