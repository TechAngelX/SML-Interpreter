package sml.instruction;

import sml.Label;

@FunctionalInterface
public interface InstructionConstructor<T> {
    Instruction create(Label label, T parameter) throws Exception;
}
