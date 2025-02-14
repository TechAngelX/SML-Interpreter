package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Label;
import sml.Machine;

import java.util.Optional;

public class AddInstruction extends Instruction {
    public static final String OP_CODE = "add";

    public AddInstruction(Label label) {
        super(label, OP_CODE);
    }

    // ==================================== Methods ======================================


    /**
     * Adds two numbers from the stack, adhering to the LIFO
     * Stack property - Last In, First Out.
     *
     * @param machine where our stack lives
     * @return Optional containing next frame after execution
     */
    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();

        int value1 = frame.pop();
        int value2 = frame.pop();

        frame.push(value1 + value2);
        return Optional.of(frame.advance());
    }


    @Override
    protected String getOperandsString() {
        return "";
    }
}
