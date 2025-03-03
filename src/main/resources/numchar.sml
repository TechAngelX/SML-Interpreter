@main:
    push 18     # R
    num_char    # converts 18 to letter 'R'
    print       # prints ASCII value of 'R'

    push 9      # I
    num_char    # converts 9 to letter 'I'
    print       # prints ASCII value of 'I'

    push 3      # C
    num_char    # converts 3 to letter 'C'
    print       # prints ASCII value of 'C'

    push 11     # K
    num_char    # converts 11 to letter 'K'
    print       # prints ASCII value of 'K'

    push 9      # I
    num_char    # converts 9 to letter 'I'
    print       # prints ASCII value of 'I'
    
    push 0
    num_char 
    
    push 27
    num_char 
    
    push 0      # Dummy push to prevent empty stack on return
    return
