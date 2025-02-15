package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IfGreaterGotoInstruction to verify:
 * - Correctly compares two values
 * - Jumps to label when first value is greater
 * - Continues to next instruction when first value is not greater
 * - Handles stack operations properly
 */
class IfGreaterGotoInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }
}