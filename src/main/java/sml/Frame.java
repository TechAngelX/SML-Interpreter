package sml;

import sml.instructions.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the execution context for a method in the SML runtime environment.
 *
 * <p>The Frame class manages the complete state of a method during its execution, including:</p>
 * <ul>
 *   <li>Program counter tracking</li>
 *   <li>Method arguments and local variables</li>
 *   <li>Operand stack management</li>
 *   <li>Instruction execution context</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Tracks the current execution point within a method</li>
 *   <li>Provides access to method-specific variables</li>
 *   <li>Manages a stack for intermediate computational values</li>
 *   <li>Supports method invocation and context switching</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class Frame {
    private final Method method;
    private int programCounter;

    private final SymbolTable<Variable.Identifier, Variable> arguments;
    private final SymbolTable<Variable.Identifier, Variable> localVariables;
    private final Deque<Integer> stack;
    private final Frame invoker;

    // Answer to Coursework Question: 3 data structures and mutable components.
    // Explain what parts of the data structure are mutable (and what are the mutator methods).

    // 1. SymbolTable<Variable.Identifier, Variable> arguments:
    //    - A Map data sctructure which stores method args.
    //    - Modified through Variable's mutator methods.
    //    - Can modify individual variable values
    //    - The table itself is final (cannot be reassigned)

    // 2. SymbolTable<Variable.Identifier, Variable> localVariables:
    //    - Again, a Map DS which stores local variables.
    //    - Modified through Variable's mutator methods.
    //    - Can modify individual variable values
    //    - The table itself is final (cannot be reassigned)

    // 3. Deque<Integer> stack:
    //   - Mutable structure (elements can be added/removed) - double-ended?.
    //   - Modified by push() and pop() methods.
    //   - The stack itself is final (cannot be reassigned)

    /**
     * Constructs a new Frame for a specific method.
     *
     * <p>Initializes the frame with:</p>
     * <ul>
     *   <li>Method-specific arguments</li>
     *   <li>Local variables</li>
     *   <li>Empty operand stack</li>
     *   <li>Tracking of the invoking frame</li>
     * </ul>
     *
     * @param method  The method to be executed in this frame
     * @param invoker The frame that invoked this method (can be null)
     * @throws NullPointerException if method is null
     */

    public Frame(Method method, Frame invoker) {
        this.method = Objects.requireNonNull(method);
        this.programCounter = 0;

        this.arguments = SymbolTable.of(method.arguments().stream()
                .collect(Collectors.toMap(v -> v, v -> new Variable())));
        this.localVariables = SymbolTable.of(method.localVariables().stream()
                .collect(Collectors.toMap(v -> v, v -> new Variable())));
        this.stack = new ArrayDeque<>();

        this.invoker = invoker;
    }

    /**
     * Advances the program counter to the next instruction.
     *
     * @return Next frame if available, null if no more instructions
     */
    public Frame advance() {
        if (programCounter + 1 >= method.instructions().size()) {
            return null;
        }
        return setProgramCounter(programCounter + 1);
    }

    /**
     * Jumps to a specific labeled instruction within the method.
     *
     * @param label Target instruction label
     * @return Frame at the labeled instruction
     * @throws LabelNotFoundException if label is not found in method
     */

    public Frame jumpTo(Label label) {
        Optional<Integer> pc = method.labels().get(label);
        if (pc.isEmpty())
            throw new LabelNotFoundException(label, method);

        return setProgramCounter(pc.get());
    }

    private Frame setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
        Objects.checkIndex(programCounter, method.instructions().size());
        return this;
    }

    public Method method() {
        return method;
    }

    public Instruction currentInstruction() {
        return method.instructions().get(programCounter);
    }

    public int programCounter() {
        return programCounter;
    }

    public Optional<Frame> invoker() {
        return Optional.ofNullable(invoker);
    }

    /**
     * Retrieves a variable from local or argument scopes.
     *
     * @param identifier Variable identifier to locate
     * @return The variable associated with the identifier
     * @throws VariableNotFoundException if variable is not found
     */

    public Variable variable(Variable.Identifier identifier) {
        return localVariables.get(identifier)
                .or(() -> arguments.get(identifier))
                .orElseThrow(() -> new VariableNotFoundException(identifier));
    }

    public SymbolTable<Variable.Identifier, Variable> arguments() {
        return arguments;
    }

    public SymbolTable<Variable.Identifier, Variable> localVariables() {
        return localVariables;
    }

    public int pop() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException("Cannot pop from an empty stack in method " + method.name());
        }
        return stack.pop();
    }

    public void push(int value) {
        stack.push(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method.name())
                .append(":").append(programCounter);

        if (invoker != null) {
            Frame current = invoker;
            sb.append(" < ");
            while (current != null) {
                sb.append(current.method.name())
                        .append(":").append(current.programCounter);
                current = current.invoker;
                if (current != null) {
                    sb.append(" → ");
                }
            }
        }

        return sb.toString();
    }

    public int stackSize() {
        return stack.size();
    }
}
