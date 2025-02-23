package sml;

import sml.instructions.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ================================================================
 * Represents the virtual machine for Simple Machine Language execution.
 * ================================================================
 *
 * The Machine class maintains and controls program execution state,
 * including methods, execution frames, and program flow control.
 * It serves as the runtime environment for SML programs.
 *
 * @author Ricki Angel
 */
public final class Machine {
    private SymbolTable<Method.Identifier, Method> program;
    private Optional<Frame> frame;


    public void execute() {
        try {
            System.out.println("== Beginning program execution ==\n");

            while (frame.isPresent()) {
                Frame f = frame.get();
                Instruction instruction = f.currentInstruction();

                System.out.println("[" + f + "]  " + instruction);

                frame = instruction.execute(this);
            }

            System.out.println("\n== Ending Program Execution ==\n");
        } catch (MethodNotFoundException e) {
            System.err.println("Error: Method not found - " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error: Illegal state - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during execution: " + e.getMessage());
            e.printStackTrace();
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

            if (methodArguments.size() > currentFrame.stackSize()) {
                throw new IllegalStateException("Not enough arguments on the stack for method " + methodName +
                        ". Required: " + methodArguments.size() + ", Available: " + currentFrame.stackSize());
            }

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
