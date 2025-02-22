package sml.instruction;

import sml.Label;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * ====================================================================================================================
 * A factory that dynamically creates Instruction objects using Reflection.
 * --------------------------------------------------------------------------------------------------------------------
 * This class discovers and registers Instruction classes within the same package, storing them in a HashMap,
 * called instructionMap, allolwing for the creation of Instruction instances based on their opcode, facilitating
 * a pluggable architecture where new Instruction types can be added without modifying the factory itself.
 * --------------------------------------------------------------------------------------------------------------------
 * The `instructionMap` static map holds the relationship between an opcode and a generic wildcard
 * `Class<?>` that extends `Instruction`.
 * ====================================================================================================================
 *
 * @author Ricki Angel
 */
public class InstructionFactory {
    private static final Map<String, Class<? extends Instruction>> instructionMap = new HashMap<>();

    static {
        findAndRegisterInstructions();
    }

    private static void findAndRegisterInstructions() {
        try {
            ClassLoader classLoader = InstructionFactory.class.getClassLoader();
            String packageName = InstructionFactory.class.getPackage().getName();
            String packagePath = packageName.replace('.', '/');

            URL packageURL = classLoader.getResource(packagePath);
            if (packageURL == null) {
                System.err.println("Could not find package: " + packagePath);
                return;
            }

            File packageDir = new File(packageURL.getFile());
            File[] classFiles = packageDir.listFiles(file -> file.isFile() && file.getName().endsWith("Instruction.class") // Only load files that end with "Instruction" (not InstructionFactory) for example.
            );

            if (classFiles != null) {
                for (File classFile : classFiles) {
                    String className = packageName + "." + classFile.getName().replace(".class", "");

                    Class<?> clazz = Class.forName(className);

                    if (Instruction.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {

                        try {
                            Field opcodeField = clazz.getField("OP_CODE");
                            Object opcodeValue = opcodeField.get(null);

                            if (opcodeValue instanceof String opCode) {
                                Class<? extends Instruction> instructionClass = clazz.asSubclass(Instruction.class);
                                instructionMap.put(opCode, instructionClass);
                            }
                        } catch (Exception e) {
                            System.err.println("Could not register instruction: " + className);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Instruction createInstruction(String opcode, Label label) {
        Class<? extends Instruction> instructionClass = instructionMap.get(opcode);

        if (instructionClass == null) {
            return null;
        }

        try {
            return instructionClass.getConstructor(Label.class).newInstance(label);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}