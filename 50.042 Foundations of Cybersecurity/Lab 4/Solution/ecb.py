#!/usr/bin/env python3
# ECB wrapper skeleton file for 50.042 FCS

#Name: Bundhoo Simriti
#Student Id: 1006281

from present import present, present_inv
import argparse
import subprocess

nokeybits = 80
blocksize = 64

def ecb(infile, outfile, keyfile, mode):
    outputs = []

    with open(keyfile, 'rb') as key_file:
        key_data = key_file.read()
        key_int = int.from_bytes(key_data, 'little')

    with open(infile, 'rb') as in_file:
        while (in_bytes := in_file.read(blocksize // 8)):
            in_int = int.from_bytes(in_bytes, 'little')
            
            if mode.lower() == 'e':
                outputs.append(present(in_int, key_int))
            elif mode.lower() == 'd':
                outputs.append(present_inv(in_int, key_int))
            else:
                print("ERROR! Invalid character, please type 'e' for encryption and 'd' for decryption.")
                return

    with open(outfile, 'wb') as out_file:
        for output in outputs:
            out_file.write(output.to_bytes(blocksize // 8, 'little'))

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Block cipher using ECB mode.')
    parser.add_argument('-i', dest='infile', help='input file')
    parser.add_argument('-o', dest='outfile', help='output file')
    parser.add_argument('-k', dest='keyfile', help='key file')
    parser.add_argument('-m', dest='mode', help='mode')

    args = parser.parse_args()
    infile = args.infile
    outfile = args.outfile
    keyfile = args.keyfile
    mode = args.mode

    ecb(infile, outfile, keyfile, mode)
    subprocess.Popen(["open", outfile])
    print("Process completed")