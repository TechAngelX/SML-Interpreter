
package sml;

import sml.instruction.Instruction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * =====================================================================================
 * Represents a method in the Simple Machine Language (SML) program.
 * -------------------------------------------------------------------------------------
 * A method consists of a unique identifier, a list of arguments, a set of local variables,
 * and a sequence of instructions to execute.
 *
 * @author Ricki Angel
 */
public class Method {
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

    public Identifier name() {
        return name;
    }

    public List<Instruction> instructions() {
        return instructions;
    }

    public SymbolTable<Label, Integer> labels() {
        return labels;
    }

    public List<Variable.Identifier> arguments() {
        return arguments;
    }

    public Set<Variable.Identifier> localVariables() {
        return localVariables;
    }

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
}