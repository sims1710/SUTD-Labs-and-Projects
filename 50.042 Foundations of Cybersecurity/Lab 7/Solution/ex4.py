# NAME: Bundhoo Simriti
# Student ID: 1006281
from Crypto.PublicKey import RSA 
from ex2 import square_multiply, encrypt_rsa
import random

if __name__ == "__main__":
    public_key_path = 'mykey.pem.pub'
    
    #Alice's part
    #Step 1
    key = open(public_key_path, 'r').read()
    rsakey = RSA.import_key(key)
    modulus = rsakey.n
    exponent = rsakey.e
    
    #Step 2
    s = random.getrandbits(1024)
    
    #Step 3
    x = square_multiply(s,exponent, modulus)
    
    signature, message = s, x
    
    #Bob's part
    #Step 1
    x_prime = encrypt_rsa(str(signature), public_key_path)
    
    #Step 2
    if x_prime == message:
        print("The signature is valid!")
    else:
        print("The signature is not valid!")
    