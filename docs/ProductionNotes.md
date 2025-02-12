
Create subclasses of the Instruction class that extend the Instruction class functionality. Do this ONLY AFTER completing methods in the Instruction class.

The Entry point for the programme - the Main method - RunSml Class.
Remember to override .equals() and .hashCode() for the subclasses.

Missing instruction classes that need to be implemented:
load  ✅ LoadInstruction class created.
store ✅ Working on ....
push
add/sub/mul/div
if_cmpgt
if_cmpe


variables() provably needed in all instructiuon classes to report which
ariable the instruction uses by returning it in a stream.


LoadInstruction class. Should **read** from the variable using variable.load().
StoreInstruction class. Should **write**  o the variable using variable.store(value).


THOUGHTS: execute() seems to be prevalent for most of the instruction subclasses.
in terms of D.R.Y, Perhaps make the execute() method an interface? Or leave the 
abstract method in a base abstract class for better implementation and inheritance?