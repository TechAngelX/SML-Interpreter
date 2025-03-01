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


public class ConfigDiscoveryTest {
    private InstructionRegistry registry;
    private InstructionRegistrationLogger logger;

    @BeforeEach
    void setUp() {
        logger = new DefaultInstructionRegistrationLogger();
        registry = new InstructionRegistry();
    }

   
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

