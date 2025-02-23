package sml;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ====================================================================================================================
 * The Frame class represents a method's execution context in Simple Machine Language (SML).
 * --------------------------------------------------------------------------------------------------------------------
 * Manages the state of a single method execution including its program counter, arguments, local variables,
 * and operand stack. Provides functionality for instruction execution, variable access, and stack operations.
 * This class is central to SML's execution model, maintaining the runtime environment for each method call.
 * ====================================================================================================================
 *
 * @author Ricki Angel
 */
public class Frame {
    private final Method method;
    private int programCounter;
    /**
     * ================================================================
     * Frame's Mutable Data Structures:
     * ================================================================
     *
     * 1. SymbolTable<Variable.Identifier, Variable> arguments:
     *    - Contains mutable Variable objects (values can change).
     *    - Modified through Variable's mutator methods.
     *
     * 2. SymbolTable<Variable.Identifier, Variable> localVariables:
     *    - Contains mutable Variable objects (values can change).
     *    - Modified through Variable's mutator methods.
     *
     * 3. Deque<Integer> stack:
     *    - Mutable structure (elements can be added/removed).
     *    - Modified by push() and pop() methods.
     */
    private final SymbolTable<Variable.Identifier, Variable> arguments;
    private final SymbolTable<Variable.Identifier, Variable> localVariables;
    private final Deque<Integer> stack;

    private final Frame invoker;

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

    public Frame advance() {
        if (programCounter + 1 >= method.instructions().size()) {
            return null;
        }
        return setProgramCounter(programCounter + 1);
    }
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

    public SymbolTable<Variable.Identifier, Variable> arguments() {
        return arguments;
    }

    public SymbolTable<Variable.Identifier, Variable> localVariables() {
        return localVariables;
    }

    public Variable variable(Variable.Identifier identifier) {
        return localVariables.get(identifier)
                .or(() -> arguments.get(identifier))
                .orElseThrow(() -> new VariableNotFoundException(identifier));
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
        return method.name() + ", l "
                + programCounter
                + Optional.ofNullable(invoker)
                .map(pc -> " (" + pc + ")")
                .orElse("");
    }

    public int stackSize() {
        return stack.size();
    }
}
