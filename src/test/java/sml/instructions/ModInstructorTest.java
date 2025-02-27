package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;
import sml.helperfiles.DefaultInstructionRegistrationLogger;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ModInstructionTest {
    private Machine machine;

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
        assertTrue(instruction instanceof ModInstruction);
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
    
    

    
}
