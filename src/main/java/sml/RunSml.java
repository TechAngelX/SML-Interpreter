package sml;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

    public static RunSml create() {
        return new RunSml(new Translator(), new Machine());
    }
    public void run(String filename) throws IOException {
        Collection<Method> instructions = translator.readAndTranslate(filename);
        machine.setProgram(instructions);
        machine.execute();
    }

    public static void main(String... args) {
        try {


            Constructor<RunSml> constructor = RunSml.class.getConstructor(Translator.class, Machine.class);
            Translator translator = new Translator();
            Machine machine = new Machine();

            RunSml runner = constructor.newInstance(translator, machine);
            runner.run(args[0]);
        } catch (Exception e) {
            System.err.println("Error creating RunSml instance: " + e.getMessage());
        }
    }
}
