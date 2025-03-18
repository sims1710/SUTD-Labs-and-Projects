#include <stdio.h>
#include <stdlib.h>

int sort_array(const void *a, const void *b)
{
    int val_a = *(int *)a;
    int val_b = *(int *)b;

    // can simply do return val_a - val_b
    // do val_b - val_a to reverse the order

    if (val_a == val_b) return 0;
    else if (val_b > val_a) return 1;
    else return -1;
}

void print_array(int * arr)
{
    int i;
    for (i=0; i<20; i++)
    {
        printf("%d", arr[i]);
    }
    printf("\n");
}

int main(int argc, char ** argv)
{
    int * list[20] = {0, 56, 43, 32, 12, 5, 3, 12, 45, 33, 132, 5, 39, 2, 53, 38, 6, 78, 56, 90};
    print_array(list);
    qsort((void *)list, 20, sizeof(int), sort_array);
    print_array(list);
    return 0;
}