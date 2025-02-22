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
 * ====================================================================================================================
 * The Translator class is responsible for reading a Simple Machine Language (SML) program from a file,
 * parsing the content, and translating it into a collection of {@link Method} objects that represent
 * the program's methods and instructions.
 * ====================================================================================================================
 *
 * The class processes the file line by line, extracting method labels, arguments, and instructions,
 * creating {@link Method} objects with corresponding {@link Instruction} objects as the content of each method.
 *
 *
 * @author Ricki Angel
 */
public final class Translator {

    private String line = "";

    // Holds the current method's name, its instructions, and its arguments while parsing:
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

    public void setLine(String line) {
        this.line = line;
    }

    public void getLine() {
        this.line = line;
    }

    private static final String ITEM_SEPARATOR = ",";
    private static final String METHOD_LABEL = "@";


    // Reads and translates an SML file into a collection of Method objects:
    public Collection<Method> readAndTranslate(String fileName) throws IOException {
        Collection<Method> methods = new ArrayList<>();

        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            State state = null;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String labelString = getLabel();

                if (labelString != null && labelString.startsWith(METHOD_LABEL)) {
                    if (state != null) methods.add(state.createMethod());

                    state = new State(new Method.Identifier(labelString));
                    processMethodArguments(state);
                }
                else {
                    Label label = labelString != null ? new Label(labelString) : null;
                    Instruction instruction = getInstruction(label);

                    if (instruction != null) {
                        if (state != null)
                            state.instructions.add(instruction);
                        else
                            throw new IllegalArgumentException("Instructions cannot appear outside methods " + labelString + " " + instruction);
                    }
                }
            }
            if (state != null) methods.add(state.createMethod());
        }
        return methods;
    }

    // Processes the arguments of a method from the current line and adds them to the state:
    private void processMethodArguments(State state) {
        for (String s = scan(); !s.isEmpty(); s = scan()) {
            String variable = s.endsWith(ITEM_SEPARATOR)
                    ? s.substring(0, s.length() - 1).trim()
                    : s;

            state.addArgument(variable);

            if (!s.endsWith(ITEM_SEPARATOR)) break;
        }
    }

    // Matches an instructions opcode with corresponding instruction creation method
    private Instruction getInstruction(Label label) {
        String opcode = scan();
        if (opcode.isEmpty()) return null;

        return switch (opcode) {
            case GotoInstruction.OP_CODE -> createGotoInstruction(label);
            case ReturnInstruction.OP_CODE -> new ReturnInstruction(label);
            case InvokeInstruction.OP_CODE -> createInvokeInstruction(label);
            case PrintInstruction.OP_CODE -> new PrintInstruction(label);
            case AddInstruction.OP_CODE -> new AddInstruction(label);
            case MultiplyInstruction.OP_CODE -> new MultiplyInstruction(label);
            case DivInstruction.OP_CODE -> new DivInstruction(label);
            case LoadInstruction.OP_CODE -> createLoadInstruction(label);
            case IfEqualGotoInstruction.OP_CODE -> createIfEqualGotoInstruction(label);
            case IfGreaterGotoInstruction.OP_CODE -> createIfGreaterGotoInstruction(label);
            case PushInstruction.OP_CODE -> createPushInstruction(label);
            case SubInstruction.OP_CODE -> createSubInstruction(label);
            default -> null;
        };
    }

    // Helper methods for state-changing instructions like Goto, push etc.
    private GotoInstruction createGotoInstruction(Label label) {
        String targetLabel = scan();
        return new GotoInstruction(label, new Label(targetLabel));
    }

    private ReturnInstruction createReturnInstruction(Label label) {
        return new ReturnInstruction(label);
    }

    private InvokeInstruction createInvokeInstruction(Label label) {
        String methodName = scan();
        return new InvokeInstruction(label, new Method.Identifier(methodName));
    }

    private PrintInstruction createPrintInstruction(Label label) {
        return new PrintInstruction(label);
    }

    private AddInstruction createAddInstruction(Label label) {
        return new AddInstruction(label);
    }

    private MultiplyInstruction createMultiplyInstruction(Label label) {
        return new MultiplyInstruction(label);
    }

    private DivInstruction createDivInstruction(Label label) {
        return new DivInstruction(label);
    }

    private LoadInstruction createLoadInstruction(Label label) {
        String varName = scan();
        return new LoadInstruction(label, new Variable.Identifier(varName));
    }

    private IfEqualGotoInstruction createIfEqualGotoInstruction(Label label) {
        String jumpLabelName = scan();
        return new IfEqualGotoInstruction(label, new Label(jumpLabelName));
    }

    private IfGreaterGotoInstruction createIfGreaterGotoInstruction(Label label) {
        String jumpLabelName = scan();
        return new IfGreaterGotoInstruction(label, new Label(jumpLabelName));
    }

    private PushInstruction createPushInstruction(Label label) {
        int value = Integer.parseInt(scan());
        return new PushInstruction(label, value);
    }

    private SubInstruction createSubInstruction(Label label) {
        return new SubInstruction(label);
    }

    private String getLabel() {
        String word = scan();
        if (word.endsWith(":")) return word.substring(0, word.length() - 1);

        // Undo scanning the word
        line = word + " " + line;
        return null;
    }

    /**
     * Returns the first word of line and removes it from line.
     * If there is no word, return "".
     */
    private String scan() {
        line = line.trim();

        int whiteSpacePosition = 0;
        while (whiteSpacePosition < line.length()) {
            if (Character.isWhitespace(line.charAt(whiteSpacePosition))) break;
            whiteSpacePosition++;
        }

        String word = line.substring(0, whiteSpacePosition);
        line = line.substring(whiteSpacePosition);
        return word;
    }
}