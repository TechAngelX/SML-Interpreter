package sml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sml.instructions.Instruction;
import sml.services.FileService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles the orchestration of reading the file, managing state transitions, and adding methods to the program.
 * <p>
 * Responsible for reading SML code from source files, parsing language constructs, and converting the program into
 * executable {@link Method} objects with their associated {@link Instruction} objects and arguments. The parser
 * supports method definitions with parameters, labeled instructions for control flow, and various instruction types.
 * This modular design separates parsing from execution concerns, facilitating extensibility with new instruction types.
 *
 * @author Ricki Angel
 */
@Component
public final class Translator {
    private final FileService fileService;
    private String line = "";

    /**
     * Constructs a Translator with the given FileService.
     * <p>
     * This constructor injects the FileService dependency for file operations.
     * </p>
     *
     * @param fileService the FileService instance
     */
    @Autowired
    public Translator(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * This constructor is provided for situations where automatic DI is not used.
     * <p>
     * Provides a No-arguments constructor for backward compatibility with manual Dependency Injection (DI).
     * </p>
     */
    public Translator() {
        this.fileService = null;
    }

    // Holds the current method's name, its instructions, and its arguments while parsing:
    private static class State {
        final Method.Identifier methodName;
        final List<Instruction> instructions;
        final List<Variable.Identifier> arguments;

        /**
         * Constructor for creating a new parsing state with a method identifier.
         *
         * @param methodName the method identifier
         */
        State(Method.Identifier methodName) {
            this.methodName = methodName;
            instructions = new ArrayList<>();
            arguments = new ArrayList<>();
        }
        /**
         * Creates a Method object from the current state.
         * <p>
         * This method returns a Method with its name, arguments, and instructions.
         * </p>
         *
         * @return a newly created Method object
         */
        Method createMethod() {
            return new Method(methodName, arguments, instructions);
        }
        /**
         * Adds an argument to the method's argument list.
         *
         * @param name the argument's name
         */
        void addArgument(String name) {
            Variable.Identifier id = new Variable.Identifier(name);
            arguments.add(id);
        }
    }
    private static final String ITEM_SEPARATOR = ",";
    private static final String METHOD_LABEL = "@";

    /**
     * Reads and translates a .sml file into a collection of Method objects.
     * <p>
     * This method reads the SML program from the specified file, processes the lines, and creates a collection of
     * Method objects. Each Method consists of instructions and arguments parsed from the SML code.
     * </p>
     *
     * @param fileName the name of the file to read
     * @return a collection of Method objects
     * @throws IOException if an error occurs while reading the file
     */
    public Collection<Method> readAndTranslate(String fileName) throws IOException {
        Collection<Method> methods = new ArrayList<>();

        // Use fileService or fall back to direct file access
        try (Scanner sc = fileService != null
                ? fileService.createFileScanner(fileName)
                : new Scanner(new File(fileName), StandardCharsets.UTF_8)) {

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
                            throw new IllegalArgumentException("Instruction cannot appear outside methods " + labelString + " " + instruction);
                    }
                }
            }
            if (state != null) methods.add(state.createMethod());
        }
        return methods;
    }
    /**
     * Processes the arguments of a method from the current line and adds them to the state.
     *
     * @param state the state representing the current method being parsed
     */
    private void processMethodArguments(State state) {
        for (String s = scan(); !s.isEmpty(); s = scan()) {
            String variable = s.endsWith(ITEM_SEPARATOR)
                    ? s.substring(0, s.length() - 1).trim()
                    : s;

            state.addArgument(variable);

            if (!s.endsWith(ITEM_SEPARATOR)) break;
        }
    }
    /**
     * Gets an Instruction based on the current label in the SML code.
     *
     * @param label the label associated with the instruction
     * @return the corresponding Instruction object or null if not found
     */
    private Instruction getInstruction(Label label) {
        String opcode = scan();
        if (opcode.isEmpty()) return null;

        try {
            String className = getInstructionClassName(opcode);
            Class<?> instructionClass = Class.forName(className);
            return createInstructionInstance(instructionClass, label);
        } catch (Exception e) {
            System.err.println("Error creating instruction for opcode: " + opcode);
            e.printStackTrace();
            return null;
        }
    }

    private String getInstructionClassName(String opcode) {
        String normalizedOpcode = normaliseOpcode(opcode);
        return "sml.instructions." + normalizedOpcode + "Instruction";
    }

    /**
     * Normalises the opcode by capitalising and removing underscores.
     *
     * @param opcode the opcode to normalise
     * @return the normalized opcode
     */
    private String normaliseOpcode(String opcode) {
        if (!opcode.contains("_")) {
            return capitalise(opcode);
        }
        return Arrays.stream(opcode.split("_"))
                .map(this::capitalise)
                .collect(Collectors.joining());
    }

    private String capitalise(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Creates an Instruction instance based on the instruction class and label.
     *
     * @param instructionClass the class representing the instruction
     * @param label the label associated with the instruction
     * @return the created Instruction object
     * @throws Exception if an error occurs during instantiation
     */
    private Instruction createInstructionInstance(Class<?> instructionClass, Label label) throws Exception {
        var constructor = findLabelConstructor(instructionClass);
        Object[] args = buildConstructorArgs(constructor, label);
        return (Instruction) constructor.newInstance(args);
    }

    /**
     * Finds the constructor of the instruction class that takes a Label as the first argument.
     *
     * @param instructionClass the instruction class
     * @return the found constructor
     */
    private Constructor<?> findLabelConstructor(Class<?> instructionClass) {
        return Arrays.stream(instructionClass.getDeclaredConstructors())
                .filter(c -> c.getParameterTypes().length > 0 && c.getParameterTypes()[0] == Label.class)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No valid constructor found"));
    }

    private Object[] buildConstructorArgs(Constructor<?> constructor, Label label) {
        var paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        args[0] = label;

        for (int i = 1; i < paramTypes.length; i++) {
            String arg = scan();
            args[i] = convertArgument(arg, paramTypes[i]);
        }

        return args;
    }

    private Object convertArgument(String arg, Class<?> paramType) {
        return switch (paramType.getName()) {
            case "sml.Label" -> new Label(arg);
            case "sml.Variable$Identifier" -> new Variable.Identifier(arg);
            case "sml.Method$Identifier" -> new Method.Identifier(arg);
            case "int" -> Integer.parseInt(arg);
            default -> throw new IllegalArgumentException(
                    "Unsupported parameter type: " + paramType.getName());
        };
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

        if (line.isEmpty()) {
            return "";
        }

        int whiteSpacePosition = 0;
        while (whiteSpacePosition < line.length()) {
            if (Character.isWhitespace(line.charAt(whiteSpacePosition))) break;
            whiteSpacePosition++;
        }

        String word = line.substring(0, whiteSpacePosition);
        line = line.substring(Math.min(whiteSpacePosition, line.length()));
        return word;
    }
}