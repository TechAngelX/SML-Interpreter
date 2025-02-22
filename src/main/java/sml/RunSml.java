package sml;

import java.io.IOException;
import java.util.Collection;

public class RunSml {
    /**
     * ================================================================
     * Entry point for executing Simple Machine Language (SML) programs.
     * ================================================================
     *
     * Provides the main method to:
     * - Load SML programs from a file
     * - Translate program instructions
     * - Execute the program using the Machine interpreter
     *
     * Handles program loading, translation, and execution with basic
     * error handling for file reading and argument validation.
     *
     * Command-line Usage:
     * Run the program with: java sml.RunSml src/main/resources/test1.sml
     * @author Ricki Angel
     */
    public static void main(String... args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of arguments - sml.RunSml <file> - required");
            System.exit(-1);
        }

        try {
            Translator t = new Translator();
            Collection<Method> instructions = t.readAndTranslate(args[0]);
            Machine m = new Machine();
            m.setProgram(instructions);

            System.out.println("\n== Beginning Program Execution ==");
            m.execute();
            System.out.println("\n== Ending Program Execution ==");
        } catch (IOException e) {
            System.out.println("Error reading the program from " + args[0]);
        }
    }
}
