/**
 * <h1>Simple Machine Language (SML) Interpreter</h1>
 *
 * <p>
 * This project implements an interpreter for the Simple Machine Language (SML),
 * a stack-based language designed for educational purposes. The interpreter provides
 * a virtual machine environment supporting arithmetic operations, control flow,
 * method invocation, and variable manipulation.
 * </p>
 *
 * <p>
 * Developed as a coursework assignment for the Software Design and Programming module,
 * part of the MSc Computer Science course at Birkbeck, University of London.
 * </p>
 *
 * <h2>Architecture Overview</h2>
 *
 * <ul>
 *   <li><strong>Machine:</strong> Manages program execution, frames, and instruction handling.</li>
 *   <li><strong>Instruction Set:</strong> Implements operations like arithmetic, control flow, and variable access.</li>
 *   <li><strong>Dynamic Discovery:</strong> Registers new instructions via configuration files or package scanning.</li>
 *   <li><strong>Translator:</strong> Converts SML source code into executable structures.</li>
 *   <li><strong>Frame System:</strong> Maintains operand stacks and variable storage for method execution.</li>
 * </ul>
 *
 * <h2>Key Design Features</h2>
 *
 * <ul>
 *   <li><strong>Extensibility:</strong> New instructions can be added dynamically.</li>
 *   <li><strong>Design Patterns:</strong> Utilizes Factory, Strategy, and Template Method patterns.</li>
 *   <li><strong>Spring Integration:</strong> Supports dependency injection but functions independently if needed.</li>
 *   <li><strong>Strong Typing:</strong> Uses record classes and wrapper types for safety.</li>
 *   <li><strong>Comprehensive Testing:</strong> Includes extensive unit and integration tests.</li>
 * </ul>
 *
 * <h2>Language Features</h2>
 *
 * <ul>
 *   <li><strong>Arithmetic:</strong> Operations like add, sub, mul, div, mod, sqrt.</li>
 *   <li><strong>Control Flow:</strong> Conditional/unconditional jumps, comparisons.</li>
 *   <li><strong>Methods:</strong> Parameterized calls, return values, nested execution.</li>
 *   <li><strong>Variables:</strong> Named storage, load/store operations.</li>
 *   <li><strong>Stack Manipulation:</strong> Push/pop operations for constants.</li>
 *   <li><strong>I/O:</strong> Basic print statements.</li>
 * </ul>
 *
 * <h2>Extending the Interpreter</h2>
 *
 * <ol>
 *   <li><strong>Adding Instructions:</strong> Extend {@link sml.instructions.Instruction} and define an OP_CODE.</li>
 *   <li><strong>Custom Discovery:</strong> Implement {@link sml.discovery.InstructionDiscoveryStrategy}.</li>
 *   <li><strong>Feature Extensions:</strong> Introduce new data types, control structures, or I/O features.</li>
 * </ol>
 *
 * <h2>Usage Examples</h2>
 * 
 * <h4>Using Command Line Interface:</h4>
  * <table>
 *   <tr>
 *     <th align="center">Linux / Mac OS CLI</th>
 *     <th align="center">Windows / PowerShell CLI</th>
 *   </tr>
 *   <tr>
 *     <td align="center">
 *       <a href="https://youtu.be/5946Zzi-ORM" target="_blank">
 *         <img src="doc-files/MacSMLsetup.jpg" alt="Linux / Mac OS setup" width="380">
 *       </a>
 *       <br>
 *       <em>Click to watch Linux / Mac OS CLI setup</em>
 *     </td>
 *     <td align="center">
 *       <a href="https://youtu.be/7FYMgWmGP08" target="_blank">
 *         <img src="doc-files/WinSMLsetup.jpg" alt="Windows (PowerShell) CLI setup" width="380">
 *       </a>
 *       <br>
 *       <em>Click to watch Windows (PowerShell) CLI setup</em>
 *     </td>
 *   </tr>
 * </table>
  * <h4>Using Spring Framework:</h4>
 * <pre>
 * ApplicationContext context = new AnnotationConfigApplicationContext(SmlConfig.class);
 * RunSml runner = context.getBean(RunSml.class);
 * runner.run("src/resources/your{program.sml");
 * </pre> 
 * <h4>Without Spring Framework:</h4>
 * <pre>
 * RunSml.create().run("src/resources/yourProgram.sml");
 * </pre>
 *
 * <h2>Example Programs</h2>
 *
 * <table border="1" cellspacing="0" cellpadding="5" style="border-collapse: collapse; border: 1px solid black;">
 *   <tr>
 *     <th>Filename</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>test1.sml</td>
 *     <td>Recursive Fibonacci calculation.</td>
 *   </tr>
 *   <tr>
 *     <td>test2.sml</td>
 *     <td>Iterative Fibonacci calculation.</td>
 *   </tr>
 *   <tr>
 *     <td>sqrtest.sml</td>
 *     <td>Tests square root calculations.</td>
 *   </tr>
 *   <tr>
 *     <td>numchar.sml</td>
 *     <td>Converts numbers to letters and prints ASCII values.</td>
 *   </tr>
 *   <tr>
 *     <td>simplecalc.sml</td>
 *     <td>Performs multiplication, stores result, and uses conditional jumps.</td>
 *   </tr>
 *   <tr>
 *     <td>simple.sml</td>
 *     <td>Tests an undefined `int_to_str` instruction.</td>
 *   </tr>
 * </table>
 *
 * <h2>Project Structure</h2>
 *
 * <ul>
 *   <li>{@link sml.instructions} - Instruction implementations.</li>
 *   <li>{@link sml.discovery} - Instruction discovery mechanisms.</li>
 *   <li>{@link sml.registry} - Manages instruction registration.</li>
 *   <li>{@link sml.services} - Service components (e.g., file handling).</li>
 *   <li>{@link sml.config} - Spring configuration and properties.</li>
 *   <li>{@link sml.helperfiles} - Utility functions and logging.</li>
 * </ul>
 *
 * <h2>System Requirements</h2>
 *
 * <ul>
 *   <li>Java 23 or higher.</li>
 *   <li>Maven 3.8.0+ for building.</li>
 *   li>JUnit 5.11.4 for unit testing..</li>
 *   <li>Spring Framework 6.2.2 (optional).</li>
 * </ul>
 *
 * @author Ricki Angel
 * @version 1.0
 * @see sml.Machine
 * @see sml.instructions.Instruction
 * @see sml.discovery.InstructionDiscoveryStrategy
 * @see sml.RunSml
 */
package sml;
