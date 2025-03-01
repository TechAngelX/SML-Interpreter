/**
 * Provides the core components for the Simple Machine Language (SML) program.
 *
 * <p>Contains essential classes for executing SML instructions, managing program flow, 
 * and handling runtime environment for a stack-based virtual machine.</p>
 *
 * <h2>Key Components:</h2>
 * <ul>
 *     <li>{@link sml.Machine} - The primary execution environment</li>
 *     <li>{@link sml.InstructionRegistrationManager} - Dynamically discovers and creates instruction instances</li>
 *     <li>{@link sml.Translator} - Parses source files and converts them into executable instructions</li>
 *     <li>{@link sml.Variable} - Manages storage for integer values</li>
 *     <li>{@link sml.Label} - Handles jump and method identification destinations</li>
 *     <li>{@link sml.Frame} - Maintains execution state, tracking stack and variables for method contexts</li>
 * </ul>
 *
 * <p>The {@code sml.instructions} subpackage provides a comprehensive set of 
 * arithmetic, branching, and method invocation instructions that power the SML runtime.</p>
 *
 * @author Ricki Angel
 */
package sml;
