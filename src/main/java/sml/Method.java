package sml;

import sml.instructions.Instruction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a method in the SML program.
 *
 * <p>A method consists of a unique identifier, a list of arguments, a set of local variables,
 * and a sequence of instructions to execute.</p>
 *
 * <p>The method's identifier must start with a '@' character, and the method may contain arguments,
 * local variables, and instructions. The constructor ensures that arguments are unique, that the instructions
 * contain at least one element, and that any labels in the instructions are mapped to their respective indices.</p>
 *
 * @author Ricki Angel
 */
public class Method {
    /**
     * Represents a unique identifier for a method.
     *
     * <p>Ensures method names start with '@' and provides a string representation
     * that omits the '@' symbol when creating the identifier.</p>
     */
    public record Identifier(String name) {
        public Identifier {
            if (name.charAt(0) != '@')
                throw new IllegalArgumentException("Method identifier name must start with @");
            name = name.substring(1);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Identifier name;
    private final List<Variable.Identifier> arguments;
    private final Set<Variable.Identifier> localVariables;
    private final List<Instruction> instructions;
    private final SymbolTable<Label, Integer> labels;

    /**
     * Constructs a new Method instance with the specified name, arguments, and instructions.
     *
     * <p>The constructor validates that the method's arguments are unique and that the method
     * has at least one instruction. It also computes the local variables and labels associated
     * with the method based on the provided instructions.</p>
     *
     * @param name the unique identifier for the method
     * @param arguments the list of method arguments
     * @param instructions the sequence of instructions for the method
     * @throws IllegalArgumentException if arguments are duplicated or no instructions are provided
     */
    public Method(Identifier name, List<Variable.Identifier> arguments, List<Instruction> instructions) {
        this.name = name;
        this.arguments = List.copyOf(arguments);
        this.instructions = List.copyOf(instructions);

        Map<Variable.Identifier, Long> argumentOccurrences = this.arguments.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        if (argumentOccurrences.entrySet().stream().anyMatch(e -> e.getValue() > 1))
            throw new IllegalArgumentException("Duplicate arguments: " +
                    argumentOccurrences.entrySet().stream()
                            .filter(e -> e.getValue() > 1)
                            .map(Map.Entry::getKey)
                            .toList());

        this.localVariables = this.instructions.stream()
                .flatMap(Instruction::variables)
                .filter(v -> !argumentOccurrences.containsKey(v))
                .collect(Collectors.toSet());

        if (this.instructions.isEmpty())
            throw new IllegalArgumentException("No instructions found");

        this.labels = SymbolTable.of(IntStream.range(0, this.instructions.size())
                .mapToObj(idx -> this.instructions
                        .get(idx)
                        .optionalLabel()
                        .stream()
                        .map(label -> Map.entry(label, idx)))
                .flatMap(s -> s)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    /**
     * Returns the unique identifier for the method.
     *
     * @return the method identifier
     */
    public Identifier name() {
        return name;
    }

    /**
     * Returns the list of instructions for the method.
     *
     * @return the list of instructions
     */
    public List<Instruction> instructions() {
        return instructions;
    }

    /**
     * Returns the symbol table of labels associated with the method's instructions.
     *
     * @return the symbol table of labels
     */
    public SymbolTable<Label, Integer> labels() {
        return labels;
    }

    /**
     * Returns the list of method arguments.
     *
     * @return the list of arguments
     */
    public List<Variable.Identifier> arguments() {
        return arguments;
    }

    /**
     * Returns the set of local variables used by the method.
     *
     * @return the set of local variables
     */
    public Set<Variable.Identifier> localVariables() {
        return localVariables;
    }

    /**
     * Returns a string representation of the method, including its name, arguments,
     * local variables, and instructions.
     *
     * @return a string representation of the method
     */
    @Override
    public String toString() {
        return String.format("Method @%s(args: %s, locals: %s, instructions: %s)",
                name,
                arguments.stream()
                        .map(Variable.Identifier::toString)
                        .collect(Collectors.joining(", ")),
                localVariables.stream()
                        .map(Variable.Identifier::toString)
                        .collect(Collectors.joining(", ")),
                instructions.stream()
                        .map(Instruction::toString)
                        .collect(Collectors.joining("; ")));
    }

    /**
     * Uses pattern matching to compare this method to another object for equality.
     *
     * <p>Checks if the provided object is an instance of the {@code Method} class,
     * and compares the method's {@code name}, {@code arguments}, and {@code instructions}
     * to determine equality.</p>
     *
     * @param obj the object to compare this method with
     * @return {@code true} if the object is a {@code Method} and has the same {@code name},
     *         {@code arguments}, and {@code instructions} as this method, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Method other &&
                this.name.equals(other.name) &&
                this.arguments.equals(other.arguments) &&
                this.instructions.equals(other.instructions);
    }

    /**
     * Returns the hash code for this method.
     *
     * @return the hash code for this method
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, arguments, instructions);
    }
}