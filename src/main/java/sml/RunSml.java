package sml;

import java.io.IOException;
import java.util.Collection;

public class RunSml {
    /**
     * =========================================================================================================
     * Entry point for executing Simple Machine Language (SML) programs.
     * =========================================================================================================
     *
     * Provides the main method to load SML programs from a file, translate program instructions and Execute the
     * program using the Machine interpreter. Handles program loading, translation, and execution with basic
     * error handling for file reading and argument validation.
     *
     * Dependency Injection:
     * - Constructor-based manual DI
     * - Explicit component control
     *
     * Command-line Usage:
     * Compile sml folder to /target/classes directory.
     * Navigate to root folder
     * Run the program with: java sml.RunSml src/main/resources/test1.sml
     *
     * @author Ricki Angel
     */

    private final Translator translator;
    private final Machine machine;

    public RunSml(Translator translator, Machine machine) {
        this.translator = translator;
        this.machine = machine;
    }

    public void run(String filename) throws IOException {
        Collection<Method> instructions = translator.readAndTranslate(filename);
        machine.setProgram(instructions);
        machine.execute();
    }

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
