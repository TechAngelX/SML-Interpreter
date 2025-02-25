@main:
    push 10
    push 10
    mul          # Stack has [100]
    store result # Save multiplication result
    load result  # Get copy for comparison
    push 5
    if_cmpeq L10
    load result  # Reload saved value
    print        # Print it
    push 15
    print
    push 1       # Push a value for return
    return
L10: push 10
    print
    push 1       # Push a value for return
    return