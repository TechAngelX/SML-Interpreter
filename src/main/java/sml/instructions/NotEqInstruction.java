package sml.instructions;

import sml.*;
/**
 * =====================================================================================
 * NotEqInstruction - Test Instruction for Package Scanning Dynamic Discovery.
 * -------------------------------------------------------------------------------------
 * This is a mock instruction, specifically created to test the dynamic opcode discovery
 * {@code discoverByPackageScanning} mechanism of the InstructionFactory. It supposedly pops
 * two values and prints first value to console if they are NOT equal. The logic within this
 * instruction is irrelevant and not implemented for the test; its primary purpose is to verify
 * that the factory can successfully discover and instantiate this instruction without modifying
 * its code.
 *
 * @author Ricki Angel
 */
public non-sealed class NotEqInstruction extends Instruction {
    public static final String OP_CODE = "not_eq";

    public NotEqInstruction(Label label) {
        super(label, OP_CODE);
    }

        @Override
    protected void doExecute(Frame frame) {
        // No-op for testing. Logic does not matter here.
        // We're testing if the Factory discovers this new Instruction.
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
