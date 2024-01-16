package dev.zontreck.essentials.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler
{

    public static String readFile(String filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            return new String(fileBytes);
        } catch (IOException e) {
            return "An error occurred: " + e.getMessage();
        }
    }
    public static void writeFile(String filePath, String newContent) {
        try {
            Files.write(Paths.get(filePath), newContent.getBytes());
        } catch (IOException e) {
        }
    }
}
