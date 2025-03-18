import hashlib
import time
import subprocess

with open('hash5.txt', mode='r', encoding="utf-8", newline="\n") as fin:
    hashed_text = fin.read()
    hash_lines = hashed_text.split("\n")

decoded_hashes = []
computation_times =[]

character_list = "abcdefghijklmnopqrstuvwxyz0123456789" #26 letters + 10 numbers = 36 characters for 5 bits 
total_possible_combinations = len(character_list)**5 

#starting the brute force
start_time = time.time()
for i in range(total_possible_combinations):
    value = "".join(character_list[(i // (len(character_list) ** j)) % len(character_list)] for j in range(4, -1, -1))
    #print(value)
    hash_guess = hashlib.md5(value.encode("utf-8")).hexdigest()
    if hash_guess in hash_lines:
        decoded_hashes.append(f"{value}")
        if len(decoded_hashes) == len(hash_lines) - hash_lines.count(''):
            break
end_time = time.time()
computation_time = end_time - start_time
            
# Write the decoded text to the ex2_hash.txt file
with open('ex2_hash.txt', mode='w', encoding="utf-8", newline="\n") as fout:
    fout.write('\n'.join(decoded_hashes))

print(f"Total Computation Time: {computation_time} seconds")

# Calculate the average computation time
average_computation_time = computation_time/len(decoded_hashes) #len should be equal to 15
print(f"Average Computation Time: {average_computation_time} seconds")

subprocess.Popen(["open", 'ex2_hash.txt'])