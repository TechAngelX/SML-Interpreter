package sml.helperfiles;

public interface InstructionRegistrationLogger {
    void logRegistrationAttempt(Class<?> clazz);
    void trackSuccessfulRegistration(String instructionName, String opcode);
    void trackFailedRegistration(String instructionName, String reason);
    void printRegistrationSummary();
}
