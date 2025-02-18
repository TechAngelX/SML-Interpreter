package sml.instruction;

import sml.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * A factory that creates instruction objects using reflection.
 * This allows new instruction types to be added without modifying this class.
 *//**
 * A factory that dynamically discovers and creates instruction objects using reflection.
 */
public class InstructionFactory {
    private static final String INSTRUCTION_PKG = "sml.instruction";
    private static final Map<String, Class<?>> instructionTypes = new HashMap<>();

    static {
        loadInstructionClasses(); //TODO Create class to dynamically load all instructuiiins at once.
    }

    /**
     * Creates an instruction based on opcode and arguments.
     */
    public static Instruction createInstruction(String opcode, Label label, Object... args) {
        try {
            Class<?> instructionClass = instructionTypes.get(opcode);
            if (instructionClass == null) {
                return null;
            }

            Constructor<?>[] constructors = instructionClass.getConstructors();
            if (constructors.length == 0) return null;

            Object[] params = new Object[args.length + 1];
            params[0] = label;
            System.arraycopy(args, 0, params, 1, args.length);

            for (Constructor<?> constructor : constructors) {
                if (isCompatible(constructor, params)) {
                    return (Instruction) constructor.newInstance(params);
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error creating the instruction: " + e.getMessage());
            return null;
        }
    }


    /**
     * Checks if a constructor is compatible with given parameters.
     */
    private static boolean isCompatible(Constructor<?> constructor, Object[] params) {
        Class<?>[] ctorTypes = constructor.getParameterTypes();
        if (ctorTypes.length != params.length) return false;

        for (int i = 0; i < ctorTypes.length; i++) {
            if (params[i] != null && !ctorTypes[i].isAssignableFrom(params[i].getClass())) {
                return false;
            }
        }
        return true;
    }
}
