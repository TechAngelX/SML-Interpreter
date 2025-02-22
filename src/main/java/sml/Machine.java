package sml;

import sml.instruction.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the machine, the context in which programs run.
 * <p>
 * An instance contains the program (as a table with methods) and
 * a program counter. The program counter represents the current
 * instruction and the call stack in the program execution.
 */
public final class Machine {

    private SymbolTable<Method.Identifier, Method> program;

    // The current frame contains the current method name (with a list of instructions),
    // its arguments and local variables, the operand stack and
    // the program counter (the index of the instruction to be executed next)
    private Optional<Frame> frame;

    /**
     * Execute the program starting from method "main".
     * Precondition: the program has been stored properly.
     */
    public void execute() {
        while (frame.isPresent()) {
            Frame f = frame.get();
            Instruction instruction = f.currentInstruction();
            System.out.println("[" + f + "] " + instruction);
            // TODO: Add exception handling for missing labels, etc.
            //       Produce user-friendly error messages.
            //       You may need to extend the functionality of the exception classes.
            frame = instruction.execute(this);
        }
    }

    public void setProgram(Collection<Method> methods) {
        program = SymbolTable.of(methods.stream()
                .collect(Collectors.toMap(Method::name, m -> m)));
        frame = Optional.empty();
        frame = newFrameForMethodInvocation(new Method.Identifier("@main"));
    }


    public Frame frame() {
        return frame.get();
    }

    public Optional<Frame> newFrameForMethodInvocation(Method.Identifier methodName) {
        Method method = program.get(methodName)
                .orElseThrow(() -> new MethodNotFoundException(methodName));

        Frame newFrame = new Frame(method, frame.orElse(null));

        if (frame.isPresent()) {
            Frame currentFrame = frame.get();
            List<Variable.Identifier> methodArguments = newFrame.method().arguments();

            // Check if there are enough elements on the stack
            if (methodArguments.size() > currentFrame.stackSize()) {
                throw new IllegalStateException("Not enough arguments on the stack for method " + methodName +
                        ". Required: " + methodArguments.size() + ", Available: " + currentFrame.stackSize());
            }

            // Pop arguments from the stack in reverse order
            for (int i = methodArguments.size() - 1; i >= 0; i--) {
                Variable.Identifier var = methodArguments.get(i);
                int value = currentFrame.pop();
                Variable variable = newFrame.arguments().get(var)
                        .orElseThrow(() -> new AssertionError("Variable " + var + " not found (can never happen)"));
                variable.store(value);
            }
        }
        return Optional.of(newFrame);
    }

    /**
     * String representation of the program under execution.
     *
     * @return pretty formatted version of the code.
     */
    @Override
    public String toString() {
        // TODO: implement
        return "";
    }

}
