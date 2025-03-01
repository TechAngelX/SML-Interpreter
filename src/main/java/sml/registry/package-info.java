/**
 * Provides registry mechanisms for managing instruction creation and lookup.
 * <p>
 * This package contains the registry system that maintains opcode-to-instruction mappings:
 * <ul>
 *   <li>{@link sml.registry.InstructionRegistry} - Maintains a mapping of opcodes to instruction classes
 *       and provides methods for instruction instantiation</li>
 * </ul>
 * </p>
 * <p>
 * The registry system serves as a centralised repository for instruction class information,
 * enabling:
 * <ul>
 *   <li>Dynamic registration of instructions at runtime</li>
 *   <li>Opcode-based instruction instantiation</li>
 *   <li>Verification of instruction availability</li>
 *   <li>Managing instruction creation with appropriate parameters</li>
 * </ul>
 * </p>
 * <p>
 * The registry works in conjunction with the discovery system to provide a complete
 * solution for instruction lifecycle management in the SML virtual machine.
 * </p>
 *
 * @author Ricki Angel
 * @see sml.discovery.InstructionDiscoveryStrategy
 * @see sml.InstructionRegistrationManager
 */
package sml.registry;
