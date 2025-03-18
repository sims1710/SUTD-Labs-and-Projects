const gcd = require('../src/gcd.js');

describe('gcd function', () => {
  // Test Cases for covering all paths
  it('should return null when x is negative and y is positive', () => {
    expect(gcd(-5, 10)).toBeNull(); // Path 1: x < 1, y >= 1 → r = null
  });

  it('should return the correct gcd when x > y', () => {
    expect(gcd(8, 5)).toBe(1); // Path 2: x >= 1, y >= 1 → x > y → t = x - y
  });

  it('should return the correct gcd when x < y', () => {
    expect(gcd(5, 8)).toBe(1); // Path 3: x >= 1, y >= 1 → x < y → t = y - x
  });

  it('should return x when x and y are equal', () => {
    expect(gcd(4, 4)).toBe(4); // Path 4: x >= 1, y >= 1 → x == y → r = x
  });

  // Extra Test Cases for MCDC coverage
  it('should return null when x is negative', () => {
    expect(gcd(-1, 5)).toBeNull(); // Test Case 1: x < 1, y >= 1 → r = null
  });

  it('should return null when y is negative', () => {
    expect(gcd(5, -1)).toBeNull(); // Test Case 2: x >= 1, y < 1 → r = null
  });

  it('should return null when both x and y are negative', () => {
    expect(gcd(-1, -1)).toBeNull(); // Test Case 3: x < 1, y < 1 → r = null
  });
});