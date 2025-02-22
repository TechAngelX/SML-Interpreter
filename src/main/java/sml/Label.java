package sml;

import java.util.Objects;

/**
 * ================================================================
 * Represents a label in the Simple Machine Language (SML).
 * ================================================================
 * <p>
 * A strongly-typed, immutable wrapper for label strings, ensuring
 * type safety and preventing null labels.
 * <p>
 * Key Benefits:
 * - Immutability prevents unintended modifications.
 * - Null-safety through compact constructor.
 * - Provides semantic meaning beyond raw strings.
 *
 * @author Ricki Angel
 */

// ================================================================
// Benefits of Using a `record` Class for `Label`
// ================================================================
//
// - **Immutability & Type Safety**: Prevents accidental modification and distinguishes labels from plain Strings.
// - **Built-in Methods**: Auto-generates `equals`, `hashCode`, and `toString`, reducing boilerplate.
// - **Null-Safety**: Ensures labels are never null via `Objects.requireNonNull(label)`.
//
// **Comparison with `String`:**
// - Using `String`: No enforced meaning, allows null values, and risks misuse.
// - Using `Label`: Stronger type distinction, enforced validity, and safer usage.


public record Label(String label) {

    public Label {
        Objects.requireNonNull(label);
    }

    public String toString() {
        return label;
    }


// We do not need to override equals() and hashCode(),
// as records automatically generate them.
//
// For label comparisons in instructions:
// - Automatically ensures labels equal when string values match
// - Generates consistent hashCode() based on string value
//
// Default record implementation handles this correctly
// by using String's comparison methods.
}
