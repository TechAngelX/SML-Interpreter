package sml;

import java.util.Objects;

/**
 * Represents a variable in the Simple Machine Language.
 *
 * <p>A variable is identified by its name and can store and retrieve integer values. The variable's
 * name is managed by the nested {@link Identifier} record, ensuring the name is non-null and unique.
 * The variable can store an integer value using the {@code store} method and load the stored value using
 * the {@code load} method.</p>
 *
 * @author Ricki Angel
 * @version 1.0
 */
public class Variable {
    /**
     * Represents the identifier for a variable.
     *
     * <p>This record encapsulates the variable's name and ensures that the name is non-null. The name
     * provides a unique identifier for the variable.</p>
     *
     * @param name The unique name of the variable
     */
    public record Identifier(String name) {
        /**
         * Canonical constructor that ensures the variable name is not null.
         *
         * @throws NullPointerException if the name is null
         */
        public Identifier {
            Objects.requireNonNull(name);
        }

        /**
         * Returns a string representation of the variable's name.
         *
         * @return The name of the variable
         */
        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * The current integer value stored in the variable.
     * Initialized to 0 by default.
     */
    private int value;

    /**
     * Stores a new integer value in the variable.
     *
     * @param value The integer value to be stored
     */
    public void store(int value) {
        this.value = value;
    }

    /**
     * Retrieves the current value stored in the variable.
     *
     * @return The current integer value of the variable
     */
    public int load() {
        return value;
    }
}