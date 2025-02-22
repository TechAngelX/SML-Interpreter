package sml;

import sml.instruction.Instruction;

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
            System.out.println("Beginning program execution.");

            while (frame.isPresent()) {
                Frame f = frame.get();
                Instruction instruction = f.currentInstruction();

                System.out.println("[" + f + "]  " + instruction);

                frame = instruction.execute(this);
            }

            System.out.println("\n== Ending Program Execution ==");
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
    /**
     * ================================================================
     * Provides string representation of the program state
     * ================================================================
     *
     * Returns a human-readable string showing the current execution state
     * including all nested calls in the call stack.
     *
     * @return Formatted string explaining the execution state
     */
    @Override
    public String toString() {
        if (program == null || program.isEmpty()) {
            return "No program loaded";
        }

        return frame.map(currentFrame -> {
            StringBuilder description = new StringBuilder();

            // Build the technical nested frame representation first
            StringBuilder technicalStack = new StringBuilder();
            Frame current = currentFrame;

            technicalStack.append(current.method().name())
                    .append(", l ")
                    .append(current.programCounter());

            // Count nesting level and collect frames
            int nestingLevel = 0;
            Frame temp = current;
            while (temp.invoker().isPresent()) {
                temp = temp.invoker().get();
                technicalStack.append(" (")
                        .append(temp.method().name())
                        .append(", l ")
                        .append(temp.programCounter())
                        .append(")");
                nestingLevel++;
            }

            // Add human-readable description
            description.append("Currently executing ")
                    .append(current.method().name())
                    .append(" at instruction ")
                    .append(current.programCounter());

            if (nestingLevel > 0) {
                description.append("\nCall stack depth: ")
                        .append(nestingLevel + 1)
                        .append(" frames\n");
                description.append("This is part of a nested call sequence:\n");
                description.append("→ Started in main\n");

                // Add arrow depth for visual hierarchy
                Frame descFrame = current;
                while (descFrame.invoker().isPresent()) {
                    descFrame = descFrame.invoker().get();
                    description.append("  → Waiting for ")
                            .append(descFrame.method().name())
                            .append(" to complete at instruction ")
                            .append(descFrame.programCounter())
                            .append("\n");
                }
            }

            description.append("\nTechnical representation: ")
                    .append(technicalStack);

            return description.toString();
        }).orElse("No active frame");
    }
}
