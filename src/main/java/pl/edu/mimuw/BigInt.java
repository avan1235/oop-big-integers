package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int iter;
    if (number.charAt(0) == '-') {
      iter = 1;
      this.isPositive = false;
    } else {
      iter = 0;
      this.isPositive = true;
    }
    int[] arr = new int[number.length() - iter];
    for (int i = 0; i < arr.length; i++)
      arr[i] = number.charAt(number.length() - 1 - i) - '0';
    this.digits = arr;
  }

  //NOTE I modified this to make other functions less painful
  public BigInt(int[] digits, boolean isPositive) {
    int firstNonZero = digits.length;
    while (firstNonZero > 0 && digits[firstNonZero - 1] == 0)
      firstNonZero--;
    if (firstNonZero == 0) {
      this.digits = new int[1];
      digits[0] = 0;
      this.isPositive = true;
    } else {
      this.digits = new int[firstNonZero];
      this.isPositive = isPositive;
      System.arraycopy(digits, 0, this.digits, 0, this.digits.length);
    }
  }

  public BigInt(int number) {
    this(Integer.toString(number));
  }

  private int compAbs(BigInt other) {
    if (this.digits.length > other.digits.length)
      return 1;
    if (this.digits.length < other.digits.length)
      return -1;

    for (int i = this.digits.length - 1; i >= 0; i--) {
      if (this.digits[i] > other.digits[i])
        return 1;
      if (this.digits[i] < other.digits[i])
        return -1;
    }
    return 0;
  }

  public BigInt neg() {
    final var digits = Arrays.copyOf(this.digits, this.digits.length);
    return new BigInt(digits, !this.isPositive);
  }

  //this function has to modes. It either adds two integers of the same sign or subtracts smaller one from the greater one.
  public BigInt add(BigInt other) {
    if (this.isPositive == other.isPositive) {
      int[] output = new int[java.lang.Math.max(this.digits.length, other.digits.length) + 1];
      int carry = 0;
      for (int i = 0; i < output.length; i++) {
        output[i] += carry;
        if (i < this.digits.length)
          output[i] += this.digits[i];
        if (i < other.digits.length)
          output[i] += other.digits[i];
        if (10 <= output[i]) {
          carry = 1;
          output[i] -= 10;
        } else {
          carry = 0;
        }
      }
      return new BigInt(output, this.isPositive);
    } else {
      int compInts = compAbs(other);
      if (compInts == -1)
        return other.add(this);
      if (compInts == 0)
        return new BigInt("0");
      int subOne = 0;
      int[] output = new int[this.digits.length];
      for (int i = 0; i < this.digits.length; i++) {
        output[i] = this.digits[i] - subOne;
        if(i<other.digits.length)
          output[i] -= other.digits[i];
        if(output[i] < 0)
        {
          output[i] += 10;
          subOne = 1;
        }
        else
          subOne = 0;
      }
      return new BigInt(output, this.isPositive);
    }
  }

  public BigInt times(BigInt other) {
    int carry;
    int[] output = new int[this.digits.length + other.digits.length];
    for (int i = 0; i < this.digits.length; i++) {
      carry = 0;
      for (int j = 0; j < other.digits.length; j++) {
        output[i + j] += this.digits[i] * other.digits[j] + carry;
        carry = output[i + j] / 10;
        output[i + j] = output[i + j] % 10;
      }
      output[i + other.digits.length] += carry;
    }

    return new BigInt(output, this.isPositive == other.isPositive);
  }

  @Override
  public String toString() {
    int isThereMinus;
    if (this.isPositive)
      isThereMinus = 0;
    else
      isThereMinus = 1;
    char[] charArr = new char[this.digits.length + isThereMinus];
    if (!this.isPositive)
      charArr[0] = '-';
    for (int i = 0; i < this.digits.length; i++)
      charArr[charArr.length - 1 - i] = (char) ('0' + this.digits[i]);
    return new String(charArr);

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
