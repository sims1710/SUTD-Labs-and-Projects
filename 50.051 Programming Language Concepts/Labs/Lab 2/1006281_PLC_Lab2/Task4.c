#include <stdio.h>

/* Define the FSM states, as in Task 2. */
typedef enum {
    INITIAL_STATE,
    ACCEPTING_STATE
} State;

/* 
    Define the ChoiceState enum for the ChoiceFSM
    - CHOICE_INITIAL_STATE: Before finding any target letter, starting state.
    - CHOICE_ACCEPTING_STATE: After finding the first choice letter or the second choice letter, accepting state.
*/
typedef enum {
    CHOICE_INITIAL_STATE,
    CHOICE_ACCEPTING_STATE
} ChoiceState;

/* Define the FSM struct with an added field for the letter, as in Task 2. */
typedef struct {
    State currentState;
    char targetLetter;
} FSM;

/* 
    Define the ChoiceFSM struct to handle the two FSM objects for choice "a|b".
    - currentState: Current state of the ChoiceFSM.
    - firstChoice: First choice letter.
    - secondLetter: Second choice letter.
*/
typedef struct {
    ChoiceState currentState;
    char firstChoice;
    char secondChoice;
} ChoiceFSM;

/* Initialize the FSM with the target letter, as in Task 2. */
void initFSM(FSM *fsm, char letter) {
    fsm->currentState = INITIAL_STATE;
    fsm->targetLetter = letter;
}

/*
    Initialize the ChoiceFSM with two FSM objects.
    - Set initial state as CHOICE_INITIAL_STATE.
    - Fetch targetLetter attributes from both FSMs and assign correctly to ConcatFSM attributes.
*/
void initChoiceFSM(ChoiceFSM *choiceFsm, FSM *fsm1, FSM *fsm2) {
    choiceFsm->currentState = CHOICE_INITIAL_STATE;
    choiceFsm->firstChoice = fsm1->targetLetter;
    choiceFsm->secondChoice = fsm2->targetLetter;
}

/* 
    Process a character input for the ChoiceFSM.
    - Implements the correct transition logic for our FSM.
*/
void processCharChoice(ChoiceFSM *choiceFsm, char input_char) {
    switch (choiceFsm->currentState) {
        case CHOICE_INITIAL_STATE:
            if (input_char == choiceFsm->firstChoice || input_char == choiceFsm->secondChoice) 
            {
                choiceFsm->currentState = CHOICE_ACCEPTING_STATE;
            }
            else 
            {
                choiceFsm->currentState = CHOICE_INITIAL_STATE;
            }
            break;
        case CHOICE_ACCEPTING_STATE:
            break;
    }
}

/* Run the choice FSM for entire input string. */
int runRegexChoice(ChoiceFSM *choiceFsm, const char *str) {
    int i;
    for (i = 0; str[i] != '\0'; i++) {
        processCharChoice(choiceFsm, str[i]);
    }
    return choiceFsm->currentState == CHOICE_ACCEPTING_STATE;
}

int main() {
	/* Create our three FSMs objects. */
    FSM fsmA, fsmB;
	ChoiceFSM choiceFsm;
    
	/* Some test case strings. */
    const char *testString = "Hello, world!";
    const char *testString2 = "Hallo, world!";
    const char *testString3 = "Heblo, world!";
	
	/* Initialize our three FSMs. */
	initFSM(&fsmA, 'a');
    initFSM(&fsmB, 'b');
    initChoiceFSM(&choiceFsm, &fsmA, &fsmB);

	/* Test case 1. */
    if (runRegexChoice(&choiceFsm, testString))
        printf("The string \"%s\" contains 'a' or 'b'.\n", testString);
    else
        printf("The string \"%s\" does not contain 'a' or 'b'.\n", testString);
	
	/* Test case 2.
       Remember to reset the FSM. */
	initChoiceFSM(&choiceFsm, &fsmA, &fsmB);
    if (runRegexChoice(&choiceFsm, testString2))
        printf("The string \"%s\" contains 'a' or 'b'.\n", testString2);
    else
        printf("The string \"%s\" does not contain 'a' or 'b'.\n", testString2);
	
	/* Test case 3.
       Remember to reset the FSM. */
	initChoiceFSM(&choiceFsm, &fsmA, &fsmB);
    if (runRegexChoice(&choiceFsm, testString3))
        printf("The string \"%s\" contains 'a' or 'b'.\n", testString3);
    else
        printf("The string \"%s\" does not contain 'a' or 'b'.\n", testString3);

    return 0;
}
