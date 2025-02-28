package sml.discovery;

import sml.helperfiles.InstructionRegistrationLogger;
import sml.instructions.Instruction;
import sml.registry.InstructionRegistry;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Discovers and registers SML instructions dynamically by scanning package directories.
 * <p>
 * This class implements a strategy for runtime instruction discovery by:
 * <ul>
 *   <li>Examining the classes in the SML instructions package</li>
 *   <li>Identifying classes that implement the Instruction interface</li>
 *   <li>Registering those that meet the criteria for being a valid instruction</li>
 * </ul>
 * </p>
 *
 * <h2>Usage</h2>
 * <p>
 * To add a new instruction type:
 * <ol>
 *   <li>Create a new class in the instructions directory</li>
 *   <li>Implement the Instruction interface</li>
 *   <li>Define a static OP_CODE field with the opcode string</li>
 *   <li>Implement the required constructor and methods</li>
 * </ol>
 * The package scanning mechanism will automatically discover and register the new instruction
 * without requiring modifications to the factory code.
 * </p>
 *
 * @author Ricki Angel
 */
public class PackageScanDiscovery implements InstructionDiscoveryStrategy {
    private static final Logger LOGGER = Logger.getLogger(PackageScanDiscovery.class.getName());
    private static final String PACKAGE_NAME = "sml.instructions";

    private final InstructionRegistrationLogger logger;

    /**
     * Constructs a new PackageScanDiscovery discovery method.
     *
     * @param logger The logger to use for tracking registration events.
     */
    public PackageScanDiscovery(InstructionRegistrationLogger logger) {
        this.logger = logger;
    }

    /**
     * Discovers and registers instruction classes by scanning the package directory.
     * <p>
     * This implementation searches for class files in the SML instructions package directory,
     * loads each class, and attempts to register those that:
     * <ul>
     *   <li>Are subclasses of {@link Instruction}</li>
     *   <li>Are not abstract classes</li>
     *   <li>Contain a static {@code OP_CODE} field</li>
     *   <li>Have an opcode that hasn't already been registered</li>
     * </ul>
     * </p>
     * <p>
     * The method logs detailed information about the discovery process,
     * including warnings for missing directories and registration failures.
     * </p>
     *
     * @param registry The registry to register discovered instructions with
     * @return The number of newly registered instructions, or 0 if discovery fails
     * @throws RuntimeException If a severe error occurs during the scanning process
     */
   
    @Override
    public int discoverInstructions(InstructionRegistry registry) {
        LOGGER.log(Level.INFO, "Discovering instructions by package scanning in: " + PACKAGE_NAME);
        int initialSize = registry.size();
        int registered = 0;

        try {
            String packagePath = PACKAGE_NAME.replace('.', '/');
            String rootPath = System.getProperty("user.dir");
            String classesPath = rootPath + "/target/classes/" + packagePath;
            File packageDir = new File(classesPath);

            if (!packageDir.exists()) {
                LOGGER.log(Level.WARNING, "Package directory not found: " + classesPath);
                return 0;
            }

            File[] classFiles = packageDir.listFiles(
                    file -> file.isFile() && file.getName().endsWith(".class")
                            && !file.getName().equals("package-info.class")
            );

            if (classFiles == null) {
                LOGGER.log(Level.WARNING, "No class files found in package directory");
                return 0;
            }

            for (File classFile : classFiles) {
                String className = classFile.getName().replace(".class", "");
                String fullClassName = PACKAGE_NAME + "." + className;
                try {
                    Class<?> clazz = Class.forName(fullClassName);
                    if (registerClass(registry, clazz)) {
                        registered++;
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.log(Level.FINE, "Error loading class: " + fullClassName, e);
                }
            }

            LOGGER.log(Level.INFO, "Registered " + registered + " instructions by package scanning");
            return registered;

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Package scanning failed: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Registers a class as an instruction if it meets all required criteria.
     * <p>
     * The method performs validation checks to ensure the class:
     * <ul>
     *   <li>Is a subclass of {@link Instruction}</li>
     *   <li>Is not an abstract class</li>
     *   <li>Has a static OP_CODE field</li>
     *   <li>Has an opcode that isn't already registered</li>
     * </ul>
     * </p>
     * <p>
     * All registration attempts, successful or failed, are tracked by the logger.
     * </p>
     *
     * @param registry The registry to register the instruction with
     * @param clazz The class to evaluate and potentially register
     * @return {@code true} if registration was successful, {@code false} otherwise
     */

    private boolean registerClass(InstructionRegistry registry, Class<?> clazz) {
        logger.logRegistrationAttempt(clazz);

        if (!Instruction.class.isAssignableFrom(clazz)) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Not an Instruction subclass");
            return false;
        }

        if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
            LOGGER.log(Level.FINEST, "Skipping abstract class: " + clazz.getName());
            return false;
        }

        try {
            String opcode = (String) clazz.getDeclaredField("OP_CODE").get(null);

            if (registry.isRegistered(opcode)) {
                LOGGER.log(Level.FINE, "Opcode already registered: " + opcode);
                return false;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Instruction> instructionClass = (Class<? extends Instruction>) clazz;
            registry.register(opcode, instructionClass);
            logger.trackSuccessfulRegistration(clazz.getSimpleName(), opcode);
            return true;
        } catch (NoSuchFieldException e) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Missing OP_CODE field");
            return false;
        } catch (Exception e) {
            logger.trackFailedRegistration(clazz.getSimpleName(), "Registration error: " + e.getMessage());
            return false;
        }
    }
    /**
     * Returns the descriptive name of this discovery strategy.
     * <p>
     * This name is used for logging and reporting purposes to identify
     * which discovery mechanism successfully registered instructions.
     * </p>
     *
     * @return The string "Package Scanning" as the strategy identifier
     */

    @Override
    public String getName() {
        return "Package Scanning";
    }
}
