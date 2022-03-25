package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    this.isPositive = !(number.charAt(0) == '-');
    final var signShift = isPositive ? 0 : 1;
    var leadingZeros = 0;

    for (int i = signShift; i < number.length(); i++) {
      if (number.charAt(i) != '0') {
        leadingZeros = i - signShift;
        break;
      }
    }
    this.digits = new int[number.length() - signShift - leadingZeros];

    for (int i = number.length() - 1; i >= signShift + leadingZeros; i--) {
      this.digits[number.length() - i - 1] = Character.getNumericValue(number.charAt(i));
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

  public BigInt add(BigInt other) {
    if (this.isPositive == other.isPositive) {
      return new BigInt(addSameSign(other), this.isPositive);
    } else {
      BigInt smaller, bigger;

      if (absBiggerThan(other)) {
        bigger = this;
        smaller = other;
      } else {
        bigger = other;
        smaller = this;
      }

      return new BigInt(bigger.addDifferentSign(smaller), bigger.isPositive);
    }
  }

  public BigInt times(BigInt other) {
    var resultDigits = new int[this.digits.length + other.digits.length];

    for (int i = 0; i < this.digits.length; i++) {
      for (int j = 0; j < other.digits.length; j++) {
        resultDigits[i + j] += this.digits[i] * other.digits[j];

        if (resultDigits[i + j] >= 10) {
          resultDigits[i + j + 1] += resultDigits[i + j] / 10;
          resultDigits[i + j] %= 10;
        }
      }
    }

    // fix possible leading zero
    if (resultDigits[resultDigits.length - 1] == 0) {
      resultDigits = Arrays.copyOf(resultDigits, resultDigits.length - 1);
    }

    return new BigInt(resultDigits, this.isPositive == other.isPositive);
  }

  @Override
  public String toString() {
    final var sb = new StringBuilder();

    if (!this.isPositive) {
      sb.append('-');
    }

    for (int i = this.digits.length - 1; i >= 0; i--) {
      sb.append(Character.forDigit(this.digits[i], 10));
    }

    return sb.toString();
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

  private int[] addSameSign(BigInt other) {
    final var len = Math.max(this.digits.length, other.digits.length);
    final var resultDigits = new int[len + 1];

    for (int i = 0; i < len; i++) {
      if (i < this.digits.length) {
        resultDigits[i] += this.digits[i];
      }

      if (i < other.digits.length) {
        resultDigits[i] += other.digits[i];
      }

      if (resultDigits[i] >= 10) {
        resultDigits[i + 1] += resultDigits[i] / 10;
        resultDigits[i] %= 10;
      }
    }

    int newLen = len;
    for (int i = len; i >= 0; i--) {
      if (resultDigits[i] != 0) {
        newLen = i + 1;
        break;
      }
    }

    return Arrays.copyOf(resultDigits, newLen);
  }

  private boolean absBiggerThan(BigInt other) {
    if (this.digits.length > other.digits.length) {
      return true;
    }
    if (this.digits.length < other.digits.length) {
      return false;
    }

    for (int i = this.digits.length - 1; i >= 0; i--) {
      if (this.digits[i] > other.digits[i]) {
        return true;
      }
      if (this.digits[i] < other.digits[i]) {
        return false;
      }
    }

    return true;
  }

  private int[] addDifferentSign(BigInt other) {
    final var resultDigits = new int[this.digits.length];

    for (int i = 0; i < this.digits.length; i++) {
      if (i >= other.digits.length) {
        resultDigits[i] = this.digits[i];
        continue;
      }

      if (this.digits[i] < other.digits[i]) {
        this.digits[i] += 10;
        this.digits[i + 1]--;
      }

      resultDigits[i] = this.digits[i] - other.digits[i];
    }

    int newLen = resultDigits.length + 1;
    for (int i = resultDigits.length - 1; i >= 0; i--) {
      if (resultDigits[i] != 0) {
        newLen = i + 1;
        break;
      }
    }

    return Arrays.copyOf(resultDigits, newLen);
  }
}
