package sml.discovery;

import sml.helperfiles.InstructionRegistrationLogger;
import sml.instructions.Instruction;
import sml.registry.InstructionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
    private static final String CONFIG_FILE = "config/opcode.properties";

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

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
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
     * <p>Performs checks to ensure the class is a valid instruction before registration.</p>
     *
     * @param registry The instruction registry to register the class in
     * @param clazz The instruction class to be registered
     * @param configOpcode The opcode associated with the instruction
     * @return True if registration was successful, false if validation failed
     */
    private boolean registerClass(InstructionRegistry registry, Class<?> clazz, String configOpcode) {
        logger.logRegistrationAttempt(clazz);

        if (!Instruction.class.isAssignableFrom(clazz)) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Not an Instruction subclass");
            return false;
        }

        if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Abstract class");
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Instruction> instructionClass = (Class<? extends Instruction>) clazz;
            registry.register(configOpcode, instructionClass);
            logger.trackSuccessfulRegistration(clazz.getSimpleName(), configOpcode);
            return true;
        } catch (Exception e) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Registration error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getName() {
        return "Configuration File";
    }
}
