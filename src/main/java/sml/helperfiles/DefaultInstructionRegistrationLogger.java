package sml.helperfiles;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the {@link InstructionRegistrationLogger} for tracking
 * instruction class registration processes in the Simple Machine Language (SML) system.
 * <p>
 * This component provides a comprehensive logging mechanism for tracking
 * instruction class registration attempts, successes, and failures during
 * dynamic class discovery and registration.
 * </p>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>Tracks successful instruction registrations</li>
 *     <li>Captures details of failed registration attempts</li>
 *     <li>Generates a detailed registration summary</li>
 *     <li>Supports both console and logging framework output</li>
 * </ul>
 *
 * <p>
 * The logger maintains two internal lists:
 * <ol>
 *     <li>A list of successfully registered instructions</li>
 *     <li>A list of failed registration attempts</li>
 * </ol>
 * </p>
 *
 * <p>
 * Registration information can be printed to the console using
 * {@link #printRegistrationSummary()} method, which provides a
 * comprehensive overview of the registration process.
 * </p>
 *
 * @author Ricki Angel
 * @version 1.0
 * @see InstructionRegistrationLogger
 * @since 1.0
 */
@Component
public class DefaultInstructionRegistrationLogger implements InstructionRegistrationLogger {
    /**
     * List to track successfully registered instruction classes.
     * Each entry includes the instruction name and its opcode.
     */
    private static final List<String> SUCCESSFULLY_REGISTERED = new ArrayList<>();

    /**
     * List to track failed instruction registration attempts.
     * Each entry includes the instruction name and reason for failure.
     */
    private static final List<String> FAILED_REGISTRATION = new ArrayList<>();

    /**
     * System logger for recording registration events and errors.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultInstructionRegistrationLogger.class.getName());

    /**
     * Logs an attempt to register an instruction class.
     * <p>
     * Currently, this method is a no-op (commented out logging),
     * but can be extended to provide more detailed registration tracking.
     * </p>
     *
     * @param clazz The instruction class being registered
     */
    @Override
    public void logRegistrationAttempt(Class<?> clazz) {
//        LOGGER.log(Level.INFO, "Attempting to register class: " + clazz.getSimpleName());
    }

    /**
     * Tracks a successful instruction registration.
     * <p>
     * Adds the instruction name and its opcode to the list of successfully
     * registered instructions.
     * </p>
     *
     * @param instructionName Name of the successfully registered instruction
     * @param opcode          Opcode associated with the instruction
     */
    @Override
    public void trackSuccessfulRegistration(String instructionName, String opcode) {
        SUCCESSFULLY_REGISTERED.add(instructionName + " (opcode: " + opcode + ")");
//        LOGGER.log(Level.FINEST, "Successfully registered instruction: " + instructionName + " for opcode: " + opcode);
    }

    /**
     * Tracks a failed instruction registration attempt.
     * <p>
     * Logs the failure reason and adds the instruction to the failed
     * registration list. Outputs an error message to the console and
     * logs a warning through the logging framework.
     * </p>
     *
     * @param instructionName Name of the instruction that failed to register
     * @param reason          Explanation for the registration failure
     */
    @Override
    public void trackFailedRegistration(String instructionName, String reason) {
        FAILED_REGISTRATION.add(instructionName + " - " + reason);

        System.err.println("Instruction class not in /instructions folder: " + instructionName);

        LOGGER.log(Level.WARNING, "Failed to register instruction: " + instructionName + " - " + reason);
    }

    /**
     * Prints a comprehensive summary of the instruction registration process.
     * <p>
     * Outputs to the console:
     * <ul>
     *     <li>Details of successfully registered instructions</li>
     *     <li>List of failed registration attempts (if any)</li>
     *     <li>Total count of successful and failed registrations</li>
     * </ul>
     * </p>
     */
    @Override
    public void printRegistrationSummary() {
        System.out.println("\n=== Instruction Registration Summary ===");

        for (int i = 0; i < SUCCESSFULLY_REGISTERED.size(); i++) {
            System.out.println("  → Attempting: " + SUCCESSFULLY_REGISTERED.get(i).split(" \\(")[0]);
            System.out.println("  ✓ Registered: " + SUCCESSFULLY_REGISTERED.get(i) + "\n");
        }

        if (!FAILED_REGISTRATION.isEmpty()) {
            System.out.println("\nFailed Registration Attempts:");
            FAILED_REGISTRATION.forEach(instruction -> System.out.println("  ✗ " + instruction));
        }

        System.out.println("\nTotal Registered: " + SUCCESSFULLY_REGISTERED.size());
        System.out.println("Total Failed: " + FAILED_REGISTRATION.size());
        System.out.println("=======================================\n");
    }
}
