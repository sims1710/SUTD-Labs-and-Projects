# include <stdio.h>
# include "factorial.h"

int main(int argc, char const *argv[]) 
{
    int n = 5;
    printf("Factorial of %d through loop is %d\n", n, fact_loop(n));
    printf("Factorial of %d through recursion is %d\n", n, fact_rec(n));
}

int fact_loop(int n)
{
    int result = 1;
    for (int i = n; i > 0; i--)
    {
        result = result * i;
    }
    return result;
}

int fact_rec(int n)
{
    if (n == 1)
    {
        return 1;
    }
    else
    {
        return n * fact_rec(n-1);
    }
}
