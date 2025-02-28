package sml.discovery;

import sml.registry.InstructionRegistry;

/**
 * Strategy interface for discovering instruction classes.
 * <p>
 * Implementations of this interface provide different ways to discover
 * and register SML instruction classes.
 * </p>
 *
 * @author Ricki Angel
 */
public interface InstructionDiscoveryStrategy {

    /**
     * Strategy interface for discovering instruction classes.
     * <p>
     * Implementations of this interface provide different ways to discover
     * and register SML instruction classes. The interface defines the contract
     * for discovering instructions at runtime and registering them with the
     * provided registry.
     * </p>
     *
     * <p>
     * This interface is implemented by two concrete strategies:
     * <ul>
     *   <li>ConfigDiscovery - Discovers instructions from configuration properties file</li>
     *   <li>PackageScanDiscovery - Discovers instructions by scanning package directories</li>
     * </ul>
     * </p>
     *
     * @author Ricki Angel
     */
    int discoverInstructions(InstructionRegistry registry);

    /**
     * Returns the name of this discovery method.
     *
     * @return A descriptive name for this method
     */
    String getName();
}
