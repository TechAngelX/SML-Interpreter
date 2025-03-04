package sml.instructions;

import sml.Frame;
import sml.Label;

public class NumCharInstruction extends Instruction {
    public static final String OP_CODE = "num_char";

    /**
     * Represents the NumChar instruction in the Simple Machine Language.
     *
     * <p>This class is an additional test mock-up instruction designed as a test case
     * for the dynamic instruction discovery system (package scan). It's deliberately not
     * included in the opcode_properties config file. This instruction is supplementary 
     * to the SDP coursework assignment, designed purely to enhance my familiarity with
     * opcode and instruction discovery.</p> 
     *
     * <p>The NumCharInstruction converts a number to its corresponding alphabetic character,
     * prints the result, and pushes the character's ASCII value onto the stack.
     * It extends the base {@link Instruction} class and provides the implementation
     * for executing the instruction within the SML runtime.</p>
     *
     * <p>Key responsibilities:</p>
     * <ul>
     *   <li>Popping a number from the stack</li>
     *   <li>Converting the number to its corresponding alphabetic character</li>
     *   <li>Printing the resulting character</li>
     *   <li>Pushing the character's ASCII value onto the stack</li>
     * </ul>
     *
     * <p>Execution behavior:</p>
     * <ol>
     *   <li>Retrieves the top value from the stack, expecting it to be a number</li>
     *   <li>Converts the number to its corresponding alphabetic character using the formula:
     *       <code>'A' + number - 1</code></li>
     *   <li>Prints the resulting character to the console</li>
     *   <li>Pushes the character's ASCII value onto the stack</li>
     * </ol>
     *
     * <p>Example usage:</p>
     * <pre>
     *   // Create a NumCharInstruction with an optional label
     *   Instruction numCharInstruction = new NumCharInstruction(new Label("L1"));
     *
     * @author Ricki Angel
     */
    public NumCharInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the NumChar instruction.

     * Pops a number from the stack, converts it to the corresponding alphabetic character,
     * prints the character, and pushes the character's ASCII value onto the stack.
     *
     * @param frame the execution frame
     */
    @Override
    protected void performInstructionLogic(Frame frame) {
        int number = frame.pop();
        char letter = (char) ('A' + number -1);
        System.out.println(letter);
        frame.push((int)letter);

    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
