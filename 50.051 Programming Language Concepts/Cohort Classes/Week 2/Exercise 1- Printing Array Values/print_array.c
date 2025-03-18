# include <stdio.h>
# include "print_array.h"

int main(int argc, char const *argv[]) 
{
    int arr[] = {1,2,3,4};
    int size = sizeof(arr)/sizeof(arr[0]); // instead of arr[0], can use int
    print_array(arr, size);
}

void print_array(int arr[], int size)
{
    int i;
    for (i = 0; i < size; i++)
    {
        printf("arr[%d] = %d\n", i, arr[i]);

    }
}

// Other Way
void print_array2(int *arr, int size)
{
    int i;
    for (i = 0; i < size; i++)
    {
        printf("arr[%d] = %d\n", i, *(arr+i)); // instead of *(arr+i), can use arr[i]
    }
}

/*
arr[0] = * arr
arr[i] = * (arr + i)
*/