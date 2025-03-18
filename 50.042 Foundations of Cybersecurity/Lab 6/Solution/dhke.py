# 50.042 FCS Lab 6 template
# Year 2021
# NAME: Bundhoo Simriti
# Student ID: 1006281

import primes
import random

def dhke_setup(nb):
    # Choose a large prime p
    p = primes.gen_prime_nbits(nb)

    # Choose an integer alpha, a primitive element or generator in the group
    alpha = random.randint(2, p - 2)

    # Save the values of p and alpha
    return p, alpha


def gen_priv_key(p):
    # Choose a random private key
    priv_key = random.randint(2, p - 2)
    return priv_key


def get_pub_key(alpha, priv_key, p):
    # Compute the public key
    pub_key = pow(alpha, priv_key, p)
    return pub_key


def get_shared_key(keypub, keypriv, p):
    # Compute the shared key
    shared_key = pow(keypub, keypriv, p)
    return shared_key


if __name__ == "__main__":
    p, alpha = dhke_setup(80)
    print("Generate P and alpha:")
    print("P:", p)
    print("alpha:", alpha)
    print()
    a = gen_priv_key(p)
    b = gen_priv_key(p)
    print("My private key is: ", a)
    print("Test other private key is: ", b)
    print()
    A = get_pub_key(alpha, a, p)
    B = get_pub_key(alpha, b, p)
    print("My public key is: ", A)
    print("Test other public key is: ", B)
    print()
    sharedKeyA = get_shared_key(B, a, p)
    sharedKeyB = get_shared_key(A, b, p)
    print("My shared key is: ", sharedKeyA)
    print("Test other shared key is: ", sharedKeyB)
    print("Length of key is %d bits." % sharedKeyA.bit_length())
