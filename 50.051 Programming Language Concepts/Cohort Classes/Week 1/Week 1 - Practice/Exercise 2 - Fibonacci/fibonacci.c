# include <stdio.h>
# include "fibonacci.h"

int main(int argc, char const *argv[]) 
{
    int n = 5;
    printf("Fibonacci of %d is %d\n", n, fibo(n));
}

int fibo (int n) 
{
    int i = 1;
    int t1 = 0;
    int t2 = 1;
    int result;

    if (n == 0) return 0;
    if (n == 1) return 1;

    do
    {
        result = t1 + t2;
        t1 = t2;
        t2 = result;
        i++;
    } 
    while (i < n);
    return result;
}
