# 50.042 FCS Lab 6 template
# Year 2021
# NAME: Bundhoo Simriti
# Student ID: 1006281

import random
def square_multiply(a, x, n):
    y = 1
    n_b = x.bit_length()  # Number of bits in x

    for i in range(n_b - 1, -1, -1):
        # Square
        y = (y * y) % n

        # Multiply only if the bit of x at i is 1
        if (x >> i) & 1 == 1:
            y = (a * y) % n

    return y

def miller_rabin(n, a):
    if n == 2 or n == 3:
        return True
    if n < 2 or n % 2 == 0:
        return False

    # Write n-1 as 2^r * d
    r = 0
    d = n - 1
    while d % 2 == 0:
        r += 1
        d //= 2

    # Miller-Rabin primality test
    for _ in range(a):
        x = pow(a, d, n)
        if x == 1 or x == n - 1:
            continue
        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                break
        else:
            return False

    return True


def gen_prime_nbits(n):
    while True:
        p = random.getrandbits(n)
        p |= (1 << (n - 1)) | 1  # Set the MSB and LSB to 1

        if miller_rabin(p, 2):
            return p
        
if __name__=="__main__":
    print('Is 561 a prime?')
    print(miller_rabin(561,2))
    print('Is 27 a prime?')
    print(miller_rabin(27,2))
    print(miller_rabin(61,2))

    print('Random number (100 bits):')
    print(gen_prime_nbits(100))
    print('Random number (80 bits):')
    print(gen_prime_nbits(80))
