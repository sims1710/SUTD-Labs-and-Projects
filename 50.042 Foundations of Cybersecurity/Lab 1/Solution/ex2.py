#!/usr/bin/env python3
# SUTD 50.042 FCS Lab 1
# Simple file read in/out

#NAME: Bundhoo Simriti
#STUDENT ID:1006281
#PART 2: shift cipher for binary input    

# Import libraries
import sys
import argparse
import subprocess
import os

def doStuff(filein, fileout, key, mode):
    # open file handles to both files
    fin = open(filein, mode="r", encoding="utf-8", newline="\n")  # read mode
    fin_b = open(filein, mode="rb")  # binary read mode
    fout = open(fileout, mode="w", encoding="utf-8", newline="\n")  # write mode
    fout_b = open(fileout, mode="wb")  # binary write mode
    
    # and write to fileout

    # close all file streams
    fin.close()
    fin_b.close()
    fout.close()
    fout_b.close()

    # PROTIP: pythonic way
    with open(filein, mode="rb") as fin:
        data = fin.read()
        # do stuff
    modified_data = bytearray()

    if not os.path.exists(fileout):
        with open(fileout, mode="wb"):
            pass
      
    with open(fileout, mode="wb") as fout:
        for byte in data:
            if mode == 'e':
                modified_byte = e(byte, key)
            else:
                modified_byte = d(byte, key)
            modified_data.append(modified_byte)
        fout.write(modified_data)

def e(byte, key): #encryption function
    encrypted_byte = (byte + key) % 256
    return encrypted_byte

def d(byte, key): #decryption function
    decrypted_byte = (byte - key) % 256
    return decrypted_byte
       
# file will be closed automatically when interpreter reaches end of the block

# our main function
if __name__ == "__main__":
    # set up the argument parser

    parser = argparse.ArgumentParser()
    parser.add_argument("-i", dest="filein", help="input file")
    parser.add_argument("-o", dest="fileout", help="output file")
    parser.add_argument("-k", dest="key", type=int, help="cipher key", default= 1)
    parser.add_argument("-m", dest="mode", help= "mode: 'e' for encryption, 'd' for decryption", default= 'e')

    # parse our arguments
    args = parser.parse_args()
    filein = args.filein #input
    fileout = args.fileout #output
    key = args.key #cipher key
    mode = args.mode #'e' or 'd'

    valid = True

    if key < 0 and key > 255:
        print("ERROR! Invalid key")
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
