#!/usr/bin/env python3
# SUTD 50.042 FCS Lab 1
# Simple file read in/out

#NAME: Bundhoo Simriti
#STUDENT ID:1006281
#PART 1: shift cipher for printable input    

# Import libraries
import sys
import argparse
import string
import subprocess
import os

def doStuff(filein, fileout, key, mode):
    # open file handles to both files
    fin = open(filein, mode="r", encoding="utf-8", newline="\n")  # read mode
    fin_b = open(filein, mode="rb")  # binary read mode
    fout = open(fileout, mode="w", encoding="utf-8", newline="\n")  # write mode
    fout_b = open(fileout, mode="wb")  # binary write mode
    c = fin.read()  # read in file into c as a str
    # and write to fileout

    # close all file streams
    fin.close()
    fin_b.close()
    fout.close()
    fout_b.close()

    # PROTIP: pythonic way
    with open(filein, mode="r", encoding="utf-8", newline="\n") as fin:
        text = fin.read()
        # do stuff

    if not os.path.exists(fileout):
        with open(fileout, mode="w", encoding="utf-8", newline="\n"):
            pass
    
    with open(fileout, mode="w", encoding="utf-8", newline="\n") as fout:
        text_length = len(text)
        for index in range(text_length):
            character = text[index]
            if character in string.printable:
                if mode == 'e':
                    new_character = e(character, key) #encryption
                else:
                    new_character = d(character, key) #decryption
            else: #return non-string.printable character as it is
                new_character = character
            fout.write(new_character)

def e(character, key): #encryption function
    new_character_index = (string.printable.index(character) + key) % len(string.printable)
    new_character = string.printable[new_character_index]
    return new_character

def d(character, key): #decryption function
    new_character_index = (string.printable.index(character) - key) % len(string.printable)
    new_character = string.printable[new_character_index]
    return new_character
       
# file will be closed automatically when interpreter reaches end of the block

# our main function
if __name__ == "__main__":
    # set up the argument parser

    parser = argparse.ArgumentParser()
    parser.add_argument("-i", dest="filein", help="input file")
    parser.add_argument("-o", dest="fileout", help="output file")
    parser.add_argument("-k", dest="key", type=int, help="cipher key", default= 1)
    parser.add_argument("-m", dest="mode", help= "mode: 'e' for encryption, 'd' for decryption", default='e')

    # parse our arguments
    args = parser.parse_args()
    filein = args.filein #input
    fileout = args.fileout #output
    key = args.key #cipher key
    mode = args.mode #'e' or 'd'

    valid = True

    if key < 0:
        print("ERROR! Invalid key")
        valid = False
    elif key > len(string.printable):
        print("ERROR! The key is longer than the number of valid characters in the file")
        valid = False

    if mode == 'e' or mode == 'E':
        mode = 'e'
    elif mode == 'd' or mode == 'D':
        mode = 'd'
    else:
        print("ERROR! Invalid character, please type 'e' for encryption and 'd' for decryption.")
        valid = False
    
    if valid:
        doStuff(filein, fileout, key, mode)
        subprocess.Popen(["open", fileout])
        #print("Process completed")

    # all done