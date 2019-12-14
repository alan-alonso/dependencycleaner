package br.alan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 * Main
 *
 */
public class Main {
    public static void main(String[] args) throws ArgumentParserException, IOException {
        final Map<String, Object> parsedArgs = parseArgs(args);
        final Path dir = Paths.get(parsedArgs.get("input").toString());
        final DependencyCleaner cleaner = new DependencyCleaner(parsedArgs.get("depdirname").toString(),
            parsedArgs.get("depfilename").toString());

        if ((Boolean) parsedArgs.get("deep")) {
            Files.walkFileTree(dir, cleaner);
        } else {
            cleaner.preVisitDirectory(dir, null);
        }
    }

    public static Map<String, Object> parseArgs(String[] args) throws ArgumentParserException {
        ArgumentParser argparser = ArgumentParsers
                                                  .newFor("dependencycleaner")
                                                  .build()
                                                  .description("Cleans dependency directories of programming languages")
                                                  .version("1.0.0");
        argparser
          .addArgument("--depdirname", "-ddn")
          .help("Dependencies directory name")
          .required(true)
          .type(String.class);

        argparser
          .addArgument("--depfilename", "-dfn")
          .help("Dependencies file name")
          .required(true)
          .type(String.class);

        argparser
          .addArgument("--input", "-i")
          .help("Input directory for dependency cleaning")
          .required(true)
          .type(String.class);

        argparser
          .addArgument("--deep")
          .action(Arguments.storeTrue())
          .help("Set deep discovery of dependencies")
          .setDefault(false)
          .type(Boolean.class);

        return argparser.parseArgs(args).getAttrs();
    }
}
