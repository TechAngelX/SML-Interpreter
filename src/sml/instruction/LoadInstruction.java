package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;

public class LoadInstruction extends Instruction {
    public static final String OP_CODE = "load";

    private final Variable.Identifier varName;

    public LoadInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE);
        this.varName = Objects.requireNonNull(varName);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        Variable variable = frame.variable(varName);
        frame.push(variable.load());
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return varName.toString();
    }


}