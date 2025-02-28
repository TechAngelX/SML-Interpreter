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
    private static final String CONFIG_FILE = "config/instruction/opcode.properties";

    private final InstructionRegistrationLogger logger;

 
    public ConfigDiscovery(InstructionRegistrationLogger logger) {
        this.logger = logger;
    }

    @Override
    public int discoverInstructions(InstructionRegistry registry) {
        // ...
        }
}
