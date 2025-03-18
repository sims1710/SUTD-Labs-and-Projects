#include <stdio.h>

/* 
    Define the FSM states as an enum.
    State 0: Current part of the binary number is a multiple of 3, starting state and only accepting state.
    State 1: Current part has a remainder of 1 when divided by 3.
    State 2: Current part has a remainder of 2 when divided by 3.
*/
typedef enum {
    STATE_0,
    STATE_1,
    STATE_2  
} State;

/*
    Define the FSM object as a struct.
    State: Current state of the FSM.
*/
typedef struct {
    State currentState;
} FSM;

/*
    Initialize the FSM before running an input string.
    Simply set the state attribute to ???.
*/
void initFSM(FSM *fsm) {
    fsm->currentState = STATE_0;
}

/* Process a single character and update the FSM according to FSM transition logic. */
void processInput(FSM *fsm, char input) 
{
    switch (fsm->currentState) {
        case STATE_0:
            if (input == '0') {
                fsm->currentState = STATE_0;
            } else if (input == '1') {
                fsm->currentState = STATE_1;
            }
            break;

        case STATE_1:
            if (input == '0') {
                fsm->currentState = STATE_2;
            } else if (input == '1') {
                fsm->currentState = STATE_0;
            }
            break;

        case STATE_2:
            if (input == '0') {
                fsm->currentState = STATE_1;
            } else if (input == '1') {
                fsm->currentState = STATE_2;
            }
            break;

        default:
            printf("Invalid state\n");
            break;
    }
}

/* Function to check if the binary number in string *binary is a multiple of 3 */
int isMultipleOf3(FSM fsm, char *binary) {
    int i;
    for (i = 0; binary[i] != '\0'; i++) {
        processInput(&fsm, binary[i]);
    }

    switch (fsm.currentState) {
        case STATE_0:
            return 1;
        default:
            return 0;
    }
}

int main() {
	/* Test case 1: 110, binary number representation for 6 (should be multiple of 3). */
    char binaryNumber[] = "110";
	/* Test case 2: 110, binary number representation for 7 (should not be multiple of 3). */
    char binaryNumber2[] = "111"; 
	
	/* Initialize the FSM */
    FSM fsm;
    initFSM(&fsm);
    
	/* Test case 1 run */
    if (isMultipleOf3(fsm, binaryNumber))
        printf("The binary number %s is a multiple of 3.\n", binaryNumber);
    else
        printf("The binary number %s is not a multiple of 3.\n", binaryNumber);
	
	/* Test case 2 run, remember to reset the FSM. */
	initFSM(&fsm);
    if (isMultipleOf3(fsm, binaryNumber2))
        printf("The binary number %s is a multiple of 3.\n", binaryNumber2);
    else
        printf("The binary number %s is not a multiple of 3.\n", binaryNumber2);
    
    return 0;
}
