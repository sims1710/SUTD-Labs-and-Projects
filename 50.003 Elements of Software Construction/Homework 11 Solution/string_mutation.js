function mutateString(input) {
    // Check if the input string is empty or has only one character
    if (input.length < 2) {
      return input; // Return the input string as it is
    }
  
    // Choose a random position between 0 and the second-to-last index of the string
    const position = Math.floor(Math.random() * (input.length - 1));
  
    // Swap the characters at the chosen position and the next position
    const mutatedString = input.slice(0, position) +
                          input.charAt(position + 1) +
                          input.charAt(position) +
                          input.slice(position + 2);
  
    return mutatedString;
}

// Testing the code
const inputString = "SUTD";
const mutatedString = mutateString(inputString);
console.log(mutatedString); 