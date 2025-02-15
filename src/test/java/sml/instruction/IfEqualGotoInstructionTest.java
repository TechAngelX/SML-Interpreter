package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IfEqualGotoInstruction to verify:
 * - Correctly compares two values for equality
 * - Jumps to label when values are equal
 * - Continues to next instruction when values are not equal
 * - Handles stack operations properly
 */