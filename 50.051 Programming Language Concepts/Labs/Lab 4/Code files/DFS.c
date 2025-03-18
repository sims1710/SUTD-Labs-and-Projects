#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Same as before
typedef struct{
    char nonTerminal;
    char* replacement;
} ProductionRule;


// Same as before
char* applyProduction(char* currentString, int position, ProductionRule rule){
    if (position < 0 || position >= strlen(currentString) || currentString[position] != rule.nonTerminal){
        return NULL;
    }
    char* newString = (char*)malloc(strlen(currentString) + strlen(rule.replacement));
    strncpy(newString, currentString, position);
    newString[position] = '\0';
    strcat(newString, rule.replacement);
    strcat(newString, currentString + position + 1);
    return newString;
}


// For DFS, we implement a stack, instead of a queue.
// - top: top element in stack.
// - next: rest of the stack.
typedef struct Stack{
    char* top;
    struct Stack* next;
} Stack;


// Add element new to current stack stack
void push(Stack** stack, char* new){
    Stack* newNode = (Stack*)malloc(sizeof(Stack));
    newNode->top = strdup(new);
    newNode->next = *stack;
    *stack = newNode;
}


// Get element on top of stack stack
char* pop(Stack** stack){
    if (*stack == NULL) {
        return NULL;
    }
    Stack* temp = *stack;
    *stack = (*stack)->next;
    char* top = temp->top;
    free(temp);
    return top;
}


// Check if stack is empty
int isEmpty(Stack* stack){
    return stack == NULL;
}


// DFS parsing, using left-most derivation
// - startSymbol: the start symbol for our CFG (here, "E").
// - targetString: the sequence of terminals we are trying to match (e.g. "n+n").
// - rules: our array of production rules for the CFG.
// - numRules: the number of production rules.
void dfsParse(char* startSymbol, char* targetString, ProductionRule* rules, int numRules){
    // Initialize the stack and add startSymbol to stack
	Stack* stack = NULL;
    push(&stack, startSymbol);
    int j;
    
	// Keep using the stack until it gets empty or iteration number reaches 200
    int iteration = 0;
    while (!isEmpty(stack) && iteration < 200) {
		// Pop element on top of stack
        char* currentString = pop(&stack);
        printf("Iteration %d: %s\n", iteration + 1, currentString);

        // If we have a match between currentString and targetString, stop, found derivation
		if (strcmp(currentString, targetString) == 0){
            printf("Target top \"%s\" found at iteration %d\n", targetString, iteration + 1);
            free(currentString);
            break;
        }
		
		// Find the leftmost non-terminal symbol in currentString
        int leftmostNonTerminalPos = -1;
        for (j = 0; j < strlen(currentString); j++) {
			// Assuming 'E' and 'T' are the only non-terminals
            if (currentString[j] == 'E' || currentString[j] == 'T') {
                leftmostNonTerminalPos = j;
                break;
            }
        }

        // If a non-terminal is found, try all applicable rules
        if (leftmostNonTerminalPos != -1) {
            for (j = 0; j < numRules; j++) {
                if (rules[j].nonTerminal == currentString[leftmostNonTerminalPos]) {
                    char* newString = applyProduction(currentString, leftmostNonTerminalPos, rules[j]);
                    if (newString) {
                        push(&stack, newString);
                        free(newString);
                    }
                }
            }
        }
		
		// Free string and increase iteration number
        free(currentString);
        iteration++;
    }
	
	// Clear stack once derivation has been found
    while (!isEmpty(stack)) {
        char* temp = pop(&stack);
        free(temp);
    }
	
	// Special display if maximal number of iterations reached
    if (iteration == 200) {
        printf("Stopped after reaching maximum iterations (200).\n");
    }
}


int main() {
	// Test case 1
    ProductionRule rules[] = {
        {'E', "E+T"},
        {'E', "T"},
        {'T', "n"}
    };
    int numRules = sizeof(rules) / sizeof(rules[0]);
	printf("Testing CFG 1 on n+n\n");
    dfsParse("E", "n+n", rules, numRules);
	
    // Test case 2
    ProductionRule rules2[] = {
        {'E', "T"},
        {'E', "E+T"},
        {'T', "n"}
    };
    int numRules2 = sizeof(rules) / sizeof(rules[0]);
    printf("\nTesting CFG 2 on n+n\n");
    dfsParse("E", "n+n", rules2, numRules2);

    return 0;
}
