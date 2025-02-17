package sml;

import sml.instruction.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * The Translator class, translates Simple Machine Language (SML) programs into a collection of {@link Method} objects.
 * ====================================================================================================================
 *
 * This is part of an SML interpreter that reads, translates, and prepares programs for execution. The translation
 * process involves parsing each line of the input to identify labels, opcodes,
 * and operands, then creating corresponding {@link Instruction} objects grouped under methods.
 */
public final class Translator {

    // line contains the characters in the current line that's not been processed yet
    private String line = "";

    private static class State {
        final Method.Identifier methodName;
        final List<Instruction> instructions;
        final List<Variable.Identifier> arguments;

        State(Method.Identifier methodName) {
            this.methodName = methodName;
            instructions = new ArrayList<>();
            arguments = new ArrayList<>();
        }

        Method createMethod() {
            return new Method(methodName, arguments, instructions);
        }

        void addArgument(String name) {
            Variable.Identifier id = new Variable.Identifier(name);
            arguments.add(id);
        }
    }

    private static final String ITEM_SEPARATOR = ",";
    private static final String METHOD_LABEL = "@";

    public Collection<Method> readAndTranslate(String fileName) throws IOException {

        Collection<Method> methods = new ArrayList<>();

        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            // each iteration processes the contents of line
            // and reads the next input line into "line"
            State state = null;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String labelString = getLabel();
                if (labelString != null && labelString.startsWith(METHOD_LABEL)) {
                    if (state != null)
                        methods.add(state.createMethod());

                    state = new State(new Method.Identifier(labelString));
                    for (String s = scan(); !s.isEmpty(); s = scan()) {

                        String variable = s.endsWith(ITEM_SEPARATOR)
                                ? s.substring(0, s.length() - 1).trim()
                                : s;

                        state.addArgument(variable);

                        if (!s.endsWith(ITEM_SEPARATOR))
                            break;
                    }
                }
                else {
                    Label label = labelString != null
                            ? new Label(labelString)
                            : null;

                    Instruction instruction = getInstruction(label);
                    if (instruction != null) {
                        if (state != null)
                            state.instructions.add(instruction);
                        else
                            throw new IllegalArgumentException("Instructions cannot appear outside methods " + labelString + " " + instruction);
                    }
                }
            }
            if (state != null)
                methods.add(state.createMethod());
        }
        return methods;
    }

    // Refactor with helper guide determining whether the instruction needs to return (State change) or not.
    private Instruction getInstruction(Label label) {
        String opcode = scan();
        if (opcode.isEmpty())
            return null;

        return switch (opcode) {
            case GotoInstruction.OP_CODE -> {
                String s = scan(); // State Change
                yield new GotoInstruction(label, new Label(s));
            }
            case ReturnInstruction.OP_CODE -> new ReturnInstruction(label);
            case InvokeInstruction.OP_CODE -> {
                String s = scan(); // State Change
                yield new InvokeInstruction(label, new Method.Identifier(s));
            }
            case PrintInstruction.OP_CODE -> new PrintInstruction(label);
            case AddInstruction.OP_CODE -> new AddInstruction(label);
            case MultiplyInstruction.OP_CODE -> new MultiplyInstruction(label);
            case DivInstruction.OP_CODE -> new DivInstruction(label);
            case LoadInstruction.OP_CODE -> {
                String varName = scan(); // State Change
                yield new LoadInstruction(label, new Variable.Identifier(varName));
            }
            case IfEqualGotoInstruction.OP_CODE -> {
                String jumpLabelName = scan(); // State Change
                yield new IfEqualGotoInstruction(label, new Label(jumpLabelName));
            }
            case IfGreaterGotoInstruction.OP_CODE -> {
                String jumpLabelName = scan(); // State Change
                yield new IfGreaterGotoInstruction(label, new Label(jumpLabelName));
            }
            default -> {
                yield null;
            }
        };
    }


    // TODO: Then, replace the switch by using the Reflection API

    // TODO: Next, use dependency injection to allow this machine class
    //       to work with different sets of opcodes (different CPUs)

    private String getLabel() {
        String word = scan();
        if (word.endsWith(":"))
            return word.substring(0, word.length() - 1);

        // undo scanning the word
        line = word + " " + line;
        return null;
    }

    /**
     * Returns the first word of line and remove it from line.
     * If there is no word, return "".
     */
    private String scan() {
        line = line.trim();

        int whiteSpacePosition = 0;
        while (whiteSpacePosition < line.length()) {
            if (Character.isWhitespace(line.charAt(whiteSpacePosition)))
                break;
            whiteSpacePosition++;
        }

        String word = line.substring(0, whiteSpacePosition);
        line = line.substring(whiteSpacePosition);
        return word;
    }
}