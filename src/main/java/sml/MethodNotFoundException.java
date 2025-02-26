package sml;

/**
 * Exception thrown when a method is not found.
 *
 * <p>This exception is typically used when attempting to access or execute a method that
 * doesn't exist or hasn't been defined in the SML program.</p>
 */
public class MethodNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code MethodNotFoundException} with a detailed message.
     *
     * @param method the identifier of the method that could not be found
     */
    public MethodNotFoundException(Method.Identifier method) {
        super("Method not found: " + method);
    }
}
