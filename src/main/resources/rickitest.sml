@main:
   push 9       # Push 9
   sqrt         # Compute sqrt(9)
   print        # Print 3

   push 16      # Push 16
   sqrt         # Compute sqrt(16)
   print        # Print 4

   push 25      # Push 25
   sqrt         # Compute sqrt(25)
   print        # Print 5

   push 0       # Push 0
   sqrt         # Compute sqrt(0)
   print        # Print 0

   # Demonstrating stack interaction
   push 100     # Push 100
   push 49      # Push additional value
   sqrt         # Compute sqrt(49)
   print        # Print 7
   print        # Print 100 still on stack

   push 1       # Ensure return has a value
   return       # End execution