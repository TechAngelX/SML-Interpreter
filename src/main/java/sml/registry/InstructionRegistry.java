package sml.registry;

import sml.Label;
import sml.instructions.Instruction;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registry of available SML instruction classes.
 * <p>
 * Maintains a mapping of opcodes to their corresponding instruction classes
 * and provides methods for instruction instantiation.
 * </p>
 *
 * @author Ricki Angel
 */
public class InstructionRegistry {
    private static final Logger LOGGER = Logger.getLogger(InstructionRegistry.class.getName());
    private final Map<String, Class<? extends Instruction>> instructionMap = new HashMap<>();

    /**
     * Registers an instruction class with its corresponding opcode.
     *
     * @param opcode The opcode to register
     * @param instructionClass The instruction class to register
     */
    public void register(String opcode, Class<? extends Instruction> instructionClass) {
        instructionMap.put(opcode, instructionClass);
    }

    /**
     * Checks if an opcode is already registered.
     *
     * @param opcode The opcode to check
     * @return true if the opcode is registered, false otherwise
     */
    public boolean isRegistered(String opcode) {
        return instructionMap.containsKey(opcode);
    }

    /**
     * Returns the number of registered instructions.
     *
     * @return The count of registered instructions
     */
    public int size() {
        return instructionMap.size();
    }

    /**
     * Creates an instruction instance for the given opcode.
     *
     * @param opcode The opcode of the instruction to create
     * @param label The label for the instruction (can be null)
     * @return The created instruction, or null if creation fails
     */
    public Instruction createInstruction(String opcode, Label label) {
        Class<? extends Instruction> instructionClass = instructionMap.get(opcode);
        if (instructionClass == null) {
            LOGGER.log(Level.WARNING, "No instruction class found for opcode: " + opcode);
            return null;
        }

        try {
            Constructor<? extends Instruction> constructor = instructionClass.getConstructor(Label.class);
            return constructor.newInstance(label);
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.SEVERE, "No constructor with Label parameter found for opcode: " + opcode, e);
        } catch (ReflectiveOperationException e) {
            LOGGER.log(Level.WARNING, "Error creating instruction for opcode: " + opcode, e);
        }
        return null;
    }
}
