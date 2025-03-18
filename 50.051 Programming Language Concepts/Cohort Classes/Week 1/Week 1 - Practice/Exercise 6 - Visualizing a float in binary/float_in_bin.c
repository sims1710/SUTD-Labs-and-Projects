# include <stdio.h>
# include "float_in_bin.h"

int main(int argc, char const *argv[]) 
{
    float f = 2.5;
    print_float_bits(f);
}

void print_float_bits(float f)
{
    int now_an_int = *(int *) &f;
    int i;
    for (i = 0; i < 32; i++) 
    {
        printf("%d\n", (1 << i) & now_an_int ? 1 : 0); // printing towards the left, masking, if it's a number then it's 1 else 0
    }    
}