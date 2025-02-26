package sml.helperfiles;

/**
 * Defines a contract for logging and tracking instruction class registration processes
 * in the Simple Machine Language system.
 * <p>
 * This interface provides methods for monitoring the registration of instruction classes,
 * capturing both successful and failed registration attempts, and generating a
 * comprehensive summary of the registration process.
 * </p>
 *
 * <h2>Key Responsibilities</h2>
 * <ul>
 *     <li>Log initial registration attempts</li>
 *     <li>Track successful instruction registrations</li>
 *     <li>Record reasons for failed registrations</li>
 *     <li>Generate a detailed registration summary</li>
 * </ul>
 *
 * <p>
 * Implementations of this interface are expected to provide concrete logging
 * mechanisms for tracking instruction class registration in the SML system.
 * </p>
 *
 * @author Ricki Angel
 * @version 1.0
 * @see DefaultInstructionRegistrationLogger
 * @since 1.0
 */
public interface InstructionRegistrationLogger {
    /**
     * Logs an attempt to register an instruction class.
     * <p>
     * This method is called when the system begins the process of registering
     * a new instruction class, allowing for initial tracking of registration attempts.
     * </p>
     *
     * @param clazz The instruction class being registered
     */
    void logRegistrationAttempt(Class<?> clazz);

    /**
     * Tracks a successful instruction registration.
     * <p>
     * Records details of an instruction that has been successfully registered,
     * including its name and associated opcode.
     * </p>
     *
     * @param instructionName The name of the successfully registered instruction
     * @param opcode          The unique operation code associated with the instruction
     */
    void trackSuccessfulRegistration(String instructionName, String opcode);

    /**
     * Tracks a failed instruction registration attempt.
     * <p>
     * Captures details of an instruction that could not be registered,
     * including the reason for the registration failure.
     * </p>
     *
     * @param instructionName The name of the instruction that failed to register
     * @param reason          Explanation for why the registration attempt failed
     */
    void trackFailedRegistration(String instructionName, String reason);

    /**
     * Generates and outputs a comprehensive summary of the instruction registration process.
     * <p>
     * Provides a detailed report of:
     * <ul>
     *     <li>Successfully registered instructions</li>
     *     <li>Failed registration attempts</li>
     *     <li>Total count of registrations</li>
     * </ul>
     * </p>
     */
    void printRegistrationSummary();
}
