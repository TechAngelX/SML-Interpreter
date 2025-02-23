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

    private static <T> Instruction createSpecificInstruction(String opcode, Class<T> defaultClass, Object... params) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get(opcode);
            if (cls == null) return (Instruction) defaultClass.getConstructor(Label.class, params.getClass()).newInstance(params);
            Constructor<?> constructor = cls.getConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
            return (Instruction) constructor.newInstance(params);
        } catch (Exception e) {
            try {
                return (Instruction) defaultClass.getConstructor(Label.class, params.getClass()).newInstance(params);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error creating default instruction for: " + opcode, ex);
                return null;
            }
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
            java.io.File packageDir = new java.io.File(classesPath);

            if (packageDir.exists()) {
                java.io.File[] classFiles = packageDir.listFiles(
                        file -> file.isFile() && file.getName().endsWith(".class")
                );
                if (classFiles != null) {
                    for (java.io.File classFile : classFiles) {
                        String className = classFile.getName().replace(".class", "");
                        String fullClassName = packageName + "." + className;
                        try {
                            Class<?> clazz = Class.forName(fullClassName);
                            registerInstructionClass(clazz);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading class " + fullClassName, e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Package scanning failed", e);
        }
    }

    /**
     * =====================================================================================
     * Direct Class Discovery and Registration
     * -------------------------------------------------------------------------------------
     * Attempts to discover and register instruction classes by directly loading them
     * using predefined opcodes and class name suffixes.
     */
    private static void discoverByDirectClassLoading() {
        try {
            String[] commonOpcodes = {"add", "sub", "mul", "div", "goto", "if_cmpgt", "if_cmpeq", "print", "load", "store", "push", "pop", "return", "invoke", "sqrt"};
            String packageName = Instruction.class.getPackage().getName();
            for (String opcode : commonOpcodes) {
                String className = Character.toUpperCase(opcode.charAt(0)) + opcode.substring(1) + "Instruction";
                String fullClassName = packageName + "." + className;
                try {
                    Class<?> clazz = Class.forName(fullClassName);
                    registerInstructionClass(clazz);
                } catch (ClassNotFoundException ignored) {
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error loading class: " + fullClassName, e);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Direct class loading discovery failed", e);
        }
    }

    private static void registerInstructionClass(Class<?> clazz) {
        LOGGER.log(Level.INFO, "Attempting to register class: {0}", clazz.getSimpleName());

        if (Instruction.class.isAssignableFrom(clazz)) {
            try {
                String opcode = (String) clazz.getDeclaredField("OP_CODE").get(null);
                INSTRUCTION_MAP.put(opcode, clazz.asSubclass(Instruction.class));
                LOGGER.log(Level.FINEST, "Successfully registered instruction: {0} for opcode: {1}",
                        new Object[]{clazz.getSimpleName(), opcode});
            } catch (NoSuchFieldException e) {
                if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    LOGGER.log(Level.FINEST, "Skipping abstract class: {0}", clazz.getName());
                } else {
                    LOGGER.log(Level.WARNING, "Missing OP_CODE field in {0}", clazz.getName());
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error registering class: " + clazz.getName(), e);
            }
        }
    }

    /**
     * Backward-compatible methods for specific instruction types
     */
    public static Instruction createGotoInstruction(Label label, Label target) {
        return createSpecificInstruction("goto", GotoInstruction.class, label, target);
    }

    public static Instruction createIfCmpeqInstruction(Label label, Label target) {
        return createSpecificInstruction("if_cmpeq", IfCmpeqInstruction.class, label, target);
    }

    public static Instruction createIfCmpgtInstruction(Label label, Label target) {
        return createSpecificInstruction("if_cmpgt", IfCmpgtInstruction.class, label, target);
    }

    public static Instruction createLoadInstruction(Label label, Variable.Identifier varId) {
        return createSpecificInstruction("load", LoadInstruction.class, label, varId);
    }

    public static Instruction createStoreInstruction(Label label, Variable.Identifier varId) {
        return createSpecificInstruction("store", StoreInstruction.class, label, varId);
    }

    public static Instruction createInvokeInstruction(Label label, Method.Identifier methodId) {
        return createSpecificInstruction("invoke", InvokeInstruction.class, label, methodId);
    }

    public static Instruction createSquareRootInstruction(Label label) {
        return createSpecificInstruction("sqrt", SqrtInstruction.class, label);
    }

    public static Instruction createPushInstruction(Label label, int value) {
        return createSpecificInstruction("push", PushInstruction.class, label, value);
    }
}
