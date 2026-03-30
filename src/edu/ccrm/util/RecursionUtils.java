package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class RecursionUtils {

    public static long calculateDirectorySize(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path must be a directory: " + directory);
        }

        long totalSize = 0;
        try (Stream<Path> walk = Files.walk(directory)) {
            totalSize = walk.filter(Files::isRegularFile)
                            .mapToLong(path -> {
                                try {
                                    return Files.size(path);
                                } catch (IOException e) {
                                    System.err.println("Error getting size of " + path + ": " + e.getMessage());
                                    return 0L;
                                }
                            })
                            .sum();
        }
        return totalSize;
    }

    public static void listFilesByDepth(Path directory, int maxDepth) throws IOException {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path must be a directory: " + directory);
        }
        System.out.println("Listing files in '" + directory.getFileName() + "' up to depth " + maxDepth + ":");
        try (Stream<Path> walk = Files.walk(directory, maxDepth)) {
            walk.forEach(path -> {
                int depth = directory.relativize(path).getNameCount();
                String indent = "  ".repeat(depth);
                System.out.println(indent + (Files.isDirectory(path) ? "[DIR] " : "[FILE] ") + path.getFileName());
            });
        }
    }

}
