#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Define a structure for a CFG production
typedef struct{
    char nonTerminal;
    char* replacement;
} ProductionRule;


// Function to modify a string using a specified production
// rule on a non-terminal symbol at a given position in the string
char* applyProduction(char* currentString, int position, int ruleIndex, ProductionRule* rules, int numRules){
    // Check if position and ruleIndex are valid
    if (position < 0 || position >= strlen(currentString) || ruleIndex < 0 || ruleIndex >= numRules){
        printf("Invalid position or rule index\n");
        return NULL;
    }
    // Find the non-terminal at the specified position
	// And check if it matches the rule's non-terminal
    char nonTerminal = currentString[position];
    if (nonTerminal != rules[ruleIndex].nonTerminal){
        printf("Non-terminal at position does not match the rule's non-terminal\n");
        return NULL;
    }
    // Create new string with the modification
	// - Copy up to the position
	// - Null-terminate to use strcat
	// - Append replacement
	// - Append rest of the original state
    char* modifiedState = (char*)malloc(strlen(currentString) + strlen(rules[ruleIndex].replacement));
    strncpy(modifiedState, currentString, position);
    modifiedState[position] = '\0';
    strcat(modifiedState, rules[ruleIndex].replacement);
    strcat(modifiedState, currentString + position + 1);
    return modifiedState;
}


int main(){
    // Define production rules
    ProductionRule rules[] = {
        {'E', "E+T"},
        {'E', "T"},
        {'T', "n"}
    };
    int numRules = sizeof(rules) / sizeof(rules[0]);

    // Example usage for production rules
    printf("Using production rule 2 (T->n) on symbol in position 4 for string \"E+T+T\"\n");
    char* currentString = "E+T+T";
    int position = 0;
    int ruleIndex = 0;
    char* newState = applyProduction(currentString, position, ruleIndex, rules, numRules);
    if (newState != NULL){
        printf("Original state: %s\nModified state: %s\n", currentString, newState);
        free(newState);
    }

    return 0;
}
