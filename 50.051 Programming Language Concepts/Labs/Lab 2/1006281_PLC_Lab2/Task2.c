#include <stdio.h>

/* 
    Define the FSM states.
    - INITIAL_STATE: Before finding the target letter, starting state.
    - ACCEPTING_STATE: After finding the target letter, only accepting state.
 */
typedef enum {
    INITIAL_STATE, 
    ACCEPTING_STATE 
} State;

/*
Define the FSM struct with an added field for the letter.
    - currentState: Current state of the FSM.
    - targetLetter: The letter to search for.
*/
typedef struct {
    State currentState;
    char targetLetter;
} FSM;

/* Initialize the FSM with the correct starting state and target letter. */
void initFSM(FSM *fsm, char letter) {
    fsm->currentState = INITIAL_STATE;
    fsm->targetLetter = letter;
}

/* 
    Process a character input for the FSM with the correct logic.
    - When in INITIAL_STATE, move to ACCEPTING_STATE if input_char matches the targetLetter in FSM.
    - Otherwise, stay in INITIAL_STATE.
    - When in ACCEPTING_STATE, stay in ACCEPTING_STATE.
*/
void processChar(FSM *fsm, char input_char) {
    switch (fsm->currentState) {
        case INITIAL_STATE:
            if (input_char == fsm->targetLetter) {
                fsm->currentState = ACCEPTING_STATE;
            }
            break;
        case ACCEPTING_STATE:
            break;
    }
}

/* 
    General function to check if the string contains a specific letter
    - Reset FSM state for each new input string.
    - Pass the pointer correctly and call processChar() function.
    - Check if final FSM state is ACCEPTING_STATE.
*/
int runRegex(FSM *fsm, const char *input_str) {
    int i;
    fsm->currentState = INITIAL_STATE; 
    for (i = 0; input_str[i] != '\0'; i++) {
        processChar(fsm, input_str[i]);
        /* printf("Character: %c\n", input_str[i]); */
        if (fsm->currentState == ACCEPTING_STATE) {
            break;
        }
    }
    return fsm->currentState == ACCEPTING_STATE;
}

int main() {
	/* Initialize three FSM instances for letters 'a', 'e', 'g' */
    FSM fsmA, fsmB, fsmC;
	
	/* Two simple test cases. */
    const char *testString1 = "Hello, world!";
    const char *testString2 = "Hallo, world!";
    
    /* Initialize FSMs for different letters */
    initFSM(&fsmA, 'a');
    initFSM(&fsmB, 'e');
    initFSM(&fsmC, 'g');
    
    /* Test case 1: string 1 with letter 'a' */
    if (runRegex(&fsmA, testString1))
        printf("The string \"%s\" contains an 'a'.\n", testString1);
    else
        printf("The string \"%s\" does not contain an 'a'.\n", testString1);
    
    /* Test case 2: string 2 with letter 'a' */
    if (runRegex(&fsmA, testString2))
        printf("The string \"%s\" contains an 'a'.\n", testString2);
    else
        printf("The string \"%s\" does not contain an 'a'.\n", testString2);
    
    /* Test case 3: string 1 with letter 'e' */
    if (runRegex(&fsmB, testString1))
        printf("The string \"%s\" contains an 'e'.\n", testString1);
    else
        printf("The string \"%s\" does not contain an 'e'.\n", testString1);
    
    /* Test case 4: string 2 with letter 'e' */
    if (runRegex(&fsmB, testString2))
        printf("The string \"%s\" contains an 'e'.\n", testString2);
    else
        printf("The string \"%s\" does not contain an 'e'.\n", testString2);
    
    /* Test case 5: string 1 with letter 'g' */
    if (runRegex(&fsmC, testString1))
        printf("The string \"%s\" contains an 'g'.\n", testString1);
    else
        printf("The string \"%s\" does not contain an 'g'.\n", testString1);
    
    /* Test case 6: string 2 with letter 'g' */
    if (runRegex(&fsmC, testString2))
        printf("The string \"%s\" contains an 'g'.\n", testString2);
    else
        printf("The string \"%s\" does not contain an 'g'.\n", testString2);
    
    return 0;
}
