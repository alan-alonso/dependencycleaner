package br.alan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 * Unit tests for Main class
 */
public class MainTest {

    @Test
    public void parseArgsObligatory() throws ArgumentParserException {
        // arrange
        String[] longArgs = { "--depdirname", "node_modules", "--depfilename", "package.json", "--input", "somedir" };

        // act
        Map<String, Object> parsedArgs = Main.parseArgs(longArgs);

        // assert
        assertEquals(longArgs[1], parsedArgs.get("depdirname"));
        assertEquals(longArgs[3], parsedArgs.get("depfilename"));
        assertEquals(longArgs[5], parsedArgs.get("input"));

        // arrange
        String[] shortArgs = { "-ddn", "node_modules", "-dfn", "package.json", "-i", "somedir" };

        // act
        parsedArgs = Main.parseArgs(shortArgs);

        // assert
        assertEquals(shortArgs[1], parsedArgs.get("depdirname"));
        assertEquals(shortArgs[3], parsedArgs.get("depfilename"));
        assertEquals(shortArgs[5], parsedArgs.get("input"));
    }

    @DisplayName(value = "Parsed arguments should set deep discovery.")
    @Test
    public void parseArgsShouldSetDeepDiscovery() throws ArgumentParserException {
        // arrange (passing obligatory args)
        String[] args = { "-ddn", "node_modules", "-dfn", "package.json", "-i", "somedir", "--deep" };

        // act
        Map<String, Object> parsedArgs = Main.parseArgs(args);

        // assert true
        assertTrue((Boolean) parsedArgs.get("deep"));

        // act
        parsedArgs = Main.parseArgs(Arrays.copyOfRange(args, 0, args.length - 1));

        // assert false
        assertFalse((Boolean) parsedArgs.get("deep"));
    }
}
