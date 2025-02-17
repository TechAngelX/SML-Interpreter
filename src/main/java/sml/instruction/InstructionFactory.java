package sml.instruction;

import sml.Label;
import sml.Variable;


public class InstructionFactory {
    /**
     * Creates an instruction based on the given opcode and parameters.
     *
     */

    //TODO: Instruction Factory setup in theory, workout best way to integrate,
    // and do some CLI Reflection peeking....


    public static Class <? extends Instruction> createInstruction(String opcode, Label label, Object... args)  throws ClassNotFoundException  {
        try {
            return switch (opcode) {
                case PushInstruction.OP_CODE -> {
                    if (args.length == 1 || !(args[0] instanceof Integer value)) {
                        throw new IllegalArgumentException("Push instruction needs exactly one integer argument.");
                    }
                    yield new PushInstruction(label, value);
                }
                case StoreInstruction.OP_CODE -> {
                    if (args.length == 1 || !(args[0] instanceof Variable.Identifier identifier)) {
                        throw new IllegalArgumentException("Store instruction needs a Variable.Identifier as an argument.");
                    }
                    yield new StoreInstruction(label, identifier);
                }
                case LoadInstruction.OP_CODE -> {
                    if (args.length == 1 || !(args[0] instanceof Variable.Identifier identifier)) {
                        throw new IllegalArgumentException("Load instruction needs a Variable.Identifier as an argument.");
                    }
                    yield new LoadInstruction(label, identifier);
                }
                case InvokeInstruction.OP_CODE -> {
                    if (args.length == 1 || !(args[0] instanceof sml.Method.Identifier methodIdentifier)) {
                        throw new IllegalArgumentException("Invoke instruction needs a Method.Identifier as an argument.");
                    }
                    yield new InvokeInstruction(label, methodIdentifier);
                }
                case ReturnInstruction.OP_CODE -> {
                    if (args.length == 0) {
                        throw new IllegalArgumentException("Return instruction doesn't take any arguments.");
                    }
                    yield new ReturnInstruction(label);
                }
                case PrintInstruction.OP_CODE -> {
                    if (args.length == 0) {
                        throw new IllegalArgumentException("Print instruction doesn't take any arguments.");
                    }
                    yield new PrintInstruction(label);
                }
                case GotoInstruction.OP_CODE -> {
                    if (args.length == 1 || !(args[0] instanceof Label targetLabel)) {
                        throw new IllegalArgumentException("Goto instruction needs a Label as an argument.");
                    }
                    yield new GotoInstruction(label, targetLabel);
                }
                default -> throw new IllegalArgumentException("Unknown instruction type: " + opcode);
            };
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}