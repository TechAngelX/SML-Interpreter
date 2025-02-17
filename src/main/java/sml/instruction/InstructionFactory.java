package sml.instruction;

import sml.Instruction;
import sml.Label;
import sml.Variable;

public class InstructionFactory {
    /**
     * Creates an instruction based on the given opcode and parameters.
     *
     */
    public static Instruction createInstruction(String opcode, Label label, Object... args) {
        return switch (opcode) {
            case PushInstruction.OP_CODE -> {
                if (args.length != 1 || !(args[0] instanceof Integer value)) {
                    throw new IllegalArgumentException("Push instruction needs exactly one integer argument.");
                }
                yield new PushInstruction(label, value);
            }
            case StoreInstruction.OP_CODE -> {
                if (args.length != 1 || !(args[0] instanceof Variable.Identifier identifier)) {
                    throw new IllegalArgumentException("Store instruction needs a Variable.Identifier as an argument.");
                }
                yield new StoreInstruction(label, identifier);
            } ...

        // TODO ...