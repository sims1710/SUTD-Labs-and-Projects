#!/usr/bin/env python3
# ECB plaintext extraction skeleton file for 50.042 FCS

#Name: Bundhoo Simriti
#Student Id: 1006281

import argparse
from collections import defaultdict

def getInfo(headerfile):
    with open(headerfile, mode='rb') as fin:
        info = fin.read()
    return info

def extract(infile, outfile, headerfile):
    header_info = getInfo(headerfile)

    with open(infile, mode='rb') as finfile:
        finfile.read(len(header_info))
        
        with open(outfile, mode='wb') as fout:
            fout.write(header_info)
            fout.write(b'\n')

            frequency_dict = defaultdict(int)

            while (bytes := finfile.read(8)):
                # Frequency analysis
                frequency_dict[bytes] += 1

            frequency_dict_sorted = sorted(frequency_dict, key=frequency_dict.get, reverse=True)

            for bytes in frequency_dict_sorted:
                if bytes == frequency_dict_sorted[0]:
                    fout.write(b'00000000')
                else:
                    fout.write(b'11111111')

        print("Process completed")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Extract PBM pattern.')
    parser.add_argument('-i', dest='infile', help='input file, PBM encrypted format')
    parser.add_argument('-o', dest='outfile', help='output PBM file')
    parser.add_argument('-hh', dest='headerfile', help='known header file')

    args = parser.parse_args()
    infile = args.infile
    outfile = args.outfile
    headerfile = args.headerfile

    print('Reading from: %s' % infile)
    print('Reading header file from: %s' % headerfile)
    print('Writing to: %s' % outfile)

    extract(infile, outfile, headerfile)