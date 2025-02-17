package sml;

import java.util.Objects;

/**
 * Represents a variable in the Simple Machine Language (SML).
 * ============================================================================================
 * It provides methods to store and retrieve the value.
 * A variable is identified by its name (using an Identifier) and can store and load integer
 * values using the store and load methocds.The variable is a storage location that can hold
 * an integer value. The variable's name is managed (encapsulated?) by the nested Identifier
 * record, which ensures the name is not null and provides a unique identifier for the variable.
 */

public class Variable {

    public record Identifier(String name) {
        public Identifier {
            Objects.requireNonNull(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private int value;

    public void store(int value) {
        this.value = value;
    }

    public int load() {
        return value;
    }
}
