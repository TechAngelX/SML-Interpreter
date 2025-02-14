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

    @Override
    public Optional<Frame> execute(Machine machine) {
        return Optional.empty();
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
