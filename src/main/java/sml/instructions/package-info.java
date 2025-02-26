/**
 * The {@code sml.instructions} package defines the instruction set for the Simple Machine Language.
 * <p>
 * It includes arithmetic, branching, and stack-based operations. Each instruction extends
 * {@link sml.instructions.Instruction} and implements specific behavior within the SML execution model.
 * <p>
 * Notable instructions:
 * <ul>
 *     <li>{@link sml.instructions.AddInstruction} - Adds two values from the stack.</li>
 *     <li>{@link sml.instructions.DivInstruction} - Performs integer division.</li>
 *     <li>{@link sml.instructions.GotoInstruction} - Jumps to a specified label.</li>
 *     <li>{@link sml.instructions.InvokeInstruction} - Calls a method.</li>
 *     <li>{@link sml.instructions.LoadInstruction} - Loads a variable from memory.</li>
 *     <li>{@link sml.instructions.StoreInstruction} - Stores a value into a variable.</li>
 *     <li>{@link sml.instructions.ReturnInstruction} - Method termination operation.</li>
 * </ul>
 * <p>
 * This package forms the executable core of an SML program.
 * <p>
 *   Also includes two supplementary test classes, additional to the coursework assignment:
 * <ul>
 *     <li>{@link sml.instructions.NotEqInstruction} - Comparison instruction.</li>
 *     <li>{@link sml.instructions.SqrtInstruction} - Square root calculation instruction.</li>
 * </ul>
 *
 * @author Ricki Angel
 */
package sml.instructions;
