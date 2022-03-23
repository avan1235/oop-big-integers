package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    boolean isPositive = number.charAt(0) != '-';

    int firstDigit = isPositive ? 0 : 1;
    int[] digits = new int[number.length() - firstDigit];

    for (int i = firstDigit; i < number.length(); i++) {
      digits[number.length() - i - 1] = number.charAt(i) - '0';
    }
    this.digits = removeLeadingZeros(digits);

    if (this.digits[this.digits.length - 1] == 0) // handle "-0" thing
      isPositive = true;
    this.isPositive = isPositive;
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

  private static int[] removeLeadingZeros(int[] digits) {
    int zeros = 0;
    while (zeros < digits.length - 1 && digits[digits.length - zeros - 1] == 0) zeros++;
    return Arrays.copyOf(digits, digits.length - zeros);
  }

  public BigInt add(BigInt other) {
    int[] digits = new int[Math.max(this.digits.length, other.digits.length) + 1];
    boolean isPositive = true;

    for (int i = 0; i < digits.length - 1; i++) {
      if (i < this.digits.length)
        if (this.isPositive)
          digits[i] += this.digits[i];
        else
          digits[i] -= this.digits[i];

      if (i < other.digits.length)
        if (other.isPositive)
          digits[i] += other.digits[i];
        else
          digits[i] -= other.digits[i];

      if (digits[i] > 10) {
        digits[i + 1] += 1;
        digits[i] %= 10;
      } else if (digits[i] < 0) {
        digits[i + 1]--;
        digits[i] += 10;
      }
    }

    if (digits[digits.length - 1] < 0) {
      isPositive = false;
      for (int i = 0; i < digits.length - 1; i++) {
        digits[i] *= -1;
        if (digits[i] < 0) {
          digits[i] += 10;
          digits[i + 1] += 1; // it will be multiplied by -1, so +1 is like -1
        }
      }
      digits[digits.length - 1] *= -1;
    }

    return new BigInt(removeLeadingZeros(digits), isPositive);
  }

  public BigInt times(BigInt other) {
    int[] digits = new int[this.digits.length + other.digits.length];
    boolean isPositive = other.isPositive == this.isPositive;

    for (int i = 0; i < other.digits.length; i++) {
      for (int j = 0; j < this.digits.length; j++) {
        digits[i + j] += this.digits[j] * other.digits[i];
      }
    }

    for (int i = 0; i < digits.length - 1; i++) {
      digits[i + 1] += digits[i] / 10;
      digits[i] %= 10;
    }

    return new BigInt(removeLeadingZeros(digits), isPositive);
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder(this.isPositive ? "" : "-");
    for (int i = this.digits.length - 1; i >= 0; i--)
      res.append(digits[i]);
    return res.toString();
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
