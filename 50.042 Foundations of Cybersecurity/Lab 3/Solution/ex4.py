import hashlib
import random
import subprocess

# Load the hashes from ex2_hash.txt
with open('ex2_hash.txt', mode='r', encoding="utf-8", newline="\n") as fin:
    hashed_text = fin.read()
    hash_lines = hashed_text.split("\n")

# Salting algorithm
def salting():
    lowercase_list = "abcdefghijklmnopqrstuvwxyz"  # 26 letters + 10 numbers = 36 characters for 5 bits
    salted_passwords = []

    for password in hash_lines:
        random_letter = random.choice(lowercase_list)
        new_password = password + random_letter
        salted_passwords.append(new_password)
        if len(salted_passwords) == len(hash_lines) - hash_lines.count(''):
            break
    
    with open('plain6.txt', mode='w', encoding="utf-8", newline="\n") as fout:
        fout.write('\n'.join(salted_passwords))
 
 #Hashing algorithm       
def hashing():
    salted_hashes = []
    
    # Load the salted text from plain6.txt
    with open('plain6.txt', mode='r', encoding="utf-8", newline="\n") as filein:
        salted_text = filein.read()
        salted_lines = salted_text.split("\n")

    #starting the brute force
    for salted_hash in (salted_lines):
        hash_value = hashlib.md5(salted_hash.encode("utf-8")).hexdigest()
        salted_hashes.append(f"{salted_hash}:{hash_value}")
        if len(salted_hashes) == len(hash_lines) - hash_lines.count(''):
            break

    with open('salted6.txt', mode='w', encoding="utf-8", newline="\n") as fout:
        fout.write('\n'.join(salted_hashes))

if __name__ == "__main__":   
    salting()
    hashing()

subprocess.Popen(["open", 'plain6.txt'])    
subprocess.Popen(["open", 'salted6.txt'])