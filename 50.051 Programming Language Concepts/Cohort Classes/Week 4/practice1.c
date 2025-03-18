#include<stdio.h>
#include<string.h>

/* Defining the states */
typedef enum {
    ZERO,
    FIFTY_CENTS,
    ONE_DOLLAR,
    ONE_DOLLAR_FIFTY
} State;

/* Defining the FSM */
typedef struct {
    State current_state;
    int balance;
    #define MAX_BALANCE 1.5
} FSM;

/* Defining the transitions */
void update_state(FSM *f, char *input)
{
    if (input == "0.5")
    {
        f->current_state = FIFTY_CENTS;

    }

}

/* Main */
int main()
{
    char input_str[10];
    /* Initialise the FSM */
    FSM f = {ZERO};

    while (f.current_state != 1.5)
    {
        printf("Input options: 0.5, 1, B");
        scanf("%f", &input_str);
        update_state(&f, input_str);
    }

    return 0;
}