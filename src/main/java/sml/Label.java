package sml;

import java.util.Objects;

/**
 * Represents a label in the Simple Machine Language.
 *
 * <p>A strongly-typed, immutable wrapper for label strings, ensuring
 * type safety and preventing null labels.</p>
 *
 * <p>Key Benefits:</p>
 * <ul>
 *   <li>Immutability prevents unintended modifications</li>
 *   <li>Null-safety through compact constructor</li>
 *   <li>Provides semantic meaning beyond raw strings</li>
 * </ul>
 *
 * @author Ricki Angel
 */
// Answer to Coursework Question: What are the benefits of using this record class?
//
// - Immutability & Type Safety: Prevents accidental modification and distinguishes labels from plain Strings.
// - Built-in Methods: Auto-generates `equals`, `hashCode`, and `toString`, reducing boilerplate.
// - Null-Safety**: Ensures labels are never null via this `Objects.requireNonNull(label)` method.
//
//   Comparison with `String`:
// - Using `String`: No enforced meaning, allows null values, and risks misuse.
// - Using `Label`: Stronger type distinction, enforced validity, and safer usage.


public record Label(String label) {
    /**
     * Ensures the label is not null during instantiation.
     *
     * @throws NullPointerException if the label is null
     */
    public Label {
        Objects.requireNonNull(label);
    }

    /**
     * Returns the string value of the label.
     *
     * @return the label as a string
     */
    public String toString() {
        return label;
    }

// Answer to Coursework Question: Do we need to override .equals and .hashCode here?

// We do not need to override equals() and hashCode(),
// as records automatically generate them.
//
//  For label comparisons in instructions:
// - Automatically ensures labels equal when string values match
// - Generates consistent hashCode() based on string value
//
// Default record implementation handles this correctly
// by using String's comparison methods.
}
