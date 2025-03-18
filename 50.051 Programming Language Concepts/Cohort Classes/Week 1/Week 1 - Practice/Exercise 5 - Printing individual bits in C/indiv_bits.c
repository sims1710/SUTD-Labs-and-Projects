# include <stdio.h>
# include "indiv_bits.h"

int main(int argc, char const *argv[]) 
{
    char c = 'A';
    print_bits(c);
}

void print_bits(char c)
{
    int i;
    for (i = 0; i < 8; i++) 
    {
        printf("%d/n", (1 << i) & c ? 1 : 0); // printing towards the left, masking, if it's a number then it's 1 else 0
    }
}