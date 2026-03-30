package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static AppConfig instance;
    private Path dataFolderPath;
    private Path backupFolderPath;

    private AppConfig() {
        this.dataFolderPath = Paths.get("data");
        this.backupFolderPath = Paths.get("backup");
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public Path getDataFolderPath() {
        return dataFolderPath;
    }

    public Path getBackupFolderPath() {
        return backupFolderPath;
    }
}
