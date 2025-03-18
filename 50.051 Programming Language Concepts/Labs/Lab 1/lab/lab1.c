#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "lab1.h"

int main(int argc, char ** argv) {
    int j;
    float *frequency = (float *) malloc(NUM_LETTERS * sizeof(float));
    float engDifference;
    float frenchDifference;
    char *text;

    char ** sentenceArray;
    char *text_part_2;
    char *text_part_3;
    char *original_text;
    char *new_text;

    /* PART 1: (Trying to) Detect the Language of a text */ 

     /* Initialsing a pointer to float */
    for (j= 0; j < NUM_LETTERS; j++) 
    {
        frequency[j] = 0.0;
    }

    /* Checking the value of argc */
    if (argc != 2) 
    {
        printf("Error: Incorrect value of argc!");
        return 1;
    }
    printf("File name: %s\n", argv[1]);
    
    /* Reading a textfile and returning as an array of characters */
    text = file_to_str(argv[1]);
    if (text == NULL) 
    {
        printf("Error: You didn't input a textfile or your textfile is empty!");
        return 1;
    }

    /* printf("Text: %s\n", text); */
    printf("PART 1: Detecting the language of a text\n");
    printf("----------------------------------------------------------------\n");

    /* Computing the frequency of every letter */
    calculate_letter_frequency(text, frequency);

    /* Computing the difference between observed and expected frequencies */
    engDifference = calculate_difference(frequency, englishFreq);
    frenchDifference = calculate_difference(frequency, frenchFreq);

    if (engDifference < frenchDifference)
    {
        printf("The text is likely to be English\n");
    }
    else
    {
        printf("The text is likely to be French\n");
    }
    printf("----------------------------------------------------------------\n");
    printf("\n\n");

    /* PART 2: String Processing */
    printf("PART 2: String Processing\n");
    printf("----------------------------------------------------------------\n");
    printf("\n");
    printf("Splitting a text into sentences\n");
    printf("----------------------------------------------------------------\n");
    printf("\n");

    /* Splitting a text into sentences */
    text_part_2 = file_to_str(argv[1]);
    sentenceArray = split_sentences(text_part_2);

    /* Printing every string in the array */
    print_string_array(sentenceArray);

    /* Freeing memory */
    free_sentences(sentenceArray);

    /* Replacing words/phrases in a text */
    printf("Replacing words/phrases in a text\n");
    printf("----------------------------------------------------------------\n");
    printf("\n");
    /* char *original_text = "The quick brown fox jumps over the laZy dog. What a fox!"; */
    /* char *wordpair[] = {"fox", "squirrel", "lazy", "handsome", NULL}; */
    text_part_3 = file_to_str(argv[1]);
    original_text = text_part_3;

    /* If text is French or English, use correct wordpair[] array */
    if (engDifference < frenchDifference)
    {
        char *wordpair[] = {"day", "star", "air", "water", NULL}; /* English */
        new_text = change_wording(original_text, (const char **) wordpair);
    }
    else
    {
        char *wordpair[] = {"journee", "etoile", "fleurs", "gateaux", NULL}; /* French */
        new_text = change_wording(original_text, (const char **) wordpair);
    }

    /*
    NOTE TO THE PROFS:

    The english wordpair works best with the english-test1.txt file.
    The french wordpair works best with the french-test1.txt file.

    */

    printf("Original text: %s\n", original_text);
    printf("----------------------------------------------------------------\n");
    printf("\n");
    printf("New text: %s\n\n", new_text);

    /* Freeing the other memory spaces */
    free(frequency); 
    free(new_text);

    return 0;
}

/* DO NOT MODIFY THIS FUNCTION */
char *file_to_str(const char *x){FILE*y;char*z;size_t s;size_t i;y=fopen(x,"r");if(!y){printf("Err");return NULL;}fseek(y,0,2);s=ftell(y);rewind(y);z=(char*)malloc((s+1)*sizeof(char));if(!z){printf("Err");fclose(y);return NULL;}for(i=0;i<s;i++){z[i]=fgetc(y);}z[s]='\0';return z;}

void calculate_letter_frequency(char *text, float *frequency)
{
    int i;
    int sizeOfText = strlen(text);
    int totalLetters = 0;

    for (i=0; i < sizeOfText; i++) 
    {
        text[i] = to_uppercase(&text[i]);
        if (text[i] >= 'A' && text[i] <= 'Z') 
        {
            frequency[text[i] - 'A']++; 
            totalLetters++;
        }
    }

    for (i=0; i < NUM_LETTERS; i++) 
    {
        frequency[i] = frequency[i] / totalLetters;
    }

    /* Printing the frequency array 
    for (i=0; i < NUM_LETTERS; i++) 
    {
        printf("%f\n", frequency[i]);
    }*/
    
}

char to_uppercase(const char *c)
{
    if (*c >= 'a' && *c <= 'z') 
    {
        return *c - 32;
    }
    else 
    {
        return *c;
    }
}

char to_lowercase(const char *c)
{
    if (*c >= 'A' && *c <= 'Z') 
    {
        return *c + 32;
    }
    else 
    {
        return *c;
    }
}

float calculate_difference(const float *observedFreq, const float *expectedFreq) 
{
    float difference = 0.0;
    int i;

    for (i = 0; i < NUM_LETTERS; i++) 
    {
        difference += (observedFreq[i] - expectedFreq[i]) * (observedFreq[i] - expectedFreq[i]);
    }
    return difference;
}

char ** split_sentences(const char *text) {
    int i;
    int textSize = strlen(text);
    int sentenceCount = 0;
    int sentenceIndex = 0; /* Track position in current sentence */

    char **sentenceArray = (char **) malloc(MAX_SENTENCES * sizeof(char *));
    char *sentence = (char *) malloc(MAX_SENTENCE_LENGTH * sizeof(char));

    if (sentenceArray == NULL || sentence == NULL) 
    {
        printf("Error: Memory allocation failed!");
        if (sentenceArray != NULL)
            free(sentenceArray);
        if (sentence == NULL)
            free(sentence);
        return NULL;
    }

    for (i = 0; i <= textSize; i++) 
    {
        if (text[i] == '.' || text[i] == '?' || text[i] == '!') 
        {
            sentence[sentenceIndex] = text[i];
            sentenceIndex++;
            sentence[sentenceIndex] = '\0';
            sentenceArray[sentenceCount] = strdup(sentence);

            if (sentenceArray[sentenceCount] == NULL) 
            {
                printf("Error: Memory allocation failed!");
                for (i = 0; i < sentenceCount; i++)
                    free(sentenceArray[i]);
                free(sentenceArray);
                free(sentence);
                return NULL;
            }

            sentenceCount++;
            sentenceIndex = 0;
        } else if (sentenceIndex < MAX_SENTENCE_LENGTH - 1) 
        {
            sentence[sentenceIndex] = text[i];
            sentenceIndex++;
        }
    }

    sentenceArray[sentenceCount] = NULL;
    return sentenceArray;
}

void print_string_array(char **sentenceArray) 
{
    int i;
    for (i = 0; sentenceArray[i] != NULL; i++) 
    {
        printf("Sentence %d: %s\n\n", i + 1, sentenceArray[i]);
    }
}

void free_sentences(char **sentenceArray) 
{
    int i;
    for (i = 0; sentenceArray[i] != NULL; i++) 
    {
        free(sentenceArray[i]);
    }
    free(sentenceArray);
}

char *change_wording(char *text, const char *wordpair[]) {
    int i, j;
    int textSize = strlen(text);
    int newTextSize = 0;
    int replaced;
    char *newText = (char *)malloc((MAX_SENTENCE_LENGTH + 1) * sizeof(char));
    char *word;
    word = (char *)malloc((MAX_SENTENCE_LENGTH + 1) * sizeof(char));
    
    if (newText == NULL) {
        printf("Error: Memory allocation failed!\n");
        exit(1);
    }

    newText[0] = '\0';

    
    if (word == NULL) {
        printf("Error: Memory allocation failed!\n");
        free(newText);
        exit(1);
    }

    for (i = 0; i <= textSize; i++) {
        if (text[i] == ' ' || text[i] == '\0' || text[i] == '.' || text[i] == '!' || text[i] == '?' || text[i] == ',') {
            word[newTextSize] = '\0';
            newTextSize = 0;
            replaced = 0;

            /* Process the word before the punctuation mark */
            for (j = 0; wordpair[j] != NULL && wordpair[j + 1] != NULL; j += 2) {
                if (case_insensitive_strcmp(word, wordpair[j]) == 0) {
                    strcat(newText, wordpair[j + 1]);
                    strcat(newText, " ");
                    replaced = 1;
                    break;
                }
            }

            if (!replaced) {
                strcat(newText, word);
                strcat(newText, " ");
            }

            /* Append the punctuation mark */
            if (text[i] == '.' || text[i] == '!' || text[i] == '?' || text[i] == ',') 
            {
                word[newTextSize] = text[i];
                newTextSize++;
                if (newText[strlen(newText) - 1] == ' ') 
                {
                    newText[strlen(newText) - 1] = '\0';
                }
            }
        } else if (newTextSize < MAX_SENTENCE_LENGTH - 1) {
            word[newTextSize] = to_lowercase(&text[i]); 
            newTextSize++;
        }
    }
    free(word);
    return newText;
}

int case_insensitive_strcmp(const char *s1, const char *s2) {
    while (*s1 && *s2) {
        if (to_lowercase(s1) != to_lowercase(s2))
            return (to_lowercase(s1) - to_lowercase(s2));
        s1++;
        s2++;
    }
    return to_lowercase(s1) - to_lowercase(s2);
}

/*
Name: Bundhoo Simriti
Student ID: 1006281
*/
