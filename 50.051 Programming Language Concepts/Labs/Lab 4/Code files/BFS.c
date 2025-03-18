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
    char* newString = (char*)malloc(strlen(currentString) + strlen(rule.replacement) - 1);
    strncpy(newString, currentString, position);
    newString[position] = '\0';
    strcat(newString, rule.replacement);
    strcat(newString, currentString + position + 1);
    return newString;
}


// Defining a queue object
// - top: element on top of the queue
// - next: rest of the queue
typedef struct Queue{
    char* top;
    struct Queue* next;
} Queue;

// Add element elem at the end of queue queue
void enqueue(Queue** queue, char* elem){
    Queue* newNode = (Queue*)malloc(sizeof(Queue));
    newNode->top = strdup(elem);
    newNode->next = NULL;
    if (*queue == NULL){
        *queue = newNode;
    } else {
        Queue* current = *queue;
        while (current->next){
            current = current->next;
        }
        current->next = newNode;
    }
}


// Retrieve the element on top of the queue and adjust queue accordingly
char* dequeue(Queue** queue){
    if (*queue == NULL) {
        return NULL;
    }
    Queue* temp = *queue;
    *queue = (*queue)->next;
    char* top = temp->top;
    free(temp);
    return top;
}


// Check if queue is empty
int isEmpty(Queue* queue){
    return queue == NULL;
}


// Parsing function, LL(0) with BFS.
// - startSymbol: the start symbol for our CFG (here, "E").
// - targetString: the sequence of terminals we are trying to match (e.g. "n+n").
// - rules: our array of production rules for the CFG.
// - numRules: the number of production rules.
void bfsParse(char* startSymbol, char* targetString, ProductionRule* rules, int numRules){
    // Start an empty queue and add the startSymbol "E" to it
    Queue* queue = NULL;
    char* startSymbolCopy = strdup(startSymbol);
    enqueue(&queue, startSymbolCopy);
    free(startSymbolCopy);
    
	// While the queue has elements to check
	// and we have not reached the maximal number of iterations 200
    int iteration = 0;
    while (!isEmpty(queue) && iteration < 200){
		// Pop the top element of the queue
        char* currentString = dequeue(&queue);
		
		// Display the iteration number and element being checked
        printf("Iteration %d: %s\n", iteration + 1, currentString);
		
		// If we have a match between the element currentString and targetString,
		// stop and print success!
        if (strcmp(currentString, targetString) == 0){
            printf("Target top \"%s\" found at iteration %d\n", targetString, iteration + 1);
            free(currentString);
            break;
        }

		// Otherwise, try to apply each possible rule of the CFG
		// to every possible non-terminal symbol in currentString
		// and add new string to queue.
        for (int i = 0; i < numRules; ++i){
            for (int j = 0; j < strlen(currentString); ++j){
                if (currentString[j] == rules[i].nonTerminal){
                    char* newString = applyProduction(currentString, j, rules[i]);
                    if(newString){
                        enqueue(&queue, newString);
                        free(newString);
                    }
                }
            }
        }
        
		// Some cleanup and iteration incrementation
        free(currentString);
        iteration++;
    }

    // Cleanup remaining items in the queue, if any
    while (!isEmpty(queue)){
        char* temp = dequeue(&queue);
        free(temp);
    }
    
	// Final display for maximal iterations reached
    if (iteration == 200){
        printf("Stopped after reaching maximum iterations (200).\n");
    }
}


int main() {
    ProductionRule rules[] = {
        {'E', "E+T"},
        {'E', "T"},
        {'T', "n"}
    };
    int numRules = sizeof(rules) / sizeof(rules[0]);
	
	// Test case 1
    printf("Testing LL(0) BFS with n+n\n");
    bfsParse("E", "n+n", rules, numRules);
	
	// Test case 2
    printf("\nTesting LL(0) BFS with n+n+n\n");
    bfsParse("E", "n+n+n", rules, numRules);
	
	// Test case 3
    printf("\nTesting LL(0) BFS with n++n\n");
    bfsParse("E", "n++n", rules, numRules);

    return 0;
}
