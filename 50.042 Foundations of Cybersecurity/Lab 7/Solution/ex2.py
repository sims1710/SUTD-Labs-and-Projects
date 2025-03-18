# NAME: Bundhoo Simriti
# Student ID: 1006281
from Crypto.PublicKey import RSA 
from Crypto.Hash import SHA256

def square_multiply(a, x, n):
    y = 1
    n_b = x.bit_length()  # Number of bits in x

    for i in range(n_b - 1, -1, -1):
        # Square
        y = (y * y) % n

        # Multiply only if the bit of x at i is 1
        if x & (1 << i):
            y = (a * y) % n
    return y

def encrypt_rsa(message, public_key_path):
    key = open(public_key_path,'rb').read()
    rsakey = RSA.importKey(key)
    
    #Obtaining modulus and exponent
    modulus = rsakey.n
    exponent = rsakey.e
    
    #Encrypt the message
    message_int = int_from_bytes(string_to_bytes(message))
    encrypted_message = square_multiply(message_int, exponent, modulus)
    return encrypted_message

def decrypt_rsa(encrypted_message, private_key_path):
    key = open(private_key_path,'rb').read()
    rsakey = RSA.import_key(key)
    
    #Obtaining modulus and exponent
    modulus = rsakey.n
    exponent = rsakey.d
    
    #Decrypt the message
    decrypted_message = square_multiply(encrypted_message, exponent, modulus)
    return decrypted_message

# convert from int to hash
def int_to_bytes(integer):
    return integer.to_bytes((integer.bit_length() + 7) // 8, 'big')

def int_from_bytes(xbytes):
    return int.from_bytes(xbytes, 'big')

def string_to_bytes(string):
    return bytes(string, 'utf-8')

if __name__ == "__main__":
    public_key_path = 'mykey.pem.pub'
    private_key_path = 'mykey.pem.priv'
    message = 'message.txt'

    plaintext = open(message, 'r').read()

    # Hash the plaintext using SHA-256
    hash_algo = SHA256.new()
    hashed_txt = hash_algo.update(plaintext.encode())
    message_digest = hash_algo.hexdigest()

    # Encrypt the hashed message using RSA public key
    encrypted_message = encrypt_rsa(message_digest, public_key_path)

    # Decrypt the encrypted message using RSA private key
    decrypted_message = decrypt_rsa(encrypted_message, private_key_path)

    # convert int to the hash
    hashed_res = int_to_bytes(decrypted_message).decode()

    if message_digest == hashed_res:
        print('The signatures are the same!')
    else:
        print('The decrypted and the original signatues are not the same!')