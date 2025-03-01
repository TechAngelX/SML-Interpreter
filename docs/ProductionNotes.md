### SML Working Notes: My Workflow: Ricki Angel

Create subclasses of the `Instruction` class that extend its functionality.  
Do this **ONLY AFTER** completing methods in the `Instruction` class.

#### Entry Point

The entry point for the program—the `Main` method—is in the `sml.RunSml` class.  
Remember to override `.equals()` and `.hashCode()` for the subclasses.

### PART I

#### Missing Instruction Classes to Implement:

```
Load        ✅  LoadInstruction class completed.
Store       ✅  StoreInstruction class completed.
Push        ✅  PushInstruction class completed.
Add         ✅  AddInstruction class completed.
Sub         ✅  SubInstruction class completed.
Mul         ✅  MultiplyInstruction class completed.
Div         ✅  DivInstruction class completed.
if_cmpgt    ✅  IfGreaterGotoInstruction class completed.
if_cmpeq    ✅  IfEqualGotoInstruction class completed.

InstructionFactory ✅  InstructionFactory completed with Chain of Responsibility pattern.
```

### PART II

Translator and Reflection API ✅ Completed. Test using: java sml.RunSml src/main/resources/test1.sml
Dependency Injection (Manual) ✅ Consstructor-based DI.Complete.
Dependency Injection (Spring) ✅ Complete. ased DI. Complete.
Now incorporates both Spring DI and CLI base DI with two ways to run the machine via command line.

```

### Testing Strategy: Points to Note:
- Write tests that supply incorrect input values.
- Ensure the code fails appropriately, throwing the expected exceptions.
- Once errors are identified, correct them and re-run the tests.
- Tests should confirm that invalid inputs trigger the appropriate runtime exceptions.
- Learn about Parameterized tests and holistic testing of a  new InstructionRegisterManager class.
- Implement integration tests ✅
- Add JUnit 5 Parameterized Tess ✅
- Research, learn and add/implement mockito to mock and produce config discovery tests. ✅
- In POM, Scope mockito to test, not final build.
```

### Instruction Test Status:

Create the test classes AFTER finishing their respective concrete classes, and run each unit:

LoadInstructionTest ✅ LoadInstruction class tests completed.  
StoreInstructionTest ✅ StoreInstruction class tests completed.  
PushInstructionTest ✅ PushInstruction class tests completed.  
AddInstructionTest ✅ AddInstruction class tests completed.  
SubInstructionTest ✅ SubInstruction class tests completed.  
MultiplySubInstructionTest ✅ MultiplySubInstruction class tests completed.  
DivInstructionTest ✅ DivInstruction class tests completed.  
IfGreaterGotoInstruction ✅ IfGreaterGotoInstruction class tests completed.
IfEqualGotoInstruction ✅ IfEqualGotoInstruction class tests completed.
ConfigDiscoveryTest: ❌
After experiencing lots of test failures with new ConfigDiscovery class. Researching
adding a protected method to ConfigDiscovery  to create a "seam" - a place where tests
can inject different behavior without messing with the core logic. 


### OBSERVATIONS: Identified Problems & Solutions

```
PROBLEM 1: DRY Issue
The `variables()` method seems to be prevalent across most instruction subclasses.  
In terms of **DRY (Don't Repeat Yourself)**, should `variables()` be part of an interface  
or remain as an abstract method in a base abstract class for better implementation and inheritance?

SOLUTION 1:
✅ Created an `AbstractVarInstruction` class to be used by `Store`, `Load`, `Push`, etc.

```

```
PROBLEM 2:
Unwieldy codebase in InstructionFactory / tight coupling.

SOLUTION 2:
✅ Now Refactored using Java ServiceLoader and modulear registration.
```

```
PROBLEM 3: Execute Method Pattern Analysis*
The execute() method implementation across instruction classes follows the Template Method pattern. However, 
this raises a design question:
1. Is Template Method the optimal pattern here?
2. Would an interface-based approach provide better flexibility?

SOLUTION 3: Confirm Template Method Pattern
✅ Retained and reinforced the Template Method pattern because:
1. The base Instruction class defines the algorithm skeleton
2. Concrete instruction only need to implement execute() and getOperandsString()
3. Common behavior (toString, label handling, etc.) stays in base class
4. Pattern ensures consistent structure across all instruction
```
```
PROBLEM 4: ServiceProvider - Overengineering
Upon critically evaluating the code, it seems that the dynamic ServiceProvider solution might be overengineered. 
Currently, the configuration already utilizes reflection to discover and load instruction classes. Additionally, 
the design employs a factory pattern (InstructionFactory) to handle the creation of instruction. New instruction 
can already be added without modifying existing code, simply by introducing new instruction classes. However, the 
current implementation introduces additional complexity by creating more areas that need modification 
when adding a new instruction. The opcode-to-class mapping is already dynamic through the INSTRUCTION_MAP, which 
provides sufficient flexibility.

SOLUTION 4:
✅ Remove the ServiceProvider and revert to using the simpler InstructionFactory process.

```
```
PROBLEM 5: Sealed Yes, Truly OCP, No.
The use of a sealed class for the Instruction class presents some considerations with regard to the Open/Closed Principle (OCP). 
While a sealed class provides control over which classes can inherit from it, it introduces a form of coupling between the base 
class (Instruction.java) and any future subclasses i or anyone might add. To introduce a new type of instruction, the base class 
must be modified to include the new subclass in the permits list. So this arguably breaks the OCP rule.

SOLUTION 5:
✅ Remove Sealed/Non-sealed from base and subclasses.
```
```
PROBLEM 6: Is InstructionFactory a God Class?  300 + lines with  conditional logic and reflection operations !!!
The InstructionFactory class exhibits classic God Class symptoms: excessive size, too many responsibilities 
(configuration loading, package scanning, reflection, instruction instantiation, logging), high complexity, 
and low cohesion. This violates the Single Responsibility Principle and makes the code difficult to maintain and test.

SOLUTION 6:
✅ Refactored using a proper separation of concerns:
- Split functionality into specialized classes (InstructionRegistry, InstructionDiscoveryStrategy interface)
- Implemented Strategy Pattern for discovery methods (ConfigFileDiscoveryStrategy, PackageScanDiscoveryStrategy)
- Created a clear hierarchy of discovery approaches with graceful fallback
- Removed legacy reflection-based discovery methods in favor of configuration-driven approach
- Eliminated redundant convenience methods for each instruction type
- Designed clean interfaces between components to reduce coupling.
```
```

PROBLEM 7: Code Smell - Comment Hell !
Upon critical evaluation of my code, it is possible I have too much comments in my classes.
The thinking is - If you have to explain in too much detail what you're doing, there's something wrong.

SOLUTION 7:
❌ Work toward cleaning up and removing 'guideline' comments.
```

### Additional Functionality:

Created a simple programe 'sqrtTest.sml' that:

- Loads a number.
- Computes its square root.
- Prints both the original number and the result.
- Ends correctly without stack errors.

```
```

### Opcodes:

- add
- div
- goto
- if_cmpeq
- if_cmpgt
- invoke
- load
- mul
- print
- push
- return
- store
- sub
- sqrt - bespoke
- not_eq - bespoke
- mod - bespoke

```
```

Java's Loging levels from most to least verbose:
FINEST
FINER
FINE
CONFIG
INFO
WARNING
SEVERE
