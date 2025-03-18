#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void function_a(int a)
{
    char *s = "This will be at the midterm exam";
    *s = 'd';
    exit(0);
}

void process_a (int a)
{
    printf("A value is: %d\n", a);
    exit(a%2);

}

int main(int argc, char ** argv)
{
    int a;
    atexit(process_a);
    scanf("%d", &a);
    if (a == 1)
    {
        printf("Before exiting from main with 0\n");
        return 0;
    }
    else if (a> 0)
    {
        printf("Before exiting from main with 1\n");
        return 1;
    }
    else
    {
        function_a(a);
        printf("This code is never reached\n");
        return -13543534;
    }
}