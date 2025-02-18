package sml.instruction;

import sml.instruction.*;
import sml.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory that creates instruction objects using reflection.
 * This allows new instruction types to be added without modifying this class.
 *//**
 * A factory that dynamically discovers and creates instruction objects using reflection.
 */
public class InstructionFactory {
    private static final Map<String, Class<? extends Instruction>> instructionMap = new HashMap<>();

    static {
        instructionMap.put(SquareRootInstruction.OP_CODE, SquareRootInstruction.class);  // Just my test case.
        instructionMap.put(MultiplyInstruction.OP_CODE, MultiplyInstruction.class);
        instructionMap.put(AddInstruction.OP_CODE, AddInstruction.class);

    }

    /**
     * Creates an instruction based on the opcode and label.
     * @param opcode the opcode to identify the instruction
     * @param label the label associated with the instruction
     * @return an Instruction instance, or null if the opcode is unknown
     */
    public static Instruction createInstruction(String opcode, Label label) {
        Class<? extends Instruction> instructionClass = instructionMap.get(opcode);

        if (instructionClass == null) {
            return null;  // No matching instruction found
        }

        try {
            // Use the constructor of the instruction class to create an instance
            return instructionClass.getConstructor(Label.class).newInstance(label);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Handle exception if something goes wrong during instantiation
        }
    }
}
