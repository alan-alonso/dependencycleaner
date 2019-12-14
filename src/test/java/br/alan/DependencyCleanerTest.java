package br.alan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DependencyCleanerTest
 */
public class DependencyCleanerTest {

    // == fields ==
    public static final Path testDir = Paths.get("testDir");

    @BeforeAll
    public static void beforeClass() {
        try {
            FileUtils.deleteDirectory(testDir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() {
        // create tree with directories
        createFakeLangProjDir(Paths.get(testDir.toString(), "node"), "node_modules", "package.json");
        createFakeLangProjDir(Paths.get(testDir.toString(), "python"), ".venv", null);
    }

    @AfterEach
    public void tearDown() {
        try {
            FileUtils.deleteDirectory(testDir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void afterClass() {
        try {
            FileUtils.deleteDirectory(testDir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFakeLangProjDir(Path path, String depDir, String depFile) {
        try {
            if (depDir != null && !depDir.isEmpty()) {
                Files.createDirectories(Paths.get(path.toString(), depDir));
            }
            if (depFile != null && !depFile.isEmpty()) {
                Files.createFile(Paths.get(path.toString(), depFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @DisplayName("Visited directory contains dependencies to be deleted.")
    @Test
    public void preVisitDirectoryShouldDeleteDependencies() throws IOException {
        // arrange
        DependencyCleaner cleaner = new DependencyCleaner("node_modules", "package.json");

        // act
        FileVisitResult result = cleaner.preVisitDirectory(Paths.get(testDir.toString(), "node", "node_modules"), null);

        // assert
        assertEquals(FileVisitResult.SKIP_SUBTREE, result);
        assertFalse(Files.exists(Paths.get(testDir.toString(), "node", "node_modules")));
    }

    @DisplayName("Visited directory doesn't contain dependencies file and shouldn't be deleted.")
    @Test
    public void preVisitDirectoryShouldNotDeleteDependencies() throws IOException {
        // arrange
        DependencyCleaner cleaner = new DependencyCleaner(".venv", "requirements.txt");

        // act
        FileVisitResult result = cleaner.preVisitDirectory(Paths.get(testDir.toString(), "python", ".venv"), null);

        // assert
        assertEquals(FileVisitResult.CONTINUE, result);
        assertTrue(Files.exists(Paths.get(testDir.toString(), "python", ".venv")));
    }

    @DisplayName("Visited directory doesn't contain dependencies and should be ignored.")
    @Test
    public void preVisitDirectoryShouldIgnore() throws IOException {
        // arrange
        DependencyCleaner cleaner = new DependencyCleaner(".venv", "python");

        // act
        FileVisitResult result = cleaner.preVisitDirectory(testDir, null);

        // assert
        assertEquals(FileVisitResult.CONTINUE, result);
        assertTrue(Files.exists(testDir));
    }
}