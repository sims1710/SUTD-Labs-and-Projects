# include <stdio.h>
# include "prime_numbers.h"

int main(int argc, char const *argv[]) 
{
    int n = 23;
    printf("Prime number of %d is %d\n", n, is_prime(n));
}

int is_prime (int x)
// Loop between 1 and x/2
{
    int i;

    if (x < 3) return 1;

    for ( i = 2; i < x/2; i++)
    {
        if (x % i == 0)
        {
            return 0; // not prime
        }
    }
    return 1; // prime
}
