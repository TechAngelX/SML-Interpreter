package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Machine;

import java.util.Optional;

public class loadInstruction extends Instruction {


    public class LoadInstrucction ()
    @Override
    public Optional<Frame> execute(Machine machine) {
        return Optional.empty();
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
