#include <stdio.h>

/* Define New Enum for Combined FSM */
typedef enum {
    INITIAL_STATE,
    ACCEPTING_STATE_1,
    ACCEPTING_STATE_2,
    REJECTING_STATE
} CombinedState;

/* Define Combined FSM Struct, as in previous tasks.  */
typedef struct {
    CombinedState currentState;
    char firstChoice;
    char secondChoiceOption1;
    char secondChoiceOption2;
    char targetLetter;
} CombinedFSM;

/* 
    Initialize the Combined FSM, as in previous tasks.
    - For simplicity, we will not be storing letters ('a', 'b', 'c' and 'd') in attributes.
*/
void initCombinedFSM(CombinedFSM *combinedFsm) {
    combinedFsm->currentState = INITIAL_STATE;
    combinedFsm->firstChoice = 'a';
    combinedFsm->secondChoiceOption1 = 'b';
    combinedFsm->secondChoiceOption2 = 'c';
    combinedFsm->targetLetter = 'd';
}

/* Process given input_char for the Combined FSM.  */
void processCharCombined(CombinedFSM *combinedFsm, char input_char) {
    switch (combinedFsm->currentState)
    {
    case INITIAL_STATE:
        if (input_char == combinedFsm->firstChoice)
        {
            combinedFsm->currentState = ACCEPTING_STATE_1;
        }
        else
        {
            combinedFsm->currentState = INITIAL_STATE;
        }
        break;
    
    case ACCEPTING_STATE_1:
        if (input_char == combinedFsm->secondChoiceOption1 || input_char == combinedFsm->secondChoiceOption2)
        {
            combinedFsm->currentState = ACCEPTING_STATE_2;
        }
        else
        {
            combinedFsm->currentState = INITIAL_STATE;
        }
        break;
    
    case ACCEPTING_STATE_2:
        if (input_char == combinedFsm->targetLetter)
        {
            combinedFsm->currentState = ACCEPTING_STATE_2;
        }           
        else
        {
            combinedFsm->currentState = REJECTING_STATE;
        }
        break;

    case REJECTING_STATE:
        break;
    }
}

/* 
    Running the Combined FSM, as in previous tasks.
    - Simple for loop on all characters as before.
    - Break for loop early on rejection, if you want.
    - Return true if the FSM is in correct accepting state.
 */
int runRegexCombined(CombinedFSM *combinedFsm, const char *str) {
    int i;
    for (i = 0; str[i] != '\0'; i++) {
        processCharCombined(combinedFsm, str[i]);
        if (combinedFsm->currentState == REJECTING_STATE)
        {
            return 0;
        }
    }
    return combinedFsm->currentState == ACCEPTING_STATE_2;
}

int main() {
	
	/* Initialize our FSM object. */
    CombinedFSM combinedFsm;
	
	/* Some test strings, including the empty string.  */
    char *testStrings[] = {"a", "abd", "acd", "aad", "abdd", "ac", "abcd", "abda", NULL};
	int i;
    int matches;
	/* Run test cases and reset FSM each time. */
    for (i = 0; testStrings[i] != NULL; i++) {
		initCombinedFSM(&combinedFsm);
        matches = runRegexCombined(&combinedFsm, testStrings[i]);
        printf("String \"%s\" matches: %s\n", testStrings[i], matches ? "Yes" : "No");
    }
    return 0;
}
