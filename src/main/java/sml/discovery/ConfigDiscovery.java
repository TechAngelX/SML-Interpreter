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
public class ConfigDiscovery implements Discoverable {
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
                    if (registry, clazz, opcode)) {
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

}
