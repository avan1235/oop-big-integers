package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int numberLength = number.length();

    final var charArr = number.toCharArray();

    int offset ;
    if (charArr[0] == '-') {
      offset = 1;
      this.isPositive = false;
      this.digits = new int[numberLength - 1];
    }
    else {
      offset = 0;
      this.isPositive = true;
      this.digits = new int[numberLength];
    }

    for(int i = offset; i < numberLength; i++) {
      this.digits[numberLength - i - 1] = charArr[i] - '0';
    }

  }

  public BigInt(int[] digits, boolean isPositive) {
    int toRemove = 0;
    for(int i = digits.length - 1; i >= 0; i--) {
      if (digits[i] == 0) {
        toRemove++;
      }
      else {
        break;
      }
    }

    this.digits = Arrays.copyOfRange(digits, 0, digits.length - toRemove);
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
    final var sum = new int[Math.max(this.digits.length, other.digits.length) + 1];
    int negateThis = 1;
    int negateOther = 1;

    boolean positivity = true;

    if (!other.isPositive) {
      negateOther = -1;
    }
    if (!this.isPositive) {
      negateThis = -1;
    }

    for(int i = 0; i < this.digits.length; i++) {
      sum[i] = this.digits[i] * negateThis;
    }
    for(int i = 0; i < other.digits.length; i++) {
      sum[i] += other.digits[i] * negateOther;
    }

    for(int i = sum.length - 1; i >= 0; i--) {
      if (sum[i] < 0) {
        positivity = false;
        break;
      }
      if (sum[i] > 0) {
        break;
      }
    }

    if (!positivity) {
      for(int i = 0; i < sum.length; i++) {
        sum[i] *= -1;
      }
    }

    for(int i = 0; i < sum.length - 1; i++) {
      if (sum[i] > 10) {
        sum[i + 1]++;
        sum[i] -= 10;
      }
      else if (sum[i] < 0) {
        sum[i + 1]--;
        sum[i] += 10;
      }
    }

    return new BigInt(sum, positivity);
  }

  public BigInt times(BigInt other) {
    final var product = new int[this.digits.length + other.digits.length + 1];

    boolean positivity = this.isPositive == other.isPositive;

    for(int i = 0; i < this.digits.length; i++) {
      for(int j = 0; j < other.digits.length; j++) {
        product[i + j] += this.digits[i] * other.digits[j];
      }
    }

    for (int i = 0; i < product.length - 1; i++) {
      product[i + 1] += product[i] / 10;
      product[i] %= 10;
    }

    return new BigInt(product, positivity);
  }

  @Override
  public String toString() {
    String s;

    if(!this.isPositive) {
      s = "-";
    }
    else {
      s = "";
    }

    for(int i = this.digits.length - 1; i >= 0; i--) {
      s = s + this.digits[i];
    }

    return s;
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
