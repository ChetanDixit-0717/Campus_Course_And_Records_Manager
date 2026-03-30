package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.util.RecursionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Stream;

public class BackupService {

    private final Path dataFolderPath;
    private final Path backupBaseFolderPath;

    public BackupService() {
        AppConfig config = AppConfig.getInstance();
        this.dataFolderPath = config.getDataFolderPath();
        this.backupBaseFolderPath = config.getBackupFolderPath();
    }

    public Path createTimestampedBackup() throws IOException {
        System.out.println("[DEBUG] Starting backup process...");
        System.out.println("[DEBUG] Source data folder: " + dataFolderPath.toAbsolutePath());
        System.out.println("[DEBUG] Base backup folder: " + backupBaseFolderPath.toAbsolutePath());

        try {
            if (!Files.exists(dataFolderPath)) {
                System.out.println("[DEBUG] Data folder does not exist. Creating it now.");
                Files.createDirectories(dataFolderPath);
            }
            if (!Files.isDirectory(dataFolderPath)) {
                throw new IOException("FATAL: The source path 'data' is a file, not a directory. Please fix this.");
            }
            Files.createDirectories(backupBaseFolderPath);
            System.out.println("[DEBUG] Base directories are verified and ready.");
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to prepare base directories. Check permissions.");
            e.printStackTrace();
            throw e;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path currentBackupFolder = backupBaseFolderPath.resolve("backup_" + timestamp);
        Files.createDirectories(currentBackupFolder);
        System.out.println("Created backup folder: " + currentBackupFolder.toAbsolutePath());
        
        long fileCount;
        try (Stream<Path> pathStream = Files.walk(dataFolderPath)) {
            fileCount = pathStream.filter(p -> !p.equals(dataFolderPath)).count();
        }
        if (fileCount == 0) {
            System.out.println("[INFO] Source data folder is empty. Nothing to back up.");
            System.out.println("\nBackup completed successfully (created empty backup folder).");
            return currentBackupFolder;
        }

        System.out.println("[DEBUG] Starting file copy from source to destination...");
        try (Stream<Path> paths = Files.walk(dataFolderPath)) {
            paths.forEach(sourcePath -> {
                try {
                    Path destinationPath = currentBackupFolder.resolve(dataFolderPath.relativize(sourcePath));
                    if (sourcePath.equals(dataFolderPath)) {
                        return;
                    }
                    System.out.println("[DEBUG] Processing: " + sourcePath);
                    
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(destinationPath);
                        System.out.println("  Created directory: " + destinationPath);
                    } else {
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("  Copied file: " + sourcePath.getFileName());
                    }

                } catch (IOException e) {
                    System.err.println("[ERROR] A failure occurred inside the copy stream for path: " + sourcePath);
                    e.printStackTrace();
                    throw new RuntimeException(e); 
                }
            });
        } catch (Exception e) {
            System.err.println("[ERROR] The backup process failed catastrophically.");
            e.printStackTrace();
            throw new IOException("Backup failed. Reason: " + e.getMessage(), e);
        }

        System.out.println("\nBackup completed successfully to: " + currentBackupFolder.toAbsolutePath());
        return currentBackupFolder;
    }

    public void showBackupSize(Path backupFolder) {
        if (!Files.exists(backupFolder) || !Files.isDirectory(backupFolder)) {
            System.out.println("Backup folder does not exist or is not a directory: " + backupFolder);
            return;
        }
        try {
            long totalSize = RecursionUtils.calculateDirectorySize(backupFolder);
            System.out.println("Total size of backup folder '" + backupFolder.getFileName() + "': " + totalSize + " bytes");
        } catch (IOException e) {
            System.err.println("Error calculating backup size: " + e.getMessage());
        }
    }

    public void cleanOldBackups(int daysToKeep) throws IOException {
        System.out.println("Cleaning backups older than " + daysToKeep + " days...");
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        try (Stream<Path> paths = Files.list(backupBaseFolderPath)) {
            paths.filter(Files::isDirectory)
                 .filter(p -> {
                     try {
                         String folderName = p.getFileName().toString();
                         if (folderName.startsWith("backup_") && folderName.length() >= 22) {
                             String timestampStr = folderName.substring(7);
                             LocalDateTime folderTime = LocalDateTime.parse(timestampStr, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                             return folderTime.isBefore(cutoff);
                         }
                     } catch (Exception e) { /* Ignore */ }
                     return false;
                 })
                 .forEach(folderToDelete -> {
                     try {
                         Files.walk(folderToDelete)
                              .sorted(Comparator.reverseOrder())
                              .forEach(path -> {
                                  try {
                                      Files.delete(path);
                                  } catch (IOException e) {
                                      System.err.println("Failed to delete " + path + ": " + e.getMessage());
                                  }
                              });
                         System.out.println("Cleaned old backup: " + folderToDelete.getFileName());
                     } catch (IOException e) {
                         System.err.println("Error cleaning backup folder " + folderToDelete + ": " + e.getMessage());
                     }
                 });
        }
        System.out.println("Backup cleaning complete.");
    }
}