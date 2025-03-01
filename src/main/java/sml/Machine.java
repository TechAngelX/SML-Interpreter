package sml;

import org.springframework.stereotype.Component;
import sml.instructions.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the virtual machine for SML execution.
 *
 * <p>The Machine class maintains and controls program execution state,
 * including methods, execution frames, and program flow control.
 * It serves as the runtime environment for SML programs.</p>
 *
 * @author Ricki Angel
 */
@Component
public final class Machine {
    private SymbolTable<Method.Identifier, Method> program;
    private Optional<Frame> frame;

    /**
     * Executes the loaded SML program.
     *
     * <p>Manages program execution by iterating through instructions,
     * handling frame transitions and potential execution errors.</p>
     */
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
        }
    }

    /**
     * Initialises the program by loading methods into the symbol table.
     *
     * <p>Prepares the machine for execution by converting methods
     * to a symbol table and creating an initial frame for the main method.</p>
     *
     * @param methods Collection of methods to be loaded into the program
     */
    public void setProgram(Collection<Method> methods) {
        program = SymbolTable.of(methods.stream()
                .collect(Collectors.toMap(Method::name, m -> m)));
        frame = Optional.empty();
        frame = newFrameForMethodInvocation(new Method.Identifier("@main"));
    }

    /**
     * Retrieves the current execution frame.
     *
     * <p>Returns the active execution frame.</p>
     *
     * @return The active execution frame
     */
    public Frame frame() {
        return frame.get();
    }

    /**
     * Creates a new frame for method invocation.
     *
     * <p>Manages method call stack and argument passing by locating
     * the method, creating a new execution frame, and handling
     * argument transfer from the current frame.</p>
     *
     * @param methodName Identifier of the method to invoke
     * @return Optional containing the new execution frame
     * @throws MethodNotFoundException if the specified method cannot be found in the program
     * @throws IllegalStateException if insufficient arguments are available on the stack for method invocation
     */
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

    @Override
    public String toString() {
        if (program == null) return "No program loaded";

        int methodCount = program.values().size();
        int totalInstructions = program.values().stream()
                .mapToInt(method -> method.instructions().size())
                .sum();

        return String.format("Program: %d methods, %d total instructions",
                methodCount, totalInstructions);
    }
}
