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

    // TO Clarify, the mutable components are:  
    // programCounter: (int) → Mutable (modified by setProgramCounter(int))  
    // arguments: (SymbolTable<Variable.Identifier, Variable>) → Mutable if Variable is mutable  
    // localVariables: (SymbolTable<Variable.Identifier, Variable>) → Mutable if Variable is mutable  
    // stack: (Deque<Integer>) → Mutable (modified by push(int) and pop())  

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
     * Advances the program counter to the next instruction in the method.
     *
     * @return The next frame if another instruction exists, or null if no more instructions are available
     * @throws IndexOutOfBoundsException if the program counter exceeds the method's instruction list
     */
    public Frame advance() {
        return Optional.of(programCounter + 1)
                .filter(pc -> pc < method.instructions().size())
                .map(this::setProgramCounter)
                .orElse(null);
    }

    /**
     * Jumps to a specific labeled instruction within the current method.
     *
     * @param label The target instruction label to jump to
     * @return The frame positioned at the labeled instruction
     * @throws LabelNotFoundException if the specified label cannot be found in the method
     */

    public Frame jumpTo(Label label) {
        Optional<Integer> pc = method.labels().get(label);
        if (pc.isEmpty())
            throw new LabelNotFoundException(label, method);

        return setProgramCounter(pc.get());
    }

    /**
     * Sets the program counter to a specific instruction index within the method.
     *
     * <p>Updates the current execution point and validates the new index
     * against the method's total number of instructions.</p>
     *
     * @param programCounter The new index to set as the current program counter
     * @return The current frame with updated program counter
     * @throws IndexOutOfBoundsException if the program counter is outside
     *                                   the valid range of method instructions
     */

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
     * Retrieves a variable from local or argument scopes based on its identifier.
     *
     * @param identifier The unique identifier of the variable to retrieve
     * @return The variable associated with the given identifier
     * @throws VariableNotFoundException if no variable matches the identifier
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

    /**
     * Removes and returns the top value from the operand stack.
     *
     * @return The integer value at the top of the stack
     * @throws NoSuchElementException if the stack is empty
     */
    public int pop() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException("Cannot pop from an empty stack in method " + method.name());
        }
        return stack.pop();
    }

    /**
     * Pushes a new integer value onto the top of the operand stack.
     *
     * @param value The integer value to push onto the stack
     */
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
