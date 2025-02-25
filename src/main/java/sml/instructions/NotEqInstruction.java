package sml.instructions;

import sml.*;

/**
 * Not Equal instruction for the SML instruction set (supplementary).
 *
 * <p>This class is an additional test mock-up instruction, designed to be intentionally non-functional and serves solely as a test case for the dynamic instruction discovery system.
 * It is a mock instruction that would jump somewhere if two values popped were not equal. This is supplementary
 * to the SDP coursework assignment, designed purely to enhance my familiarity with opcode and instruction discovery.</p>
 *
 * <p>This mock instruction's purpose is to:</p>
 * <ul>
 *   <li>Validate package scanning capabilities of the InstructionFactory</li>
 *   <li>Demonstrate instruction discovery without factory code modifications</li>
 *   <li>Test runtime extension of available instructions</li>
 * </ul>
 *
 * <p>Note: The instruction does not implement any actual comparison logic.
 * Its doExecute() method is deliberately empty as the class exists purely for testing
 * the discovery mechanism, not for executing meaningful operations.</p>
 *
 * @author Ricki Angel
 */
public non-sealed class NotEqInstruction extends Instruction {
    public static final String OP_CODE = "not_eq";
    /**
     * Constructs a new NotEqInstruction with the specified label.
     *
     * <p>Initialises the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The not-equal operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public NotEqInstruction(Label label) {
        super(label, OP_CODE);
    }
    /**
     * Executes the instruction's primary operation.
     *
     * <p>This implementation is intentionally empty as the class
     * exists purely for testing instruction discovery mechanisms.</p>
     *
     * @param frame The current execution frame
     */

        @Override
    protected void doExecute(Frame frame) {
        // No-op for testing. Logic does not matter here.
        // We're testing if the Factory discovers this new Instruction.
    }
    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>This test instruction has no operands.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
