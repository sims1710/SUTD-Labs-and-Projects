# NAME: Bundhoo Simriti
# Student ID: 1006281
from Crypto.PublicKey import RSA
from ex2 import square_multiply, encrypt_rsa, decrypt_rsa

if __name__ == "__main__":
    public_key_path = 'mykey.pem.pub'
    private_key_path = 'mykey.pem.priv'
    pubkey = open(public_key_path, 'r').read()
    rsakey = RSA.import_key(pubkey)
    n = rsakey.n

    # Step 1
    integer = 100
    y = encrypt_rsa(str(integer), public_key_path)  # Convert integer to string

    # Step 2
    s = 2
    ys = square_multiply(y, s, n)

    # Step 3
    m = y * ys

    # Decrypt the message using the function from ex2.py
    decrypted = decrypt_rsa(m, private_key_path)

    print("Part II-------------")
    print("Encrypting:", integer)
    print("Result:")
    print(hex(m))
    print("Modified to:")
    print(hex(decrypted))
    print("Decrypted:", decrypted)
