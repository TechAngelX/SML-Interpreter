package sml.discovery;

import sml.helperfiles.InstructionRegistrationLogger;
import sml.instructions.Instruction;
import sml.registry.InstructionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Discovers and registers SML instructions dynamically from a configuration file.
 *
 * <p>This class implements a strategy for runtime instruction discovery by:</p>
 * <ul>
 *   <li>Reading instruction mappings from a properties file</li>
 *   <li>Validating and registering instruction classes</li>
 *   <li>Supporting extensible instruction set configuration</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class ConfigDiscovery implements InstructionDiscoveryStrategy {
    private static final Logger LOGGER = Logger.getLogger(ConfigDiscovery.class.getName());
    private static final String CONFIG_FILE = "sml/config/opcode.properties";
    private final InstructionRegistrationLogger logger;

    /**
     * Constructs a new ConfigDiscovery discovery method.
     *
     * @param logger The logger to use for tracking registration events.
     */
    public ConfigDiscovery(InstructionRegistrationLogger logger) {
        this.logger = logger;
    }

    /**
     * Gets the configuration resource as an input stream.
     *
     * @param configFile The path to the configuration file
     * @return An input stream for the resource, or null if not found
     */
    protected InputStream getConfigResource(String configFile) {
        return getClass().getClassLoader().getResourceAsStream(configFile);
    }

    /**
     * Discovers and registers instructions from the configuration file.
     *
     * <p>Attempts to load instruction mappings from a properties file, 
     * validating and registering each instruction class found.</p>
     *
     * @param registry The instruction registry to populate with discovered instructions
     * @return Number of successfully registered instructions
     * @throws RuntimeException if there are issues reading the configuration file
     */
    @Override
    public int discoverInstructions(InstructionRegistry registry) {

        LOGGER.log(Level.INFO, "Discovering instructions from configuration file: " + CONFIG_FILE);
        int initialSize = registry.size();

        try (InputStream input = getConfigResource(CONFIG_FILE)) {
            if (input == null) {
                LOGGER.log(Level.WARNING, "Configuration file not found: " + CONFIG_FILE);
                return 0;
            }

            Properties opProperties = new Properties();
            opProperties.load(input);

            if (opProperties.isEmpty()) {
                LOGGER.log(Level.WARNING, "Configuration file is empty: " + CONFIG_FILE);
                return 0;
            }

            int registered = 0;
            for (String opcode : opProperties.stringPropertyNames()) {
                String className = opProperties.getProperty(opcode);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (registerClass(registry, clazz, opcode)) {
                        registered++;
                    }
                } catch (ClassNotFoundException e) {
                    logger.trackFailedRegistration(className, "Class not found");
                    LOGGER.log(Level.WARNING, "Class not found: " + className);
                }
            }

            LOGGER.log(Level.INFO, "Registered " + registered + " instructions from configuration file");
            return registered;

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error loading configuration file: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Validates and registers a single instruction class into the instruction registry.
     *
     * <p>This method performs a comprehensive validation of instruction classes before registration, 
     * ensuring only valid and usable instruction implementations are added to the registry.</p>
     *
     * <p>Validation criteria include:</p>
     * <ul>
     *   <li>Must be a subclass of {@link Instruction}</li>
     *   <li>Must not be an abstract class</li>
     *   <li>Must be able to be registered with the provided opcode</li>
     * </ul>
     *
     * <p>Registration process:</p>
     * <ul>
     *   <li>Logs initial registration attempt</li>
     *   <li>Performs type and modifiability checks</li>
     *   <li>Registers successful instruction classes</li>
     *   <li>Tracks registration outcomes</li>
     * </ul>
     *
     * <p>Usage:</p>
     * <ol>
     *   <li>Add a new opcode name and instruction class to the {@code /sml/config/opcode.properties} file in the format: {@code opcode=sml.instructions.InstructionClass}</li>
     *   <li>Create the instruction class and place it in the {@code sml.instructions} package. Ensure that it extends the {@link Instruction} interface.</li>
     *   <li>The new instruction will be automatically discovered and registered during the application startup.</li>
     * </ol>
     *
     * <p>Example {@code opcode.properties} entry:</p>
     * <pre>
     * sqrt=sml.instructions.SqrtInstruction
     * </pre>
     *
     * <p>Visual representation of the registration process:</p>
     * <p>
     * {@code <img src="path/to/registration-process.jpg" alt="Instruction registration process">}
     * </p>
     *
     * @param registry The instruction registry where the class will be potentially registered
     * @param clazz The instruction class candidate for registration
     * @param configOpcode The operation code associated with the instruction
     * @return {@code true} if the instruction class passes validation and is successfully registered,
     *         {@code false} if validation fails or registration cannot be completed
     *
     * @see InstructionRegistry
     * @see Instruction
     */
    private boolean registerClass(InstructionRegistry registry, Class<?> clazz, String configOpcode) {
        logger.logRegistrationAttempt(clazz);

        Predicate<Class<?>> isInstruction = c -> Instruction.class.isAssignableFrom(c);
        Predicate<Class<?>> isNotAbstract = c -> !Modifier.isAbstract(c.getModifiers());

        return Optional.of(clazz)
                .filter(isInstruction)
                .filter(isNotAbstract)
                .map(c -> (Class<? extends Instruction>) c)
                .map(instructionClass -> {
                    registry.register(configOpcode, instructionClass);
                    logger.trackSuccessfulRegistration(clazz.getSimpleName(), configOpcode);
                    return true;
                })
                .orElseGet(() -> {
                    logger.trackFailedRegistration(clazz.getSimpleName(),
                            !isInstruction.test(clazz)
                                    ? "Not an Instruction subclass"
                                    : "Abstract class");
                    return false;
                });
    }
    @Override
    public String getName() {
        return "Configuration File";
    }
}
