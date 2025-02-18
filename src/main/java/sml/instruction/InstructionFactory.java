package sml.instruction;

import sml.instruction.*;
import sml.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ====================================================================================================================
 * A factory that creates instruction objects dynamicaly using Reflection.
 * --------------------------------------------------------------------------------------------------------------------
 * This will eventually allow new instruction types to be added without modifying this class and
 * provide a centralised way to create instructions. Uses a static map to register the instruction types.
 *
 * ====================================================================================================================
 *
 * @author Ricki Angel
 */

public class InstructionFactory {
    private static final Map<String, Class<? extends Instruction>> instructionMap = new HashMap<>();

    static {
        instructionMap.put(SquareRootInstruction.OP_CODE, SquareRootInstruction.class);  // Just my bespoke test case.
        instructionMap.put(MultiplyInstruction.OP_CODE, MultiplyInstruction.class);
        instructionMap.put(AddInstruction.OP_CODE, AddInstruction.class);
        // ...
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
            return null;
        }

        try {
            return instructionClass.getConstructor(Label.class).newInstance(label);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Handle exception if something goes wrong during instantiation.
        }
    }
}