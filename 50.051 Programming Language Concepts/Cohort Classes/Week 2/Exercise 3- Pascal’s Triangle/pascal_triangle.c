# include <stdio.h>
# include "pascal_triangle.h"
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
    int level = 7;
    create_pascal_triangle(level);
    return 0;
}

void create_pascal_triangle(int level)
{
    int i,j;
    int ** my_array;
    my_array = (int **) malloc(sizeof(int *) * level); // malloc the int ** with enough space to store level x int *
    // each int * is going to represent a single row of the triangle

    // malloc every single int * (each row is of a different size)

    for (i = 0; i < level; i++)
    {
        my_array[i] = (int *) malloc((i +1) * sizeof(int));
        for (j = 0; j <= i; j++)
        {
            if (j == 0 || j == i)
            {
                my_array[i][j] = 1;
            }
            else
            {
                my_array[i][j] = my_array[i-1][j-1] + my_array[i-1][j];
            }
        }
        print_array(my_array[i], i + 1);
    }

    for(i = 0; i < level; i++)
    {
        free(my_array[i]);
    }
    free(my_array);
}