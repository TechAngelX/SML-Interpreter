package sml.instruction;

import sml.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * ====================================================================================================================
 * A factory that dynamically creates Instruction objects using Reflection and ServiceLoader.
 * --------------------------------------------------------------------------------------------------------------------
 * This class discovers and registers Instruction classes within the same package, storing them in a HashMap,
 * called instructionMap, allowing for the creation of Instruction instances based on their opcode, facilitating
 * a pluggable architecture where new Instruction types can be added without modifying the factory itself.
 * --------------------------------------------------------------------------------------------------------------------
 * The `INSTRUCTION_MAP` static map holds the relationship between an opcode and a generic wildcard
 * `Class<?>` that extends `Instruction`.
 * ====================================================================================================================
 *
 * @author Ricki Angel
 */

public class InstructionFactory {
    private static final Map<String, Class<? extends Instruction>> INSTRUCTION_MAP = new HashMap<>();

    static {
        initiateInstructionMap();
    }

    private static void initiateInstructionMap() {

        boolean discovered = tryServiceLoaderDiscovery();

        if (!discovered) {
            discovered = tryPackageScanningDiscovery();
        }

        if (!discovered) {
            discovered = tryDirectClassLoadingDiscovery();
        }

        if (!discovered || INSTRUCTION_MAP.isEmpty()) {
            throw new RuntimeException("Failed to register any instruction classes");
        }
    }

    private static boolean tryServiceLoaderDiscovery() {
        try {
            ServiceLoader<Instruction> loader = ServiceLoader.load(Instruction.class);
            boolean found = false;

            for (Instruction instruction : loader) {
                Class<?> clazz = instruction.getClass();
                registerInstructionClass(clazz);
                found = true;
            }

            return found;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryPackageScanningDiscovery() {
        try {
            Package instructionPackage = Instruction.class.getPackage();
            String packageName = instructionPackage.getName();

            String packagePath = packageName.replace('.', '/');
            ClassLoader classLoader = InstructionFactory.class.getClassLoader();
            java.net.URL packageURL = classLoader.getResource(packagePath);

            if (packageURL != null) {
                java.io.File packageDir = new java.io.File(packageURL.getFile());
                java.io.File[] classFiles = packageDir.listFiles(file -> file.isFile() && file.getName().endsWith(".class"));

                if (classFiles != null && classFiles.length > 0) {
                    boolean found = false;

                    for (java.io.File classFile : classFiles) {
                        String className = classFile.getName().replace(".class", "");
                        String fullClassName = packageName + "." + className;

                        try {
                            Class<?> clazz = Class.forName(fullClassName);

                            if (registerInstructionClass(clazz)) {
                                found = true;
                            }
                        } catch (Exception e) {
                        }
                    }

                    return found;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryDirectClassLoadingDiscovery() {
        try {
            String[] commonOpcodes = {"add", "sub", "mul", "div", "goto", "if", "if_cmpgt", "if_cmpeq", "print", "load", "store", "push", "pop", "return", "invoke", "sqrt"};            String[] suffixes = {"", "Instruction"};
            String packageName = Instruction.class.getPackage().getName();
            boolean found = false;

            for (String opcode : commonOpcodes) {
                for (String suffix : suffixes) {
                    String className = Character.toUpperCase(opcode.charAt(0)) + opcode.substring(1) + suffix;
                    String fullClassName = packageName + "." + className;

                    try {
                        Class<?> clazz = Class.forName(fullClassName);

                        if (registerInstructionClass(clazz)) {
                            found = true;
                        }
                    } catch (ClassNotFoundException e) {
                    } catch (Exception e) {
                    }
                }
            }
            return found;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Registers an instruction class if it meets the criteria.
     *
     * @param clazz the class to register
     * @return true if the class was registered, false otherwise
     */
    private static boolean registerInstructionClass(Class<?> clazz) {
        try {
            if (Instruction.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {

                try {
                    java.lang.reflect.Field opcodeField = clazz.getField("OP_CODE");
                    Object opcodeValue = opcodeField.get(null);

                    if (opcodeValue instanceof String opcode) {
                        @SuppressWarnings("unchecked") Class<? extends Instruction> instructionClass = (Class<? extends Instruction>) clazz;
                        INSTRUCTION_MAP.put(opcode, instructionClass);
                        return true;
                    }
                } catch (NoSuchFieldException e) {
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * Creates an instruction instance for the given opcode and label.
     *
     * @param opcode the instruction opcode
     * @param label  the optional label (can be null)
     * @return the created instruction, or null if the opcode is unknown
     */
    public static Instruction createInstruction(String opcode, Label label) {
        Class<? extends Instruction> instructionClass = INSTRUCTION_MAP.get(opcode);
        if (instructionClass == null) {
            return null;
        }

        try {
            try {
                Constructor<?> constructor = instructionClass.getConstructor(Label.class);
                return (Instruction) constructor.newInstance(label);
            } catch (NoSuchMethodException e) {
            }
            return null;

        } catch (Exception e) {
            System.err.println("Error creating instruction for opcode: " + opcode);
            e.printStackTrace();
            return null;
        }
    }

    public static Instruction createGotoInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("goto");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return null;
        }
    }


    public static Instruction createIfGreaterGotoInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("if_cmpgt");
            if (cls == null) return new IfGreaterGotoInstruction(label, target);
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return new IfGreaterGotoInstruction(label, target);
        }
    }

    public static Instruction createIfEqualGotoInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("if_cmpeq");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return null;
        }
    }
    public static Instruction createLoadInstruction(Label label, Variable.Identifier varId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("load");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, Variable.Identifier.class);
            return (Instruction) constructor.newInstance(label, varId);
        } catch (Exception e) {
            return null;
        }
    }

    public static Instruction createStoreInstruction(Label label, Variable.Identifier varId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("store");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, Variable.Identifier.class);
            return (Instruction) constructor.newInstance(label, varId);
        } catch (Exception e) {
            return null;
        }
    }

    public static Instruction createInvokeInstruction(Label label, Method.Identifier methodId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("invoke");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, Method.Identifier.class);
            return (Instruction) constructor.newInstance(label, methodId);
        } catch (Exception e) {
            return null;
        }
    }

    public static Instruction createSquareRootInstruction(Label label) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("sqrt");
            if (cls == null) return new SquareRootInstruction(label);
            Constructor<?> constructor = cls.getConstructor(Label.class);
            return (Instruction) constructor.newInstance(label);
        } catch (Exception e) {
            return new SquareRootInstruction(label);
        }
    }
    public static Instruction createPushInstruction(Label label, int value) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("push");
            if (cls == null) return null;
            Constructor<?> constructor = cls.getConstructor(Label.class, int.class);
            return (Instruction) constructor.newInstance(label, value);
        } catch (Exception e) {
            return null;
        }
    }
}