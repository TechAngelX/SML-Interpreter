package sml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import sml.config.SmlConfig;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
/**
 * Serves as the main entry point for executing SML programs.
 * <p>
 * This service component is responsible for orchestrating the SML execution pipeline by:
 * <ol>
 *   <li>Loading and translating SML program files (.sml) into executable {@link Method} collections</li>
 *   <li>Configuring the virtual machine with the translated program</li>
 *   <li>Initiating program execution</li>
 * </ol>
 * <p>
 * This class implements the Spring Framework's and manual Command Line dependency injection  for configuration of its
 * core dependencies. The {@link Translator} for program parsing and the {@link Machine} for program execution.
 *
 * <h2>Spring Framework Usage:</h2>
 * <pre>
 *   ApplicationContext context = new AnnotationConfigApplicationContext(SmlConfiguration.class);
 *   RunSml runner = context.getBean(RunSml.class);
 *   runner.run("path/to/program.sml");
 * </pre>
 *
 * <h2>Command Line Usage: There are two ways of running the machine via command line:</h2>
 * <h3>Method 1: Using Maven Exec Plugin</h3>
 * <pre>
 *   mvn exec:java -Dexec.mainClass="sml.RunSml" -Dexec.args="src/main/resources/test1.sml"
 * </pre>
 *
 * <h3>Method 2: Manual Classpath Configuration</h3>
 * <pre>
 *   # First, prepare the project and dependencies
 *   mvn clean package dependency:copy-dependencies
 *
 *   # Then run the application
 *   java -cp "target/classes:target/dependency/*" sml.RunSml src/main/resources/test1.sml
 * </pre>
 *
 * @author Ricki Angel
 * @see Translator
 * @see Machine
 * @see Method
 */

@Service
public class RunSml {
    private final Translator translator;
    private final Machine machine;

    @Autowired
    public RunSml(Translator translator, Machine machine) {
        this.translator = translator;
        this.machine = machine;
    }

    /**
     * Factory method for creating a RunSml instance using manual dependency injection.
     * This supports the CLI path when Spring context is not available.
     *
     * @return a new RunSml instance with manually injected dependencies
     */
    public static RunSml create() {
        return new RunSml(new Translator(), new Machine());
    }

    /**
     * Executes an SML program from the specified file.
     *
     * @param filename The path to the SML program file
     * @throws IOException If an error occurs during file reading
     */
    public void run(String filename) throws IOException {
        Collection<Method> instructions = translator.readAndTranslate(filename);
        machine.setProgram(instructions);
        machine.execute();
    }

    /**
     * Main entry point for command-line execution.
     * Attempts to use Spring DI first, falls back to reflection-based DI if Spring fails.
     *
     * @param args Command-line arguments (expects the SML file path as the first argument)
     */
    public static void main(String... args) {
        if (args.length < 1) {
            System.err.println("Usage: java sml.RunSml src/main/resources/test1.sml");
            return;
        }
        try {
            try {
                ApplicationContext context = new AnnotationConfigApplicationContext(SmlConfig.class);
                RunSml runner = context.getBean(RunSml.class);
                runner.run(args[0]);
                return;
            } catch (Exception e) {
                System.out.println("Spring initialisation has failed. Falling back to manual DI");
            }
            try {
                Constructor<RunSml> constructor = RunSml.class.getConstructor(Translator.class, Machine.class);
                Translator translator = new Translator();
                Machine machine = new Machine();
                RunSml runner = constructor.newInstance(translator, machine);
                runner.run(args[0]);
            } catch (Exception e) {
                RunSml.create().run(args[0]);
            }
        } catch (Exception e) {
            System.err.println("Error running program: " + e.getMessage());
            e.printStackTrace();
        }
    }
}