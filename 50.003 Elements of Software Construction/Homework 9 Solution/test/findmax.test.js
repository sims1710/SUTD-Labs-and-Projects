const findmax = require('../src/findmax.js');

describe('testing findmax', () => {
    // TODO: a test that results in failure (do not compute maximum) it should fail
    test('a test that fails', () => {
        const list = [1, 2, 3, 4, 5, 6];
        const result = findmax(list);
        expect(result).toBe(1);
    })
    // TODO: a test that results in error, it should throw an error that the test won't catch
    test('a test that throws error', () => {
        expect(() => {
            findmax(); // Provide a valid list as an argument
        }).toThrow();
    })
    // TODO: a test that results in pass
    test('a test that passes', () => {
        const list = [1, 2, 3, 4, 5, 6];
        const result = findmax(list);
        expect(result).toBe(6);
    })

    test('empty array should return undefined', () => {
        const list = [];
        const result = findmax(list);
        expect(result).toBeUndefined();
      });
    
      // Test for an array with null values
      test('array with null values should ignore them and return the maximum', () => {
        const list = [null, 3, null, 5, null, 2];
        const result = findmax(list);
        expect(result).toBe(5);
      });
    
      // Test for an array with NaN values
      test('array with NaN values should ignore them and return the maximum', () => {
        const list = [NaN, 7, NaN, 4, NaN, 9];
        const result = findmax(list);
        expect(result).toBe(9);
      });
    
      // Test for an array with a single ordinal value
      test('array with a single value should return that value as the maximum', () => {
        const list = [9];
        const result = findmax(list);
        expect(result).toBe(9);
      });
    
      // Test for an array with more than one ordinal value and the max value at index 0
      test('array with max value at index 0 should return the maximum', () => {
        const list = [10, 7, 3, 2, 1];
        const result = findmax(list);
        expect(result).toBe(10);
      });
    
      // Test for an array with more than one ordinal value and the max value at index N-1
      test('array with max value at index N-1 should return the maximum', () => {
        const list = [1, 3, 2, 7, 10];
        const result = findmax(list);
        expect(result).toBe(10);
      });
    
      // Test for an array with more than one ordinal value and the max value not at index 0 nor index N-1
      test('array with max value not at index 0 or index N-1 should return the maximum', () => {
        const list = [5, 2, 7, 4, 9];
        const result = findmax(list);
        expect(result).toBe(9);
      });
      
});