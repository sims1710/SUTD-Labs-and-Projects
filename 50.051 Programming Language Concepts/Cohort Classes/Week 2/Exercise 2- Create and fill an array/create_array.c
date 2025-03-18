# include <stdio.h>
# include "create_array.h"
# include <stdlib.h>


void print_array(int arr[], int size)
{
    int i;
    for (i = 0; i < size; i++)
    {
        printf("arr[%d] = %d\n", i, arr[i]);

    }
}

int main(int argc, char const *argv[]) 
{
    int arr[] = {1,2,3,4};
    int size = sizeof(arr)/sizeof(arr[0]); // instead of arr[0], can use int
    print_array(arr, size);
    create_dynamic_array();
    return 0;
}

void create_dynamic_array ()
{
    int * my_array;
    int n;
    int i;
    printf("Please enter the size of the array: ");
    scanf("%d", &n);

    my_array = (int *) malloc(sizeof(int) * n);
    printf("Address: %p, Size: %d\n", (void *) my_array, n);

    for (i = 0; i < n; i++)
    {
        printf("Please enter a number:");
        scanf("%d", &my_array[i]);

        /*
        CAN USE:
        scanf("%d", (my_array + i));
        OR
        scanf("%d", &val);
        my_array[i] = val;
        */

    }
    print_array(my_array, n);
    free(my_array);
}