package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;
import sml.helperfiles.DefaultInstructionRegistrationLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ModInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private Machine machine;
    private Translator translator;

    @BeforeEach
    void setUp() {
        InstructionFactory factory = new InstructionFactory(new DefaultInstructionRegistrationLogger());

        machine = new Machine();
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
    }

    @Test
    @DisplayName("Should dynamically create ModInstruction through InstructionFactory based on Opcode")
    public void testModInstructionIsRegistered() {
        Instruction instruction = InstructionFactory.createInstruction("mod", new Label("test"));
        assertNotNull(instruction);
        assertInstanceOf(ModInstruction.class, instruction);
    }

    @Test
    @DisplayName("Should throw ArithmeticException when dividing by zero")
    void testModuloByZero() {
        Instruction modInstruction = new ModInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(modInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(50);
        machine.frame().push(0);

        assertThrows(ArithmeticException.class,
                () -> modInstruction.execute(machine),
                "Modulo by zero should throw ArithmeticException"
        );
    }

    @Test
    @DisplayName("Should correctly calculate modulo of two numbers from the stack")
    void testExecuteModInstruction() {
        Instruction modInstruction = new ModInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(modInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(17);
        machine.frame().push(5);

        Optional<Frame> nextFrame = modInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(2, result, "17 % 5 should equal 2");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Correctly handles modulo operation with negative dividend")
    void testModuloWithNegativeDividend() {
        Instruction modInstruction = new ModInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(modInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(-17);
        machine.frame().push(5);

        Optional<Frame> nextFrame = modInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(-2, result, "-17 % 5 should equal -2 in Java");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Test modulo operation with mod opcode")
    void testModuloOperationWithModOpcode() throws IOException {
        String program = """
                @main:
                push 55
                push 9
                mod     // 55 % 9 = 1
                print
                
                push 144
                push 12
                mod     // 144 % 12 = 0
                print
                
                push 27
                push 4
                mod     // 27 % 4 = 3
                print
                
                push 0
                return
                """;

        String filePath = createTempSmlFile("modulo_test_mod_opcode.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        String output = outContent.toString();
        assertTrue(output.contains("1"), "Result should be 1 (55 % 9)");
        assertTrue(output.contains("0"), "Result should be 0 (144 % 12)");
        assertTrue(output.contains("3"), "Result should be 3 (27 % 4)");
    }

    private String createTempSmlFile(String filename, String content) throws IOException {
        return null;
    }
}
