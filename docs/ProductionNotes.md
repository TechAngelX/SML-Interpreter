### SML Working Notes: My Workflow: Ricki Angel

Create subclasses of the `Instruction` class that extend its functionality.  
Do this **ONLY AFTER** completing methods in the `Instruction` class.

#### Entry Point
The entry point for the program—the `Main` method—is in the `RunSml` class.  
Remember to override `.equals()` and `.hashCode()` for the subclasses.

### Missing Instruction Classes to Implement:
- **Load** ✅ `LoadInstruction` class completed.
- **Store** ✅ `StoreInstruction` class completed - issues with the test.
- **Push**  Working on...
- **Add/Sub/Mul/Div**
- **if_cmpgt**
- **if_cmpe**

### Instruction Classes:

#### `LoadInstruction` ClassL - Should **read** from the variable using `variable.load()`.

#### `StoreInstruction` Class: - Should **write** to the variable using `variable.store(value)`.

---

### Test Classes:
Then create the test classes and run each unit:

- **LoadInstructionTest** ✅ `LoadInstruction` class tests completed.
- **StoreInstructionTest** ✅ `StoreInstruction` TEST WON'T WORK AS I NEED TO PUSH A VALUE ONTO THE STACK FIRST.
- 


### Identified Problems & Solutions

#### **PROBLEM 1:**
The `variables()` method seems to be prevalent across most instruction subclasses.  
In terms of **DRY (Don't Repeat Yourself)**, should `variables()` be part of an interface  
or remain as an abstract method in a base abstract class for better implementation and inheritance?

#### **SOLUTION 1: ** 
✅ Created an `AbstractVarInstruction` class to be used by `Store`, `Load`, `Push`, etc.

---

#### **PROBLEM 2:**
The `execute()` method seems to be prevalent across most instruction subclasses.  
In terms of **DRY**, perhaps should `execute()` be part of an interface?  
Or should it remain as an abstract method in a base abstract class for better implementation and inheritance?

#### **SOLUTION 2:** 
_(To be decided)_

---