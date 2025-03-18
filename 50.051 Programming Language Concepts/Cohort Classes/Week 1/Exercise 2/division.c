# include <stdio.h>
# include "division.h"

// Assume n and val are positive integers

int main(int argc, char const *argv[])
{
    int a = divide_by_2_power_n(8, 3);
    printf("%d\n", a);

}

int divide_by_2_power_n(int val, int n) 
{
    return val >> n;

}