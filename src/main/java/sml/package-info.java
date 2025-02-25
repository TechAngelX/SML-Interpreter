/**
 * Provides the core components for the Simple Machine Language program.
 * <p>
 * It contains classes for executing SML instructions, managing program flow, and handling variables.
 * Key components include:
 * <ul>
 *     <li>{@link sml.Machine} - \The main execution environment.</li>
 *     <li>{@link sml.InstructionFactory} - Dynamically creates instruction instances.</li>
 *     <li>{@link sml.Translator} - Converts source files into executable instructions.</li>
 *     <li>{@link sml.Variable} - Represents a storage unit for integer values.</li>
 *     <li>{@link sml.Label} - Manages jump destinations.</li>
 *     <li>{@link sml.Frame} - Maintains execution state, including the stack and variables.</li>
 * </ul>
 * <p>
 * The {@code sml.instructions} subpackage defines arithmetic, branching, and method invocation instructions.
 *
 * @author Ricki Angel
 */
package sml;
