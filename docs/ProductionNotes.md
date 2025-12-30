# SML Implementation Project
## Development Documentation and Technical Notes
This guide documents my thoughts and processes in completing the  Simple Machine Language programm -  a high-level interpreter developed as a coursework assignment for the 'Software Design and Programming' module, part of the MSc Computer Science course at Birkbeck, University of London.

*Author: Ricki Angel*

## Architecture Overview

The Simple Machine Language (SML) interpreter provides a virtual machine environment with core functionality including:
- Stack-based operations
- Method invocation and execution
- Variable management
- Control flow mechanisms

## Implementation Plan

The implementation followed a structured approach with the following components:

#### Instruction Set Implementation

My first task was to complete an instruction set. this was Part I of the exercise.

| Instruction | Status | Description |
|-------------|:------:|-------------|
| `Load`      | ✅ | Load values from variables to the stack |
| `Store`     | ✅ | Store stack values to variables |
| `Push`      | ✅ | Push constants onto the stack |
| `Add`       | ✅ | Addition operation |
| `Sub`       | ✅ | Subtraction operation |
| `Mul`       | ✅ | Multiplication operation |
| `Div`       | ✅ | Division operation |
| `if_cmpgt`  | ✅ | Conditional branch on greater than |
| `if_cmpeq`  | ✅ | Conditional branch on equality |

#### System Architecture Components

Then complete some of the base system architecture.
| Component                      | Status | Description                                                            |
|---------------------------------|:------:|------------------------------------------------------------------------|
| `InstructionRegistrationManager`| ✅     | Renamed from `InstructionFactory` and refactored from Chain of Responsibility to Strategy pattern |
| `Translator and Reflection API` | ✅     | Completed implementation with testing                                 |
| `Manual Dependency Injection`   | ✅     | Constructor-based DI implementation                                    |
| `Spring Dependency Injection`   | ✅     | Configuration-based Spring integration                                 |

### Test Coverage

Comprehensive testing was implemented for all components:

| Test Class | Status | Description |
|------------|:------:|-------------|
| `LoadInstructionTest` | ✅ | Validated variable loading operations |
| `StoreInstructionTest` | ✅ | Verified variable storage operations |
| `PushInstructionTest` | ✅ | Tested constant pushing operations |
| `AddInstructionTest` | ✅ | Validated addition functionality |
| `SubInstructionTest` | ✅ | Confirmed subtraction operations |
| `MulInstructionTest` | ✅ | Tested multiplication logic |
| `DivInstructionTest` | ✅ | Verified division operations and edge cases |
| `IfCmpgtInstructionTest` | ✅ | Validated conditional branching |
| `IfCmpeqInstructionTest` | ✅ | Tested equality comparison branching |
| `SmlIntegrationTest` | ✅ | End-to-end system validation |
| `InstructionRegistrationManagerTest` | ✅ | Validated instruction registration |
| `ConfigDiscoveryTest` | ✅ | Tested configuration-based discovery |

### Testing Strategy

The testing approach incorporated:

- Input validation with both valid and invalid values
- Exception handling verification
- JUnit 5 parameterized tests for comprehensive coverage
- Mockito for dependency isolation in unit tests
- Integration tests for system-level validation

## Design Challenges and Solutions
Then as i moved onto Part II of the exercise, I looked into various ways of implementing injecting new instruction class without modyfing the Instruction class - Dependency Injection.
It was here where my learning increased, emboldening me to create new instruction classes (Mod, sqrt, NotEq) etc to see how these can be injected.
Here Are some of the probllems I encountered along the way; in line with my GitHub commits:

### 1. Code Duplication in Variable Handling

**Problem**: The `variables()` method appeared in multiple instruction subclasses, violating DRY principles.

**Solution**: Created an `AbstractVarInstruction` class to be extended by variable-manipulating instructions like `Store` and `Load`.

### 2. Instruction Factory Complexity

**Problem**: Initial implementation had unwieldy codebase in `InstructionFactory` with tight coupling.

**Solution**: Refactored using modular registration system with cleaner interfaces.

### 3. Template Method Pattern Evaluation

**Problem**: Needed to assess if Template Method was the optimal pattern for instruction execution.

**Solution**: Retained and enhanced the Template Method pattern after confirming its benefits:
- Base `Instruction` class effectively defines the algorithm skeleton
- Concrete instructions only need to implement `execute()` and `getOperandsString()`
- Common behavior remains centralized in the base class
- Pattern ensures consistent structure across all instructions

### 4. Service Provider Complexity

**Problem**: Initial ServiceProvider implementation introduced unnecessary complexity.

**Solution**: Simplified the design by removing the ServiceProvider and using a more direct approach.

### 5. Open/Closed Principle Compliance

**Problem**: A realisation that Sealed classes for `Instruction` and the instruction set violated the Open/Closed Principle by requiring base class modifications.

**Solution**: Removed sealed/non-sealed modifiers to allow for greater extensibility.

### 6. God Class Anti-Pattern

**Problem**: `InstructionFactory` exhibited God Class symptoms with excessive size and responsibilities.

**Solution**: Refactored with proper separation of concerns:
- Split functionality into specialized classes (`InstructionRegistry`, `InstructionDiscoveryStrategy`)
- Implemented Strategy Pattern for discovery methods
- Created a clear hierarchy of discovery approaches with graceful fallback
- Eliminated redundant methods and improved interfaces between components

### 7. Testing Complex Instructions

**Problem**: Factory testing limitations for instructions requiring multiple parameters.

**Solution**: Implemented a dual testing approach:
- Complete instantiation testing for simple instructions
- Factory recognition testing for complex instructions
- Clear documentation of testing limitations

### 8. Excessive Code Comments

**Problem**: Excessive inline comments made code harder to read and maintain. Also, weeird non-standard 'bar' lines in JavaDocs.

**Solution**: RFesearched how to produce professional JavaDoc and clearer code structure and annotations.

## Instruction Set Reference

As I became bolder and more understanding of instruction design, and inline with developing a new Configuration Registration service,
I started to design and implement my own custom instructions and .sml programs:

| Opcode     | Description                        |
|------------|------------------------------------|
| `sqrt`     | Square root (supplementary)        |
| `not_eq`   | Not equal comparison (supplementary)|
| `mod`      | Modulo operation (supplementary)   |
| `num_char` | Converts number to character       |

### Frequent Issues:

"Cannot pop from empty stack" errors were a frequent time consuming and recurring problem for me throughout this project.
Eventrually, I learned to reslove the problem by:

- Ensuring values are pushed before they are popped.
- Adding dummy push values before return statements.
- Implement stack size checking where appropriate.

For example:
```sml
// Example of proper stack management
push 10    // Push before popping
operation  // Uses the value

push 0     // Dummy value for return
return
```

### Console Logging
Initially I had print statements everywhere ! But eventually I used to implement Java's logging library and incorporated this into
my output, to clearly detail when instruction classes have been registered.

For controlling output verbosity, logging levels from most to least verbose:

1. FINEST
2. FINER
3. FINE
4. CONFIG
5. INFO
6. WARNING
7. SEVERE

## Overall Project Summation and Reflection

This project has been intellectually challenging yet immensely rewarding, involving accelerated learning and a steep growth curve. It has provided an excellent opporunity to synthesise diverse programming concepts—from generics and lambda expressions to method references—while deepening my understanding of fundamental principles such as polymorphism, inheritance, encapsulation, modern Java usage and open extension principles like Reflection API and exposure to the Spring Framework, unit testing, and professional Java documentation.

This project also deepened my understanding of operating system fundamentals, particularly regarding executable file processing. Implementing the SML interpreter gave me practical insight into how systems load and parse program files with specific extensions (.sml), and the architectural significance of file formats in language interpretation and execution pipelines.

I've gained the ability to view projects from an architectural perspective, seeing how components integrate through design patterns. The guidance from my lecturer was invaluable, particularly in implementing logging mechanisms for instruction registration tracking. This experience has enhanced my ability to refactor code and make informed decisions when selecting appropriate design patterns.

Overall, this has been an exceptional and stimulating educational experience that has solidified my 18 months of programming knowledge. Beyond Java-specific skills, it has strengthened my grasp of universal programming concepts and best practices applicable across modern languages.
