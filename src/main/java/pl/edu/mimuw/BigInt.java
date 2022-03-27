package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;


  /* Creates an array of integers from a string representation of a decimal number. */
  public BigInt(String number) {
    int index = 0;

    if (number.charAt(index) == '-') {
      this.isPositive = false;
      index++;
    }
    else {
      this.isPositive = true;
    }

    int num_len = number.length() - index;

    this.digits = new int[num_len];

    for (int i = 0; i < num_len; i++, index++) {
      this.digits[i] = Character.getNumericValue(number.charAt(index));
    }
  }

  public BigInt(int[] digits, boolean isPositive) {
    this.digits = digits;
    this.isPositive = isPositive;
  }

  public BigInt(int number) {
    this(Integer.toString(number));
  }

  public BigInt neg() {
    final var digits = Arrays.copyOf(this.digits, this.digits.length);
    return new BigInt(digits, !this.isPositive);
  }

  /* Removes trailing zeros from an array. */
  private int[] makeTargetArray(int[] array) {
    int index = 0;

    while (array[index] == 0) {
      index++;
    }

    return Arrays.copyOfRange(array, index, array.length);
  }
  
  /* Compares two arrays of integers representing decimal numbers. */
  private int[] returnBigger(int[] arr1, int[] arr2) {
    if (arr1.length > arr2.length) { // If one array is longer it's by default bigger.
      return arr1;
    }
    if (arr1.length < arr2.length) { 
      return arr2;
    }
    
    int index = 0;
    while (arr1[index] == arr2[index]) { // If the arrays have the same length, compares the most significant digits.
      index++;
    }
    if (arr1[index] > arr2[index]) { 
      return arr1;
    }
    return arr2;
  }


  private int[] addSameSign(BigInt other) {
    int size = Math.max(this.digits.length, other.digits.length) + 1;
    int[] result = new int[size];

    int this_index = this.digits.length - 1, other_index = other.digits.length - 1;

    for (int i = size - 1; i > 0 && (other_index >= 0 || this_index >= 0); i--) {
      result[i] += this.digits[this_index] + other.digits[other_index];
      if (result[i] > 9) {
        result[i] -= 10;
        result[i - 1] += 1;
      }

      this_index--;
      other_index--;

      /* If one of the arrays ended, sums the remaining array. */
      if (this_index < 0) {
        i--;
        for (; i >= 0 && other_index >= 0; i--) {
          result[i] += other.digits[other_index];
          other_index--;
        }
      }
      else if (other_index < 0) {
        i--;
        for (; i >= 0 && this_index >= 0; i--) {
          result[i] += this.digits[this_index];
          this_index--;
        }
      }
    }

    return result;

  }

  /* Subtracts the array with lesser absolute value. */
  private int[] addOppositeSign(BigInt other) {
    int size = Math.max(this.digits.length, other.digits.length) + 1;
    int[] result = new int[size];

    int abs_bigger[] = returnBigger(this.digits, other.digits);
    int bigger_index = abs_bigger.length - 1;
    int abs_smaller[];

    if (returnBigger(this.digits, other.digits) == this.digits) {
      abs_smaller = other.digits;
    }
    else {
      abs_smaller = this.digits;
    }
    int smaller_index = abs_smaller.length - 1;

    /* Works analogically to addSameSign(). */
    for (int i = size - 1; i > 0 && (bigger_index >= 0 || smaller_index  >= 0); i--) {
      result[i] = abs_bigger[bigger_index] - abs_smaller[smaller_index];

      bigger_index--;
      smaller_index--;

      if (bigger_index < 0) {
        i--;
        for (; i > 0 && smaller_index >= 0; i--) {
          result[i] -= abs_smaller[smaller_index];
          smaller_index--;
        }
      }
      else if (smaller_index < 0) {
        i--;
        for (; i > 0 && bigger_index >= 0; i--) {
          result[i] += abs_bigger[bigger_index];
          bigger_index--;
        }
      }
    }

    /* Diverges from addSameSign() - fixes negative values. */
    for (int i = 0; i < result.length - 1; i++) {
      if (result[i + 1] < 0) {
        result[i] -= 1;
        result[i + 1] += 10;
        if (result[i] == -1) { // Backtracks if a new -1 is made by substracting from a zero.
          i -= 2;
        }
      }
    }
    return result;
  }

  public BigInt add(BigInt other) {
    int size = Math.max(this.digits.length, other.digits.length) + 1;
    int[] result = new int[size];

    if (this.isPositive == other.isPositive) {
      result = addSameSign(other);
      return new BigInt(makeTargetArray(result), this.isPositive);
    }

    result = addOppositeSign(other);

    if (returnBigger(this.digits, other.digits) == this.digits) {
      if (this.isPositive) {
        return new BigInt(makeTargetArray(result), true);
      }
    }
    return new BigInt(makeTargetArray(result), false);

  }

  public BigInt times(BigInt other) {
    int size = this.digits.length + other.digits.length - 1;
    int[] result = new int[size];

    for (int i = this.digits.length - 1; i >= 0; i--) {
      for (int j = other.digits.length - 1; j >= 0; j--) {
        result[i + j] += this.digits[i] * other.digits[j];

        if (i + j != 0) {
          result[i + j - 1] += result[i + j] / 10;
          result[i + j] %= 10; 
        }
      }
    }

    return new BigInt(makeTargetArray(result), this.isPositive == other.isPositive);
  }

  @Override
  public String toString() {
    StringBuilder h = new StringBuilder();

    if (!this.isPositive) {
      h.append("-");
    }
    for (int i = 0; i < this.digits.length; i++) {
      h.append(digits[i]);
    }

    return h.toString();
  }

  @Override
  public int hashCode() {
    final var arrayHashCode = Arrays.hashCode(digits);
    final var positiveHashCode = Boolean.hashCode(isPositive);

    return arrayHashCode * 17 + positiveHashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BigInt)) return false;
    final var other = (BigInt) obj;

    return Arrays.equals(this.digits, other.digits)
      && this.isPositive == other.isPositive;
  }
}
