#NAME: Bundhoo Simriti
#STUDENT ID:1006281
#PART 1: Substitution Cipher

import subprocess

with open('story_cipher.txt', mode='r', encoding="utf-8", newline="\n") as fin:
    cipher_text = fin.read()

#unigram
# Create a dictionary to store character frequencies
frequencies_dict ={}
for line in cipher_text:
    for character in line:
        if character.isalpha():  
            if character in frequencies_dict:
                frequencies_dict[character] += 1
            else:
                frequencies_dict[character] = 1
# Sort the dictionary in descending order of values
sorted_frequencies_dict = dict(sorted(frequencies_dict.items(), key=lambda item: item[1], reverse=True))

print("The letter frequency in 'story_cipher.txt' is: ", sorted_frequencies_dict)

#mapping dictionary for substituting letters
mapping_dict = {
    'U': 'E',
    'J': 'T',
    'Y': 'I',
    'I': 'S',
    'E': 'O',
    'D': 'N',
    'Q': 'A',
    'X': 'H',
    'H': 'R',
    'B': 'L',
    'T': 'D',
    'W': 'G',
    'C': 'M',
    'S': 'C',
    'O': 'Y',
    'K': 'U',
    'M': 'W',
    'V': 'F',
    'F': 'P',
    'R': 'B',
    'L': 'V',
    'A': 'K',
    'N': 'X',
    'Z': 'J',
    'P': 'Q',
    'G': 'Z'
}
decoded_text = ""
for line in cipher_text:
    decoded_line = ""
    for character in line:
        if character.isalpha():
            if character.upper() in mapping_dict:
                decoded_line += mapping_dict[character.upper()]
            else:
                decoded_line += character
        else:
            decoded_line += character
    decoded_text += decoded_line

#print(decoded_text)

# Write the decoded text to the solution.txt file
with open('solution.txt', mode='w', encoding="utf-8", newline="\n") as fout:
    fout.write(decoded_text)

subprocess.Popen(["open", 'solution.txt'])