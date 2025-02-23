package sml.helperfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * =====================================================================================
 * Default implementation of the InstructionRegistrationLogger.
 * -------------------------------------------------------------------------------------
 * Provides comprehensive tracking and logging for instruction class registration processes.
 *  This logger maintains two internal lists to track successful and failed instruction
 * registrations during the dynamic class discovery process. It offers detailed logging
 * at various stages of instruction registration, including:
 * <ul>
 * <li>Logging registration attempts</li>
 * <li>Tracking successful registrations</li>
 * <li>Capturing failed registration reasons</li>
 * <li>Generating a comprehensive registration summary</li>
 * </ul>
 * <p>
 * @author Ricki Angel
 */
public class DefaultInstructionRegistrationLogger implements InstructionRegistrationLogger {
    private static final List<String> SUCCESSFULLY_REGISTERED = new ArrayList<>();
    private static final List<String> FAILED_REGISTRATION = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(DefaultInstructionRegistrationLogger.class.getName());

    @Override
    public void logRegistrationAttempt(Class<?> clazz) {
//        LOGGER.log(Level.INFO, "Attempting to register class: " + clazz.getSimpleName());
    }

    @Override
    public void trackSuccessfulRegistration(String instructionName, String opcode) {
        SUCCESSFULLY_REGISTERED.add(instructionName + " (opcode: " + opcode + ")");
//        LOGGER.log(Level.FINEST, "Successfully registered instruction: " + instructionName + " for opcode: " + opcode);
    }

    @Override
    public void trackFailedRegistration(String instructionName, String reason) {
        FAILED_REGISTRATION.add(instructionName + " - " + reason);

        System.err.println("Instruction class not in /instructions folder: " + instructionName);

        LOGGER.log(Level.WARNING, "Failed to register instruction: " + instructionName + " - " + reason);
    }
    @Override
    public void printRegistrationSummary() {
        System.out.println("\n=== Instruction Registration Summary ===");

        for (int i = 0; i < SUCCESSFULLY_REGISTERED.size(); i++) {
            System.out.println("  → Attempting: " + SUCCESSFULLY_REGISTERED.get(i).split(" \\(")[0]);
            System.out.println("  ✓ Registered: " + SUCCESSFULLY_REGISTERED.get(i)+"\n");
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