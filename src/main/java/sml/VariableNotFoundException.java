package sml;


/**
 * Exception thrown when a variable is not found.
 *
 * <p>This exception is typically used in situations where a variable is accessed or referenced,
 * but it is not found in the context where it is expected.</p>
 */
public class VariableNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code VariableNotFoundException} with a message indicating the variable
     * that was not found.
     *
     * @param var the identifier of the variable that was not found
     */
    public VariableNotFoundException(Variable.Identifier var) {
        super("Variable " + var + " not found");
    }
}
