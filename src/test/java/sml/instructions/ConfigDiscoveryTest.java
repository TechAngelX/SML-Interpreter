package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.discovery.ConfigDiscovery;
import sml.helperfiles.DefaultInstructionRegistrationLogger;
import sml.helperfiles.InstructionRegistrationLogger;
import sml.registry.InstructionRegistry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ConfigDiscovery}.
 * Verifies the functionality of instruction discovery from configuration files,
 * handling various scenarios of configuration file loading and instruction registration.
 *
 * <p>Author: Ricki Angel</p>
 */
public class ConfigDiscoveryTest {
    private InstructionRegistry registry;
    private InstructionRegistrationLogger logger;

    @BeforeEach
    void setUp() {
        logger = new DefaultInstructionRegistrationLogger();
        registry = new InstructionRegistry();
    }

    /**
     * Verifies that the ConfigDiscovery can successfully discover and register instructions
     * from a test properties file created in memory. The test creates a mock configuration
     * with three instructions (add, sub, mul) and checks that they are correctly registered
     * in the instruction registry.
     */

    @Test
    @DisplayName("Should discover instructions from a configuration file")
    void testDiscoverInstructions() {
        Properties testProperties = new Properties();
        testProperties.setProperty("print", "sml.instructions.PrintInstruction");
        testProperties.setProperty("add", "sml.instructions.AddInstruction");
        testProperties.setProperty("sub", "sml.instructions.SubInstruction");

        ConfigDiscovery configDiscovery = new ConfigDiscovery(logger) {
            @Override
            protected InputStream getConfigResource(String configFile) {
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    testProperties.store(out, "Test Properties");
                    return new ByteArrayInputStream(out.toByteArray());
                } catch (IOException e) {
                    return null;
                }
            }
        };

        int discoveredCount = configDiscovery.discoverInstructions(registry);
        assertTrue(registry.isRegistered("print"), "Print instruction should be registered");
        assertTrue(registry.isRegistered("add"), "add instruction should be registered");
        assertTrue(registry.isRegistered("sub"), "sub instruction should be registered");
    }

    /**
     * Tests the behavior of ConfigDiscovery when no configuration file is available.
     * Simulates a missing configuration file scenario and ensures that the discovery
     * method gracefully handles this situation by returning zero discovered instructions.
     */

    @Test
    @DisplayName("Should handle missing configuration file gracefully")
    void testMissingConfigurationFile() {
        ConfigDiscovery configDiscovery = new ConfigDiscovery(logger) {
            @Override
            protected InputStream getConfigResource(String configFile) {
                return null;
            }
        };
        int discoveredCount = configDiscovery.discoverInstructions(registry);
        assertEquals(0, discoveredCount, "Should return a 0 when config file is missing");
    }
    /**
     * Examines the ConfigDiscovery's response to an empty configuration file.
     * Ensures that when a configuration file exists but contains no instruction mappings,
     * the discovery method returns zero and does not attempt to register any instructions.
     */

    @Test
    @DisplayName("Should handle empty configuration file")
    void testEmptyConfigurationFile() {
        ConfigDiscovery configDiscovery = new ConfigDiscovery(logger) {
            @Override
            protected InputStream getConfigResource(String configFile) {
                Properties emptyProps = new Properties();
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    emptyProps.store(out, "Empty Properties");
                    return new ByteArrayInputStream(out.toByteArray());
                } catch (IOException e) {
                    return null;
                }
            }
        };
        int discoveredCount = configDiscovery.discoverInstructions(registry);
        assertEquals(0, discoveredCount, "Should return 0 when config file is empty");
    }
}
