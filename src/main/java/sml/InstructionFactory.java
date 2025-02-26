package sml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sml.helperfiles.InstructionRegistrationLogger;
import sml.instructions.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.*;

/**
 * A dynamic factory for creating SML instructions.
 *
 * <p>Manages the discovery and instantiation of instruction classes through two primary strategies:</p>
 * <ul>
 *   <li>Package Scanning: Dynamically discovers instruction classes in the {@code sml.instructions} package</li>
 *   <li>Direct Class Loading: Loads predefined instruction classes using known opcodes</li>
 * </ul>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Automatic instruction class registration</li>
 *   <li>Flexible instruction creation based on opcodes</li>
 *   <li>Fallback mechanisms for instruction discovery</li>
 *   <li>Integrated logging for registration processes</li>
 * </ul>
 *
 * <p>Uses Spring's dependency injection and supports runtime instruction class discovery.</p>
 *
 * @author Ricki Angel
 */

@Component("InstructionFactory")
@Qualifier
public class InstructionFactory {
    private final InstructionRegistrationLogger logger;

    private static final Logger LOGGER = Logger.getLogger(InstructionFactory.class.getName());
    private static final Map<String, Class<? extends Instruction>> INSTRUCTION_MAP = new HashMap<>();

    /**
     * Constructs an InstructionFactory with a custom instruction registration logger.
     *
     * <p>Initializes the factory with a logger that tracks the discovery and registration
     * of instruction classes during runtime.</p>
     *
     * @param logger The logger used to track instruction registration processes
     * @throws NullPointerException if the provided logger is null
     */

    @Autowired
    public InstructionFactory(InstructionRegistrationLogger logger) {
        this.logger = logger;
    }

    static {
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setFormatter(new SimpleFormatter() {
                    @Override
                    public synchronized String format(LogRecord record) {
                        return record.getMessage() + System.lineSeparator();
                    }
                });
            }
        }

        discoverInstructionClasses();
    }

    private static void discoverInstructionClasses() {
        InstructionFactory factory = new InstructionFactory(new sml.helperfiles.DefaultInstructionRegistrationLogger());
        factory.performDiscovery();
        factory.logger.printRegistrationSummary();
    }

    private void performDiscovery() {
        discoverByPackageScanning();
        if (INSTRUCTION_MAP.isEmpty()) {
            discoverByDirectClassLoading();
        }
        if (INSTRUCTION_MAP.isEmpty()) {
            throw new RuntimeException("Failed to register any instruction classes");
        }
    }

    /**
     * Creates an instruction based on the given opcode and label.
     *
     * @param opcode The operation code identifying the instruction type
     * @param label  The label associated with the instruction
     * @return A new Instruction instance, or null if creation fails
     */
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
     * Creates a specific instruction with additional parameters.
     *
     * <p>Provides a flexible mechanism for creating instructions with various
     * constructor signatures. Falls back to a default implementation if
     * specific class loading fails.</p>
     *
     * @param opcode       The operation code for the instruction
     * @param defaultClass The default instruction class to use if specific class fails
     * @param params       Constructor parameters for the instruction
     * @return A new Instruction instance, or null if creation fails
     */
    private static <T> Instruction createSpecificInstruction(String opcode, Class<T> defaultClass, Object... params) {
        try {
            Class<? extends Instruction> cls = INSTRUCTION_MAP.get(opcode);
            if (cls == null)
                return (Instruction) defaultClass.getConstructor(Label.class, params.getClass()).newInstance(params);
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

    /**
     * Discovers instruction classes by scanning the predefined package.
     *
     * <p>Searches for .class files in the {@code sml.instructions} package,
     * attempting to load and register classes that implement the Instruction interface.</p>
     *
     * <p>Logs registration attempts and successes/failures.</p>
     *
     * @see #registerInstructionClass(Class)
     */
    private void discoverByPackageScanning() {
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
                        } catch (ClassNotFoundException e) {
                            LOGGER.log(Level.WARNING, "Error loading class " + fullClassName, e);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Package scanning failed", e);
        }
    }

    /**
     * Discovers instruction classes using a fallback direct class loading strategy.
     *
     * <p>Uses a predefined list of known opcodes to attempt class loading for
     * legacy or standard instruction types.</p>
     *
     * <p>Attempts to load classes with naming convention:
     * First letter capitalized + "Instruction" suffix</p>
     */
    private void discoverByDirectClassLoading() {
        try {
            String[] previouslyKnownOpcodes = {"add", "sub", "mul", "div", "goto", "if_cmpgt", "if_cmpeq", "print",
                    "load", "store", "push", "pop", "return", "invoke", "sqrt", "not_eq"};
            String packageName = Instruction.class.getPackage().getName();
            for (String opcode : previouslyKnownOpcodes) {
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
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Direct class loading discovery failed", e);
        }
    }

    /**
     * Registers a discovered instruction class in the internal instruction map.
     *
     * <p>Validates the class by checking:</p>
     * <ul>
     *   <li>Is a subclass of Instruction</li>
     *   <li>Is not an abstract class</li>
     *   <li>Contains a static OP_CODE field</li>
     * </ul>
     *
     * @param clazz The class to be registered
     */
    private void registerInstructionClass(Class<?> clazz) {
        logger.logRegistrationAttempt(clazz);

        if (Instruction.class.isAssignableFrom(clazz)) {
            try {
                if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    LOGGER.log(Level.FINEST, "Skipping abstract class: " + clazz.getName());
                    return;
                }

                String opcode = (String) clazz.getDeclaredField("OP_CODE").get(null);

                INSTRUCTION_MAP.put(opcode, clazz.asSubclass(Instruction.class));

                logger.trackSuccessfulRegistration(clazz.getSimpleName(), opcode);
            } catch (NoSuchFieldException e) {
                logger.trackFailedRegistration(clazz.getSimpleName(), "Missing OP_CODE field");
            } catch (Exception e) {
                logger.trackFailedRegistration(clazz.getSimpleName(), "Registration error: " + e.getMessage());
            }
        } else {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Not an Instruction subclass");
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
