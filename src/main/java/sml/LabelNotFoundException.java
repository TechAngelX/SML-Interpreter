package sml;

/**
 * An exception raised when a label cannot be located within a method.
 *
 * <p>Manages the handling of label lookup failures during Simple Machine Language (SML) program execution.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Provides detailed context about the missing label</li>
 *   <li>Stores the problematic label and method for further investigation</li>
 *   <li>Generates informative error messages</li>
 *   <li>Supports debugging of label resolution issues</li>
 * </ul>
 *
 * <p>Enhances error reporting by capturing specific details of label lookup failures.</p>
 *
 * @author Ricki Angel
 */
public class LabelNotFoundException extends RuntimeException {
    /**
     * The label that could not be found within the method.
     * Stored to provide context about the specific label causing the exception.
     */
    private final Label label;

    /**
     * The method in which the label lookup failed.
     * Provides crucial context for understanding the source of the label resolution error.
     */
    private final Method method;

    /**
     * Constructs a new LabelNotFoundException with the specified label and method.
     *
     * @param label The label that was not found
     * @param method The method in which the label lookup failed
     */
    public LabelNotFoundException(Label label, Method method) {
        super("Label " + label + " not found in " + method.name());
        this.label = label;
        this.method = method;
    }

    /**
     * Retrieves the label that was not found.
     *
     * @return The label associated with this exception
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Retrieves the method in which the label was not found.
     *
     * @return The method associated with this exception
     */
    public Method getMethod() {
        return method;
    }
}