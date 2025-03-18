# NAME: Bundhoo Simriti
# Student ID: 1006281
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256
from base64 import b64encode, b64decode

def generate_RSA(bits = 1024):
    key = RSA.generate(bits)
    private_key = key.export_key()
    public_key = key.publickey().export_key()
    return private_key, public_key

def encrypt_RSA(public_key_file, message):
    key = RSA.import_key(open(public_key_file, 'rb').read())
    cipher_rsa = PKCS1_OAEP.new(key)
    ciphertext = cipher_rsa.encrypt(message.encode('utf-8'))
    return b64encode(ciphertext).decode('utf-8')

def decrypt_RSA(private_key_file, cipher):
    key = RSA.import_key(open(private_key_file, 'rb').read())
    cipher_rsa = PKCS1_OAEP.new(key)
    decrypted_message = cipher_rsa.decrypt(b64decode(cipher)).decode('utf-8')
    return decrypted_message

def sign_data(private_key_file, data):
    key = RSA.import_key(open(private_key_file, 'rb').read())
    h = SHA256.new(data.encode('utf-8'))
    signature = pkcs1_15.new(key).sign(h)
    return b64encode(signature).decode('utf-8')

def verify_sign(public_key_file, sign, data):
    key = RSA.import_key(open(public_key_file, 'rb').read())
    h = SHA256.new(data.encode('utf-8'))
    try:
        pkcs1_15.new(key).verify(h, b64decode(sign))
        return True
    except (ValueError, TypeError):
        return False

if __name__ == "__main__":
    private_key_file = 'private_key.pem'
    public_key_file = 'public_key.pem'
    data_file = 'mydata.txt'
    
    #Generate keys
    private_key, public_key = generate_RSA()
    with open(private_key_file, 'wb') as f:
        f.write(private_key)
    with open(public_key_file, 'wb') as f:
        f.write(public_key)
        
    #Encrypt RSA
    with open(data_file, 'rb') as f:
        plaintext = f.read().decode('utf-8')
    ciphertext = encrypt_RSA(public_key_file, plaintext)
    
    #Decrypt RSA
    decrypted_text = decrypt_RSA(private_key_file, ciphertext)
    
    #Sign the file
    signature = sign_data(private_key_file, plaintext)
    
    #Verify signature
    is_valid = verify_sign(public_key_file, signature, plaintext)
    if is_valid:
        print("The signature is valid!")
    else:
        print("The signature is not valid!")