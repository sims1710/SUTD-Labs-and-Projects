// Only using stdio, no string library needed for this.
#include <stdio.h>
// Define the maximum number of symbols on the RHS of a rule a 10 (should be enough).
#define MAX_RHS 10
// Define the maximum number of symbols in a CFG as 10 (should be enough).
#define MAX_SYMBOLS 10
// Define the maximum number of rules in a CFG as 10 (should be enough).
#define MAX_RULES 10


// Struct for CFG symbols.
// - symbol: A single character containing the representation of the CFG symbol.
// - is_terminal: An int, which indicates whether the symbol is terminal (0 = false, 1 = true)
// - is_start: An int, which indicates whether the symbol is a start symbol (0 = false, 1 = true)
typedef struct {
    char symbol;
    int is_terminal;
    int is_start;
} CFGSymbol;


// Struct for a production rule of our CFG.
// - lhs: The left-hand side of the production rule, which consists of a single non-terminal CFGSymbol.
// - rhs: The right-hand side of the production rule, which consists of a sequence of CFGSymbols,
// defined as an array CFGSymbol elements, with size MAX_RHS.
// - rhs_count: An int denoting the number of symbols in the right-hand side array.
typedef struct {
    CFGSymbol lhs;
    CFGSymbol rhs[MAX_RHS];
    int rhs_count;
} CFGProductionRule;


// Struct for the full CFG.
// - symbols: An array of possible symbols, defined as an array of CFGSymbol, with size MAX_SYMBOLS.
// - startSymbol: The start symbol of the CFG, as a single CFGSymbol.
// - rules: An array of possible production rules, defined as an array of CFGProductionRule, with size MAX_RULES.
// - symbol_count: An int, denoting the number of CFGSymbol elements in the symbols array.
// - rule_count: An int, denoting the number of CFGProductionRule elements in the rules array.
typedef struct {
    CFGSymbol symbols[MAX_SYMBOLS];
    CFGSymbol startSymbol;
    CFGProductionRule rules[MAX_RULES];
    int symbol_count;
    int rule_count;
} CFG;


// Generic function to initialize a CFGSymbol.
void init_CFGSymbol(CFGSymbol* symbol, char text, int is_terminal, int is_start) {
    symbol->symbol = text;
    symbol->is_terminal = is_terminal;
    symbol->is_start = is_start;
}


// Specific initializers for different types of symbols (non-terminal symbol).
void init_NonTerminal(CFGSymbol* symbol, char text) {
    symbol->symbol = text;
    symbol->is_terminal = 0;
    symbol->is_start = 0;
}


// Specific initializers for different types of symbols (terminal symbol).
void init_Terminal(CFGSymbol* symbol, char text) {
    symbol->symbol = text;
    symbol->is_terminal = 1;
    symbol->is_start = 0;
}


// Specific initializers for different types of symbols (start symbol).
void init_StartSymbol(CFGSymbol* symbol, char text) {
    symbol->symbol = text;
    symbol->is_terminal = 0;
    symbol->is_start = 1;
}


// Function to create a production rule.
// - It should check if lhs is a non-terminal symbol.
// It will display an error message and set the rhs_length attribute to -1 otherwise.
// - It will then assign the sequence of symbols in CFGSymbol rhs[]
// to the production rule rhs attribute, and in the process, define the number of elements rhs_length.
CFGProductionRule createProductionRule(CFGSymbol lhs, CFGSymbol rhs[], int rhs_length) {
    CFGProductionRule rule;
    int i;
    
	// Check that lhs is not a terminal symbol (otherwise, problem) 
    if (lhs.is_terminal == 1) {
        printf("Error: Left-hand side symbol must be a non-terminal symbol.\n");
        rule.rhs_count = -1;
        return rule;
    }
    else
    {
        rule.lhs = lhs;
    
        for (i = 0; i<rhs_length; i++) {
            rule.rhs[i] = rhs[i];
        }

        rule.rhs_count = rhs_length;
        return rule;
    }
}


// Function to print a production rule.
// - Can safely assumes that we will have a valid production rule already.
// - Print the left-hand side symbol, then print " --> ",
// and finally iterate through the right-hand side symbols and print them.
void printProductionRule(CFGProductionRule rule) {
    int i;
    
    printf("%c", rule.lhs.symbol);
	printf(" --> ");
	for (i = 0; i < rule.rhs_count; i++) {
		printf("%c", rule.rhs[i].symbol);
    }
    printf("\n");
	return;
}


// Function to create a CFG.
// - Receives a CFG struct, an array of symbols, an array of production rules and counters for the lengths of these arrays.
// - Should simply assign each of these arrays and int values to the appropriate attributes of the CFG struct.
void init_CFG(CFG* cfg, CFGSymbol symbols[], int symbol_count, CFGSymbol startSymbol, CFGProductionRule rules[], int rule_count) {
    int i;
    
    for (i = 0; i < symbol_count; i++) {
        cfg->symbols[i] = symbols[i];
    }
    cfg->symbol_count = symbol_count;
    cfg->startSymbol = startSymbol;
    for (i = 0; i < rule_count; i++) {
        cfg->rules[i] = rules[i];
    }
    cfg->rule_count = rule_count;
}


// Function for printing the CFG as expected.
// - Should display all the production rules in the format "(k) lhs --> rhs".
void printCFG(const CFG cfg) {
    int i;
    
    for (i = 0; i < cfg.rule_count; i++) {
        printf("(%d):   ", i);
        printProductionRule(cfg.rules[i]);
    }
}


// Some test cases
int main(void) {
    CFGSymbol S, E, T, plus, n;
	CFGSymbol rhs0[1], rhs1[1], rhs2[3], rhs3[1], rhs4[1];
	CFGProductionRule rule0, rule1, rule2, rule3, rule4;
	CFG cfg;
	CFGSymbol symbols[5];
	CFGProductionRule rules[4];

    // Initialize symbols and print them.
    init_StartSymbol(&S, 'S');
    init_NonTerminal(&E, 'E');
    init_NonTerminal(&T, 'T');
    init_Terminal(&plus, '+');
    init_Terminal(&n, 'n');
    
    printf("\n--- Our Symbols ---\n");
    printf("Symbol: %c, Is Terminal: %d, Is Start: %d\n", S.symbol, S.is_terminal, S.is_start);
    printf("Symbol: %c, Is Terminal: %d, Is Start: %d\n", E.symbol, E.is_terminal, E.is_start);
    printf("Symbol: %c, Is Terminal: %d, Is Start: %d\n", T.symbol, T.is_terminal, T.is_start);
    printf("Symbol: %c, Is Terminal: %d, Is Start: %d\n", plus.symbol, plus.is_terminal, plus.is_start);
    printf("Symbol: %c, Is Terminal: %d, Is Start: %d\n", n.symbol, n.is_terminal, n.is_start);
	
	// Define the production rules and display them.
	printf("\n--- Our Production rules ---\n");
	rhs0[0] = n;
	rule0 = createProductionRule(n, rhs0, 1);
    rhs1[0] = E;
    rule1 = createProductionRule(S, rhs1, 1);
    rhs2[0] = E; 
    rhs2[1] = plus;
    rhs2[2] = T;
    rule2 = createProductionRule(E, rhs2, 3);
    rhs3[0] = T;
    rule3 = createProductionRule(E, rhs3, 1);
    rhs4[0] = n;
    rule4 = createProductionRule(T, rhs4, 1);
	printProductionRule(rule1);
	printProductionRule(rule2);
	printProductionRule(rule3);
	printProductionRule(rule4);
    
    // Initialize the CFG and display it.
    printf("\n--- Our CFG ---\n");
    symbols[0] = S;
    symbols[1] = E;
    symbols[2] = T;
    symbols[3] = plus;
    symbols[4] = n;
    rules[0] = rule1;
    rules[1] = rule2;
    rules[2] = rule3;
    rules[3] = rule4;
    int symbol_count = sizeof(symbols)/sizeof(symbols[0]);
    int rule_count = sizeof(rules)/sizeof(rules[0]);
    init_CFG(&cfg, symbols, symbol_count, S, rules, rule_count);
    printCFG(cfg);
	
    return 0;
}
