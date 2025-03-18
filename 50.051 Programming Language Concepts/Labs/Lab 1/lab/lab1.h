#define NUM_LETTERS 26

#define MAX_SENTENCE_LENGTH 10000
#define MAX_SENTENCES 10000

/* Expected frequencies of letters in English and French texts (source: Wikipedia) */
const float englishFreq[NUM_LETTERS] = {
    0.0817, 0.0149, 0.0278, 0.0425, 0.1270, 0.0223, 0.0202, 
    0.0609, 0.0697, 0.0015, 0.0077, 0.0403, 0.0241, 0.0675, 
    0.0751, 0.0193, 0.0009, 0.0599, 0.0633, 0.0906, 0.0276, 
    0.0098, 0.0236, 0.0015, 0.0197, 0.0007                  
};

const float frenchFreq[NUM_LETTERS] = {
    0.07948, 0.00901, 0.03315, 0.03761, 0.1715, 0.01066, 0.00866, 
    0.00777, 0.07529, 0.00613, 0.00074, 0.05456, 0.02968, 0.07095, 
    0.05896, 0.02521, 0.01362, 0.06693, 0.07948, 0.07244, 0.06311, 
    0.05264, 0.01066, 0.01066, 0.00287, 0.00142                  
};

/* Do NOT remove */
char * file_to_str(const char *filename);

/* Part 1 */
void calculate_letter_frequency(char *text, float *frequency);
float calculate_difference(const float *observedFreq, const float *expectedFreq);

char to_uppercase(const char *);

/* Part 2 */
char **split_sentences(const char *text);
void print_string_array(char **strings);
void free_sentences(char **sentences);

char * change_wording(char *text, const char *wordpair[]);
char to_lowercase(const char *c);
int case_insensitive_strcmp(const char *s1, const char *s2);

/* DO NOT FORGET TO INCLUDE

char * change_wording(...)

*/
