#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main (int argc, char ** argv)
{
    // use current datetime as seed
    srand(time(NULL)); 
    
    // printf("%d, %d\n", rand(), rand()); will return the same values over and over again => need to use seed
    printf("%d, %d\n", rand()%10, rand()%10); // between 0 - 10
    return 0;
}
