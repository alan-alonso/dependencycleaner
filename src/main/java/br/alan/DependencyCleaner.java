package br.alan;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;

public class DependencyCleaner extends SimpleFileVisitor<Path> {

    // == fields ==
    private final String depDirName;
    private final String depFileName;

    // == constructors ==
    public DependencyCleaner(String depDirName, String depFileName) {
        this.depDirName = depDirName;
        this.depFileName = depFileName;
    }

    // == public methods ==
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir == null || !dir.getFileName().toString().equals(this.depDirName)) {
            return FileVisitResult.CONTINUE;
        }

        Path parent = dir.getParent();
        File[] parentFiles = parent.toFile().listFiles();
        for (File file : parentFiles) {
            if (file.getName().equals(depFileName)) {
                FileUtils.deleteDirectory(dir.toFile());
                return FileVisitResult.SKIP_SUBTREE;
            }
        }

        return FileVisitResult.CONTINUE;
    }
}
