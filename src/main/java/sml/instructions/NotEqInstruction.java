package sml.instructions;

import sml.*;
/**
 * =====================================================================================
 * NotEqInstruction - Test Instruction for Package Scanning Dynamic Discovery.
 * -------------------------------------------------------------------------------------
 * This instruction is specifically created for testing the dynamic opcode discovery
 * {@code discoverByPackageScanning} mechanism of the InstructionFactory. It performs a
 * no-op operation during execution. The logic within this instruction is irrelevant
 * for the test; its primary purpose is to verify that the factory can successfully
 * discover and instantiate this instruction without modifying its code.
 *
 * The {@code doExecute} method defines the instruction's core operational logic.
 * @author Ricki Angel
 */
public class NotEqInstruction extends Instruction {
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
