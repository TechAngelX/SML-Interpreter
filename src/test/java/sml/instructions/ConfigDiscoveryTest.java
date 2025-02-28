package sml.discovery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.helperfiles.DefaultInstructionRegistrationLogger;
import sml.helperfiles.InstructionRegistrationLogger;
import sml.registry.InstructionRegistry;

import static org.mockito.Mockito.*;

class ConfigDiscoveryTest {
    private InstructionRegistry registry;
    private InstructionRegistrationLogger logger;
    private ConfigDiscovery configDiscovery;

    @BeforeEach
    void setUp() {
        logger = new DefaultInstructionRegistrationLogger();
        configDiscovery = new ConfigDiscovery(logger);
        registry = new InstructionRegistry();
    }

    @Test
    @DisplayName("Should discover instructions from configuration file")
    void testDiscoverInstructions() {
        int discoveredCount = configDiscovery.discoverInstructions(registry);

        // TODO...
}
