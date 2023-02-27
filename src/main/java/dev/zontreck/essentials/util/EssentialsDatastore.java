package dev.zontreck.essentials.util;

import java.nio.file.Path;

import dev.zontreck.libzontreck.util.FileTreeDatastore;

public class EssentialsDatastore extends FileTreeDatastore
{
    public static final Path AEBASE;
    static{
        AEBASE = FileTreeDatastore.of("essentials");
    }

    public static Path of(String path)
    {
        return AEBASE.resolve(path);
    }
}
