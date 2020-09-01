package begad.mc.bc.plugin.cps.utils;

import begad.mc.bc.plugin.cps.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Backup {
    public Backup() {
    }

    public static int saveBackup(String name) {
        File dir = new File(Core.getInstance().getDataFolder() + "/" + Core.getConfig().get().getString("backup.folder"));
        boolean result;
        if (!dir.exists()) {
            result = dir.mkdirs();
        } else {
            result = true;
        }

        if (!result) {
            return 1;
        } else {
            File backupfile = new File(dir, name + ".yml");
            File configfile = new File(Core.getInstance().getDataFolder(), "config.yml");

            FileInputStream inputStream;
            try {
                inputStream = new FileInputStream(configfile);
            } catch (FileNotFoundException var8) {
                var8.printStackTrace();
                return 3;
            }

            try {
                Files.copy(inputStream, backupfile.toPath());
                return 0;
            } catch (IOException var7) {
                var7.printStackTrace();
                return 2;
            }
        }
    }

    public static int loadBackup(String name) {
        File dir = new File(Core.getInstance().getDataFolder() + "/" + Core.getConfig().get().getString("backup.folder"));
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            return result ? 2 : 1;
        } else {
            File backup_file = new File(dir, name);
            if (!backup_file.exists()) {
                return 3;
            } else {
                FileInputStream inputStream;
                try {
                    inputStream = new FileInputStream(backup_file);
                } catch (FileNotFoundException var6) {
                    var6.printStackTrace();
                    return 3;
                }

                try {
                    Files.copy(inputStream, Paths.get(Core.getInstance().getDataFolder().toPath() + "/config.yml"), StandardCopyOption.REPLACE_EXISTING);
                    return 0;
                } catch (IOException var5) {
                    var5.printStackTrace();
                    return 4;
                }
            }
        }
    }
}
