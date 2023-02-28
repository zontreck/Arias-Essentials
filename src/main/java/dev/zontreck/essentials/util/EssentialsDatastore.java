package dev.zontreck.essentials.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.zontreck.libzontreck.util.FileTreeDatastore;

public class EssentialsDatastore extends FileTreeDatastore
{
    public static final Path AEBASE;

    static{
        AEBASE = FileTreeDatastore.of("essentials");
        if(!AEBASE.toFile().exists())
        {
            try {
                Files.createDirectory(AEBASE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initialize()
    {
        if(!AEBASE.toFile().exists())
        {
            try {
                Files.createDirectory(AEBASE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Path of(String path, boolean directory)
    {
        Path p = AEBASE.resolve(path);
        if(!directory)return p;
        if(!p.toFile().exists())
        {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return p;
    }
}
