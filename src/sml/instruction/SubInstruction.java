package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Label;
import sml.Machine;

import java.util.Optional;

public class SubInstruction extends Instruction {
    public static final String OP_CODE = "sub";

    public SubInstruction(Label label) {
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