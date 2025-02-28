package sml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import sml.discovery.ConfigDiscovery;
import sml.discovery.InstructionDiscoveryStrategy;
import sml.discovery.PackageScanDiscovery;
import sml.helperfiles.InstructionRegistrationLogger;
import sml.instructions.Instruction;
import sml.registry.InstructionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

/**
 * A dynamic Factory for creating SML instructions.
 *
 * <h2>Discovery Strategies</h2>
 * <p>The InstructionRegistrationManager supports two primary strategies for discovering instructions:</p>
 * <ol>
 *   <li><strong>Configuration File Discovery</strong>: 
 *       Reads instruction mappings from a properties file, allowing dynamic registration 
 *       without modifying code.</li>
 *   <li><strong>Package Scanning</strong>: 
 *       Automatically detects instruction classes within a specified package, 
 *       using reflection to identify and register available instructions.</li>
 * </ol>
 *
 * <h2>Key Features</h2>
 * <ul>
 *   <li>Dynamic instruction registration without code modifications</li>
 *   <li>Extensible discovery mechanisms</li>
 *   <li>Centralised instruction management</li>
 *   <li>Logging and tracking of instruction registration processes</li>
 *   <li>Fail-fast mechanism for instruction discovery</li>
 * </ul>
 *
 * <h2>Delegation Model</h2>
 * <p>This factory delegates core functionality to specialised components:</p>
 * <ul>
 *   <li>Storage and creation of instructions is handled by {@link InstructionRegistry}</li>
 *   <li>Discovery of instructions is performed by {@link InstructionDiscoveryStrategy} implementations</li>
 * </ul>
 *
 * @author Ricki Angel
 */
@Component("InstructionFactory")
@Qualifier
public class InstructionRegistrationManager {
    private static final Logger LOGGER = Logger.getLogger(InstructionRegistrationManager.class.getName());
    private static final InstructionRegistry REGISTRY = new InstructionRegistry();

    private final InstructionRegistrationLogger logger;
    private final List<InstructionDiscoveryStrategy> discoveryMethods = new ArrayList<>();

    static {
        configureLoggerFormat();
        initialiseFactory();
    }

    /**
     * Configures console log handlers to provide cleaner, simplified output.
     *
     * <p>Replaces default log formatting with a streamlined message-only display.</p>
     */
    private static void configureLoggerFormat() {
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setFormatter(new SimpleFormatter() {
                    @Override
                    public synchronized String format(LogRecord record) {
                        return record.getMessage() + System.lineSeparator();
                    }
                });
            }
        }
    }

    /**
     * Initialises the static factory with discovery methods.
     *
     * <p>Discovers and registers available instructions, then prints a registration summary.</p>
     */
    private static void initialiseFactory() {
        InstructionRegistrationLogger staticLogger =
                new sml.helperfiles.DefaultInstructionRegistrationLogger();
        InstructionRegistrationManager factory = new InstructionRegistrationManager(staticLogger);
        factory.discoverInstructions();
        staticLogger.printRegistrationSummary();
    }

    /**
     * Constructs a new InstructionRegistrationManager.
     *
     * @param logger The logger for tracking instruction registration
     */
    @Autowired
    public InstructionRegistrationManager(InstructionRegistrationLogger logger) {
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null");

        discoveryMethods.add(new ConfigDiscovery(logger));
        discoveryMethods.add(new PackageScanDiscovery(logger));
    }

    /**
     * Discovers instructions using the configured discovery methods.
     * <p>
     * Attempts each method in sequence until successful.
     * </p>
     */
    public void discoverInstructions() {
        LOGGER.log(Level.INFO, "Starting instruction discovery process");

        boolean success = false;
        for (InstructionDiscoveryStrategy method : discoveryMethods) {
            int discovered = method.discoverInstructions(REGISTRY);
            if (discovered > 0) {
                LOGGER.log(Level.INFO, "Successfully discovered " + discovered +
                        " instructions using " + method.getName() + " method");
                success = true;
                break;
            }
        }

        if (!success) {
            LOGGER.log(Level.SEVERE, "Failed to discover any instructions using all methods");
            throw new RuntimeException("No instructions could be discovered");
        }
    }

    /**
     * Creates an instruction based on the given opcode and label.
     * <p>
     * This is the primary method for instantiating instructions.
     * </p>
     *
     * @param opcode The operation code identifying the instruction type
     * @param label  The label associated with the instruction
     * @return A new Instruction instance, or null if creation fails
     */
    public static Instruction createInstruction(String opcode, Label label) {
        return REGISTRY.createInstruction(opcode, label);
    }
}
