package sml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collection;
/**
 * The RunSml class serves as the main entry point for executing Simple Machine Language (SML) programs.
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
 * <h2>Command Line Usage:</h2>
 * <pre>
 *   # Set classpath
 *   export CLASSPATH="/path/to/project/target/classes/"
 *
 *   # Compile (from project root)
 *   javac -d target/classes src/main/java/sml/*.java src/main/java/sml/instructions/*.java
 *
 *   # Run
 *   java sml.RunSml src/main/resources/test1.sml
 * </pre>
 *
 * @author Ricki Angel
 * @see Translator
 * @see Machine
 * @see Method
 */

@Service
public class RunSml {
    try {
        ApplicationContext contexr =

    private final Translator translator;
    private final Machine machine;

    @Autowired
    public RunSml(Translator translator, Machine machine) {
        this.translator = translator;
        this.machine = machine;
    }

    public void run(String filename) throws IOException {
        Collection<Method> instructions = translator.readAndTranslate(filename);
        machine.setProgram(instructions);
        machine.execute();
    }
}