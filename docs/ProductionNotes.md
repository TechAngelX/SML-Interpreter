### SML Working Notes: My Workflow: Ricki Angel

Create subclasses of the `Instruction` class that extend its functionality.  
Do this **ONLY AFTER** completing methods in the `Instruction` class.

#### Entry Point
The entry point for the program—the `Main` method—is in the `RunSml` class.  
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

InstructionFactory   ❌  Work in progress....
```

### PART II
Translator and Reflection   ❌  Work in progress....

```

### Testing Strategy: Points to Note:
- Write tests that supply incorrect input values.
- Ensure the code fails appropriately, throwing the expected exceptions.
- Once errors are identified, correct them and re-run the tests.
- Tests should confirm that invalid inputs trigger the appropriate runtime exceptions.

```
### Instruction Test Status:
Create the test classes AFTER finishing their respective concrete classes, and run each unit:

LoadInstructionTest          ✅  LoadInstruction class tests completed.  
StoreInstructionTest         ✅  StoreInstruction class tests completed.  
PushInstructionTest          ✅  PushInstruction class tests completed.  
AddInstructionTest           ✅  AddInstruction class tests completed.  
SubInstructionTest           ✅  SubInstruction class tests completed.  
MultiplySubInstructionTest   ✅  MultiplySubInstruction class tests completed.  
DivInstructionTest           ✅  DivInstruction class tests completed.  
IfGreaterGotoInstruction     ✅  IfGreaterGotoInstruction class tests completed.
IfEqualGotoInstruction       ✅  IfEqualGotoInstruction class tests completed.
```

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


#### **PROBLEM 3: Code Smell - Comment Hell !**
Upon critical evaluation of my code, it is possible I have too much comments in my classes.
The thinking is - If you have to explain in too much detail what you're doing, there's something wrong.

#### **SOLUTION 3:** 
Comments are fine for me as I'm working (in the same wa a painter decorato marks lines on the wall with felt pen,
but as the project moves towards production I aim to drastically clean up and remove all the // comments.
