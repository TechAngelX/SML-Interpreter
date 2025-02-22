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
 * The Translator class reads and parses Simple Machine Language (SML) programs.
 * --------------------------------------------------------------------------------------------------------------------
 * Responsible for reading SML code from source files, parsing language constructs, and converting the program into
 * executable {@link Method} objects with their associated {@link Instruction} objects and arguments. The parser
 * supports method definitions with parameters, labeled instructions for control flow, and various instruction types.
 * This modular design separates parsing from execution concerns, facilitating extensibility with new instruction types.
 * ====================================================================================================================
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

    // Read and translate an SML file into a collection of Method objects:
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
                } else {
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

    // Process the arguments of a method from the current line and adds them to the state:
    private void processMethodArguments(State state) {
        for (String s = scan(); !s.isEmpty(); s = scan()) {
            String variable = s.endsWith(ITEM_SEPARATOR)
                    ? s.substring(0, s.length() - 1).trim()
                    : s;

            state.addArgument(variable);

            if (!s.endsWith(ITEM_SEPARATOR)) break;
        }
    }

    private Instruction getInstruction(Label label) {
        String opcode = scan();
//        System.out.println("Scanned Opcode: '" + opcode + "'"); // For Debugging.
        if (opcode.isEmpty()) return null;

        Instruction instruction;
        switch (opcode) {
            case "add", "sub", "mul", "div", "print", "return" -> {
                instruction = InstructionFactory.createInstruction(opcode, label);
            }
            case "goto" -> {
                String targetLabel = scan();
                instruction = InstructionFactory.createGotoInstruction(label, new Label(targetLabel));
            }
            case "if_cmpeq" -> {
                String targetLabel = scan();
                instruction = InstructionFactory.createIfEqualGotoInstruction(label, new Label(targetLabel));
            }
            case "if_cmpgt" -> {
                String targetLabel = scan();
                instruction = InstructionFactory.createIfGreaterGotoInstruction(label, new Label(targetLabel));
            }
            case "load" -> {
                String varName = scan();
                instruction = InstructionFactory.createLoadInstruction(label, new Variable.Identifier(varName));
            }
            case "store" -> {
                String varName = scan();
                instruction = InstructionFactory.createStoreInstruction(label, new Variable.Identifier(varName));
            }
            case "invoke" -> {
                String methodName = scan();
                instruction = InstructionFactory.createInvokeInstruction(label, new Method.Identifier(methodName));
            }
            case "push" -> {
                int value = Integer.parseInt(scan());
                instruction = InstructionFactory.createPushInstruction(label, value);
            }
            case "sqrt" -> {
                return InstructionFactory.createSquareRootInstruction(label);
            }
            default -> {
                instruction = InstructionFactory.createInstruction(opcode, label);
            }
        }
//        System.out.println("DEBUG: Created instruction: " + (instruction == null ? "NULL" : instruction));

        return instruction;
    }

    /**
     * Extracts and removes a leading label from the line, if present.
     *
     * @return label string without trailing colon, or null if absent
     */

    private String getLabel() {
        String word = scan();
        if (word.endsWith(":")) return word.substring(0, word.length() - 1);

        line = word + " " + line;
        return null;
    }

    /**
     * Returns the first word of line and removes it from line.
     * If there is no word, return "".
     *
     * @return the next word from the current line
     */
    public String scan() {
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