# include <stdio.h>
# include "ascii_table.h"

int main(int argc, char const *argv[]) 
{
    print_ascii_table();
}

void print_ascii_table()
{
    int i;
    for (i = 0; i <= 255; i++) 
    {
        printf("%d: %c\n", i, i);
    }
}