package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    isPositive = number.charAt(0) != '-';

    digits = new int[number.length() - (isPositive ? 0 : 1)];

    for (int i = isPositive ? 0 : 1; i < number.length(); i++) {
      digits[number.length() - i - 1] = number.charAt(i) - '0';
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
    int[] digits = new int[Math.max(this.digits.length, other.digits.length) + 1];
    boolean isPositive = true;

    for (int i = 0; i < digits.length - 1; i++){
      if (i < this.digits.length)
        if(this.isPositive)
          digits[i] += this.digits[i];
        else
          digits[i] -= this.digits[i];

      if (i < other.digits.length)
        if(other.isPositive)
          digits[i] += other.digits[i];
        else
          digits[i] -= other.digits[i];

      if(digits[i] > 10) {
        digits[i+1] += 1;
        digits[i] %= 10;
      } else if (digits[i] < 0) {
        digits[i+1]--;
        digits[i] += 10;
      }
    }

    if (digits[digits.length - 1] < 0) {
      isPositive = false;
      for(int i = 0; i < digits.length - 1; i++) {
        digits[i] *= -1;
        if(digits[i] < 0){
          digits[i] += 10;
          digits[i+1] += 1; // it will be multiplied by -1, so +1 is like -1
        }
      }
      digits[digits.length - 1] *= -1;
    }

    int zeros = 0;
    while(zeros < digits.length - 1 && digits[digits.length - zeros - 1] == 0) zeros++;
    int[] resDigits = Arrays.copyOf(digits, digits.length - zeros);

    return new BigInt(resDigits, isPositive);
  }

  public BigInt times(BigInt other) {
    throw new IllegalStateException("TODO task 3: implement multiplication");
  }

  @Override
  public String toString() {
    String res = this.isPositive ? "" : "-";
    for(int i = this.digits.length - 1; i >= 0; i--)
      res += digits[i];
    return res;
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
