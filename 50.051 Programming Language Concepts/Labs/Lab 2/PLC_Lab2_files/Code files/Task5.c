#include <stdio.h>

/* Define the FSM states, as in Task 2.  */
typedef enum {
    INITIAL_STATE,
    ACCEPTING_STATE
} State;

/* 
    Define the KleeneState enum for the KleeneFSM
    - KLEENE_INITIAL_STATE: Initial state before finding target letter, starting and accepting state.
    - KLEENE_REJECTING_STATE: Rejecting state, if another character than target letter is seen.
 */
typedef enum {
    KLEENE_INITIAL_STATE,
    KLEENE_REJECTING_STATE 
} KleeneState;

/* Define the FSM struct with an added field for the letter, as in Task 2.  */
typedef struct {
    State currentState;
    char targetLetter;
} FSM;

/* 
    Define the KleeneFSM struct for "^a*$".
    - currentState: Current state of the KleeneFSM.
    - targetLetter: The letter to search for.
*/
typedef struct {
    KleeneState currentState;
    char targetLetter;
} KleeneFSM;

/* Initialize the FSM with the target letter, as in Task 2.  */
void initFSM(FSM *fsm, char letter) {
    fsm->currentState = INITIAL_STATE;
    fsm->targetLetter = letter;
}

/* 
    Initialize the KleeneFSM by reusing the single character FSM object.
    - Set initial state as KLEENE_INITIAL_STATE.
    - Fetch targetLetter attribute for the KleeneFSM as the same one as in the single character FSM.
*/
void initKleeneFSM(KleeneFSM *kleeneFsm, FSM *fsm) {
    kleeneFsm->currentState = KLEENE_INITIAL_STATE;
    kleeneFsm->targetLetter = fsm->targetLetter;
}

/* 
    Process a character input for the KleeneFSM.
    - Implements the correct transition logic for our FSM.
*/
void processCharKleene(KleeneFSM *kleeneFsm, char input_char) {
    switch (kleeneFsm->currentState)
    {
    case KLEENE_INITIAL_STATE:
        if (input_char == kleeneFsm->targetLetter)
            kleeneFsm->currentState = KLEENE_INITIAL_STATE;
        else
            kleeneFsm->currentState = KLEENE_REJECTING_STATE;
        break;
    case KLEENE_REJECTING_STATE:
        break;
    }
}

/* Run the Kleene FSM for entire input string. */
int runRegexKleene(KleeneFSM *kleeneFsm, const char *str) {
    int i;
    for (i = 0; str[i] != '\0'; i++) {
        processCharKleene(kleeneFsm, str[i]);
    }
    return kleeneFsm->currentState == KLEENE_INITIAL_STATE;
}

int main() {
	/* Create our two FSMs objects.  */
    FSM fsm;
    KleeneFSM kleeneFsm;
    
	/* Some test case strings.  */
    const char *testString1 = ""; /* Should pass (empty string) */
    const char *testString2 = "aaa"; /* Should pass */
    const char *testString3 = "aab"; /* Should fail */
    const char *testString4 = "b"; /* Should fail */
	
	/* Initialize our FSMs.  */
	initFSM(&fsm, 'a');
    initKleeneFSM(&kleeneFsm, &fsm);
	
	/* Test case 1. */
    if (runRegexKleene(&kleeneFsm, testString1))
        printf("The string \"%s\" matches '^a*$'.\n", testString1);
    else
        printf("The string \"%s\" does not match '^a*$'.\n", testString1);
	
	/* Test case 2. */
	/* Remember to reset the FSM. */
	initKleeneFSM(&kleeneFsm, &fsm);
    if (runRegexKleene(&kleeneFsm, testString2))
        printf("The string \"%s\" matches '^a*$'.\n", testString2);
    else
        printf("The string \"%s\" does not match '^a*$'.\n", testString2);
	
	/* Test case 3. */
	/* Remember to reset the FSM. */
	initKleeneFSM(&kleeneFsm, &fsm);
    if (runRegexKleene(&kleeneFsm, testString3))
        printf("The string \"%s\" matches '^a*$'.\n", testString3);
    else
        printf("The string \"%s\" does not match '^a*$'.\n", testString3);

    /* Test case 4. */
	/* Remember to reset the FSM. */
	initKleeneFSM(&kleeneFsm, &fsm);
	if (runRegexKleene(&kleeneFsm, testString4))
        printf("The string \"%s\" matches '^a*$'.\n", testString4);
    else
        printf("The string \"%s\" does not match '^a*$'.\n", testString4);

    return 0;
}
