// An example for qsort and comparator 
#include <stdio.h> 
#include <stdlib.h> 

// A sample comparator function that is used 
// for sorting an integer array in ascending order. 
// To sort any array for any other data type and/or 
// criteria, all we need to do is write more compare 
// functions. And we can use the same qsort() 
int compare (const void * a, const void * b) 
{ 
return ( *(int*)a - *(int*)b ); 
} 

int main () 
{ 
int arr[] = {10, 5, 15, 12, 90, 80}; 
int n = sizeof(arr)/sizeof(arr[0]), i; 

qsort (arr, n, sizeof(int), compare); 

for (i=0; i<n; i++) 
	printf ("%d ", arr[i]); 


int *ptr1 = malloc(sizeof(int));
    *ptr1 = 10;

    int *ptr2 = ptr1; // Copying the pointer value

    // Both ptr1 and ptr2 point to the same memory address,
    // so changes to the value at that address are reflected
    // when either pointer is dereferenced.
    printf("Value at ptr1: %d\n", *ptr1); // Output: 10
    printf("Value at ptr2: %d\n", *ptr2); // Output: 10

    // Modify the value at the memory location using ptr1
    *ptr1 = 20;

    // Both ptr1 and ptr2 still point to the same memory address,
    // so changes made to the value at that address are visible
    // through both pointers.
    printf("Updated value at ptr1: %d\n", *ptr1); // Output: 20
    printf("Updated value at ptr2: %d\n", *ptr2); // Output: 20

    // Modify the value at the memory location using ptr2
    *ptr2 = 30;

    // Again, both ptr1 and ptr2 point to the same memory address,
    // so changes made to the value at that address are visible
    // through both pointers.
    printf("Updated value at ptr1: %d\n", *ptr1); // Output: 30
    printf("Updated value at ptr2: %d\n", *ptr2); // Output: 30

    // Free dynamically allocated memory
    free(ptr1);

    return 0;

return 0; 
} 

