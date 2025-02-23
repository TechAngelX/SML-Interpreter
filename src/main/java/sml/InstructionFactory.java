package sml;

import sml.instructions.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ===========================================================================================================
 * Factory for dynamically discovering and creating Simple Machine Language (SML) instructions.
 * -----------------------------------------------------------------------------------------------------------
 * Chain of Responsibility Pattern Implementation: Uses two primary discovery strategies to identify available
 * instruction types without manual configuration.
 * <p>
 * 1. Package Scanning Strategy:
 * - Searches the /sml/instruction package directory for .class files
 * - Dynamically loads all classes in the package, filtering classes that implement the Instruction interface.
 * <p>
 * 2. Direct Class Loading Strategy:
 * - Uses predefined list of expected instruction class names
 * - Generates potential class names based on opcodes
 * - Capitalizes first letter and appends 'Instruction' suffix
 *
 * @author Ricki Angel
 */
public class InstructionFactory {
    private static final Logger LOGGER = Logger.getLogger(InstructionFactory.class.getName());

    private static final Map<String, Class<? extends Instruction>> INSTRUCTION_MAP = new HashMap<>();

    static {
        discoverInstructionClasses();
    }


    public static Instruction createInstruction(String opcode, Label label) {
        Class<? extends Instruction> instructionClass = INSTRUCTION_MAP.get(opcode);
        if (instructionClass == null) {
            return null;
        }

        try {
            Constructor<?> constructor = instructionClass.getConstructor(Label.class);
            return (Instruction) constructor.newInstance(label);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error creating instruction: " + opcode, e);
            return null;
        }
    }

    /**
     * Backward-compatible methods for specific instruction types
     */
    public static Instruction createGotoInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("goto");
            if (cls == null) return new GotoInstruction(label, target);
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return new GotoInstruction(label, target);
        }
    }

    public static Instruction createIfCmpeqInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("if_cmpeq");
            if (cls == null) return new IfCmpeqInstruction(label, target);
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return new IfCmpeqInstruction(label, target);
        }
    }

    public static Instruction createIfCmpgtInstruction(Label label, Label target) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("if_cmpgt");
            if (cls == null) return new IfCmpgtInstruction(label, target);
            Constructor<?> constructor = cls.getConstructor(Label.class, Label.class);
            return (Instruction) constructor.newInstance(label, target);
        } catch (Exception e) {
            return new IfCmpgtInstruction(label, target);
        }
    }


    public static Instruction createLoadInstruction(Label label, Variable.Identifier varId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("load");
            if (cls == null) return new LoadInstruction(label, varId);
            Constructor<?> constructor = cls.getConstructor(Label.class, Variable.Identifier.class);
            return (Instruction) constructor.newInstance(label, varId);
        } catch (Exception e) {
            return new LoadInstruction(label, varId);
        }
    }

    public static Instruction createStoreInstruction(Label label, Variable.Identifier varId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("store");
            if (cls == null) return new StoreInstruction(label, varId);
            Constructor<?> constructor = cls.getConstructor(Label.class, Variable.Identifier.class);
            return (Instruction) constructor.newInstance(label, varId);
        } catch (Exception e) {
            return new StoreInstruction(label, varId);
        }
    }

    public static Instruction createInvokeInstruction(Label label, Method.Identifier methodId) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get("invoke");
            if (cls == null) return new InvokeInstruction(label, methodId);
            Constructor<?> constructor = cls.getConstructor(Label.class, Method.Identifier.class);
            return (Instruction) constructor.newInstance(label, methodId);
        } catch (Exception e) {
            return new InvokeInstruction(label, methodId);
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
            if (cls == null) return new PushInstruction(label, value);
            Constructor<?> constructor = cls.getConstructor(Label.class, int.class);
            return (Instruction) constructor.newInstance(label, value);
        } catch (Exception e) {
            return new PushInstruction(label, value);
        }
    }

    private static void discoverInstructionClasses() {
        discoverByPackageScanning();

        if (INSTRUCTION_MAP.isEmpty()) {
            discoverByDirectClassLoading();
        }

        if (INSTRUCTION_MAP.isEmpty()) {
            throw new RuntimeException("Failed to register any instruction classes");
        }
    }
    /**
     * =====================================================================================
     * Package Scanning Discovery.
     * -------------------------------------------------------------------------------------
     * The discoverByPackageScanning() method iterates through all .class files found in the
     * sml/instruction package directory. For each file, it attempts to load the corresponding
     * class using Class.forName(). It then checks if the loaded class is a subclass of
     * Instruction (if it extends Instruction) and if it has a public static OP_CODE field.
     * If the checks pass, it registers the class in the INSTRUCTION_MAP using the opcode as the key.
     *
     * The {@code doExecute} method defines the instruction's core operational logic.
     * @author Ricki Angel
     */
    private static void discoverByPackageScanning() {
        try {
            String packageName = "sml.instructions";
            String packagePath = packageName.replace('.', '/');

            String rootPath = System.getProperty("user.dir");
            String classesPath = rootPath + "/target/classes/" + packagePath;

            LOGGER.info("Root Path: " + rootPath);
            LOGGER.info("Instruction Classes Path: " + classesPath);

            java.io.File packageDir = new java.io.File(classesPath);

            if (packageDir.exists()) {
                LOGGER.info("Package Directory exists: " + packageDir.getAbsolutePath());

                java.io.File[] classFiles = packageDir.listFiles(
                        file -> file.isFile() && file.getName().endsWith(".class")
                );

                LOGGER.info("Number of class files found: " + (classFiles != null ? classFiles.length : "null"));

                if (classFiles != null) {
                    for (java.io.File classFile : classFiles) {
                        String className = classFile.getName().replace(".class", "");
                        String fullClassName = packageName + "." + className;

                        LOGGER.info("Attempting to load class: " + fullClassName);

                        try {
                            Class<?> clazz = Class.forName(fullClassName);
                            registerInstructionClass(clazz);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading class " + fullClassName, e);
                        }
                    }
                }
            } else {
                LOGGER.warning("Package directory does not exist: " + packageDir.getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Package scanning failed", e);
        }
    }
    //If using this discovery method, add new op_code here.
    /**
     * =====================================================================================
     * Direct Class Discovery and Registration
     * -------------------------------------------------------------------------------------
     * Attempts to discover and register instruction classes by directly loading them
     * using predefined opcodes and class name suffixes. This method is a fallback
     * if package scanning fails.
     * <p>
     * Classes are loaded and registered using {@link #registerInstructionClass(Class)}.
     * Opcodes are transformed to class names by removing hyphens, capitalizing to camel case,
     * and appending 'Instruction'. Exceptions are logged, but do not halt the process.
     * To use this discovery method, add new opcodes here and create concrete Instruction
     * classes with corresponding opcode identifiers.
     */
    private static void discoverByDirectClassLoading() {
        try {
            String[] commonOpcodes = {
                    "add", "sub", "mul", "div", "goto",
                    "if_cmpgt", "if_cmpeq", "print", "load",
                    "store", "push", "pop", "return", "invoke", "sqrt",
            };
            String[] suffixes = {"", "Instruction"};
            String packageName = Instruction.class.getPackage().getName();

            for (String opcode : commonOpcodes) {
                for (String suffix : suffixes) {
                    String className = Character.toUpperCase(opcode.charAt(0))
                            + opcode.substring(1) + suffix;
                    String fullClassName = packageName + "." + className;

                    try {
                        Class<?> clazz = Class.forName(fullClassName);
                        registerInstructionClass(clazz);
                    } catch (ClassNotFoundException e) {

                    } catch (Exception e) {
                        LOGGER.log(Level.FINE, "Error loading class: " + fullClassName, e);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Direct class loading discovery failed", e);
        }
    }

    /**
     * Helper method to register an instruction class. Checks if class is a concrete
     * Instruction subclass.
     */
    private static void registerInstructionClass(Class<?> clazz) {
        LOGGER.info("Attempting to register class: " + clazz.getName());

        if (Instruction.class.isAssignableFrom(clazz)
                && !clazz.isInterface()
                && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {

            try {
                java.lang.reflect.Field opcodeField = clazz.getField("OP_CODE");
                Object opcodeValue = opcodeField.get(null);

                LOGGER.info("Found opcode: " + opcodeValue);

                if (opcodeValue instanceof String opcode) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Instruction> instructionClass =
                            (Class<? extends Instruction>) clazz;

                    LOGGER.info("Registering instruction: " + opcode);
                    INSTRUCTION_MAP.put(opcode, instructionClass);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error registering instruction class: " + clazz.getName(), e);
            }
        } else {
            LOGGER.info("Class did not meet registration criteria: " + clazz.getName());
        }
    }
}