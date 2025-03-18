# 50.042 FCS Lab 6 template
# Year 2021
# NAME: Bundhoo Simriti
# Student ID: 1006281

import math
import primes


def baby_step(alpha, beta, p):
    m = math.ceil(math.sqrt(p - 1))

    baby_steps = {}

    # Compute and store the values of alpha^(xb) * beta
    for xb in range(m):
        xb_val = primes.square_multiply(alpha, xb, p)
        xb_val = (xb_val * beta) % p
        baby_steps[xb_val] = xb

    return baby_steps


def giant_step(alpha, p):
    m = math.ceil(math.sqrt(p - 1))

    giant_steps = {}

    # Compute and store the values of alpha^(m * xg)
    for xg in range(m):
        xg_val = primes.square_multiply(alpha, m * xg, p)
        giant_steps[xg] = xg_val

    return giant_steps


def baby_giant(alpha, beta, p):
    m = math.ceil(math.sqrt(p - 1))

    baby_steps = baby_step(alpha, beta, p)
    giant_steps = giant_step(alpha, p)

    # Check for a match between baby steps and giant steps
    for xg, xg_val in giant_steps.items():
        if xg_val in baby_steps:
            xb = baby_steps[xg_val]
            x = m * xg - xb
            return x

    return None

if __name__ == "__main__":
    """
    test 1
    My private key is:  264
    Test other private key is:  7265
    """
    p = 17851
    alpha = 17511
    A = 2945
    B = 11844
    sharedkey = 1671
    a = baby_giant(alpha, A, p)
    b = baby_giant(alpha, B, p)
    guesskey1 = primes.square_multiply(A, b, p)
    guesskey2 = primes.square_multiply(B, a, p)
    print("Guess key 1:", guesskey1)
    print("Guess key 2:", guesskey2)
    print("Actual shared key :", sharedkey)
    
     # Key size: 16 bits
    p = 65537  # A prime number of 16 bits
    alpha = 3  # Primitive element or generator
    alice_priv_key = 12345
    bob_priv_key = 54321

    # Compute public keys
    alice_pub_key = pow(alpha, alice_priv_key, p)
    bob_pub_key = pow(alpha, bob_priv_key, p)

    # Perform the attack
    shared_key_attack = baby_giant(alpha, bob_pub_key, p)
    print("Attacker's guess for Bob's private key:", shared_key_attack)

    # Compute the actual shared key
    shared_key_actual = pow(bob_pub_key, alice_priv_key, p)
    print("Bob's actual shared key:", shared_key_actual)

