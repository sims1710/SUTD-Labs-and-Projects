function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
  
function generateExpr() {
    const rules = ['Expr + Term', 'Expr - Term', 'Term'];
    const rule = rules[getRandomInt(0, rules.length - 1)];
  
    if (rule === 'Expr + Term') {
      return generateExpr() + ' + ' + generateTerm();
    } else if (rule === 'Expr - Term') {
      return generateExpr() + ' - ' + generateTerm();
    } else {
      return generateTerm();
    }
}
  
function generateTerm() {
    const rules = ['Term * Factor', 'Term / Factor', 'Factor'];
    const rule = rules[getRandomInt(0, rules.length - 1)];
  
    if (rule === 'Term * Factor') {
      return generateTerm() + ' * ' + generateFactor();
    } else if (rule === 'Term / Factor') {
      return generateTerm() + ' / ' + generateFactor();
    } else {
      return generateFactor();
    }
}
  
function generateFactor() {
    const rules = ['-Integer', '(Expr)', 'Integer', 'Integer.Integer'];
    const rule = rules[getRandomInt(0, rules.length - 1)];
  
    if (rule === '-Integer') {
      return '-' + generateInteger();
    } else if (rule === '(Expr)') {
      return '(' + generateExpr() + ')';
    } else if (rule === 'Integer') {
      return generateInteger();
    } else {
      return generateInteger() + '.' + generateInteger();
    }
}
  
function generateInteger() {
    const digits = '0123456789';
    let integer = '';
  
    // The first digit cannot be zero, to avoid numbers with leading zeroes
    integer += digits[getRandomInt(1, digits.length - 1)];
  
    // Randomly add more digits to the integer
    const numDigits = getRandomInt(1, 5);
    for (let i = 0; i < numDigits; i++) {
      integer += digits[getRandomInt(0, digits.length - 1)];
    }
  
    return integer;
}
  
function generateRandomExpression() {
    return generateExpr();
}
  
// Example usage:
const randomExpression = generateRandomExpression();
console.log(randomExpression);