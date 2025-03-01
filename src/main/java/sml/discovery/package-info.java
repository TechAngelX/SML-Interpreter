/**
 * Provides discovery mechanisms for dynamically loading SML instructions at runtime.
 * <p>
 * This package contains strategy implementations for discovering and registering 
 * instruction classes in the SML virtual machine. It enables extensibility through:
 * <ul>
 *   <li>{@link sml.discovery.ConfigDiscovery} - Loads instructions from configuration files</li>
 *   <li>{@link sml.discovery.PackageScanDiscovery} - Discovers instructions by scanning package directories</li>
 *   <li>{@link sml.discovery.InstructionDiscoveryStrategy} - Common interface for discovery implementations</li>
 * </ul>
 * </p>
 * <p>
 * These discovery mechanisms allow the SML system to be extended with new instructions
 * without modifying the core codebase. New instructions can be added by:
 * <ol>
 *   <li>Creating a new class that extends {@link sml.instructions.Instruction}</li>
 *   <li>Adding configuration entries or placing the class in the appropriate package</li>
 * </ol>
 * </p>
 * <p>
 * The discovery system supports dependency injection through Spring Framework
 * and provides detailed logging of the discovery process.
 * </p>
 *
 * @author Ricki Angel
 * @see sml.registry.InstructionRegistry
 * @see sml.instructions.Instruction
 */
package sml.discovery;
