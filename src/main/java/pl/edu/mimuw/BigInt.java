package pl.edu.mimuw;

import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.Collections.swap;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    if (number.charAt(0) == '-') {
      this.isPositive = false;
    }
    else this.isPositive = true;
    this.digits = new int[number.length() - (this.isPositive ? 0 : 1)];
    for (int i = 0;i<this.digits.length;i++) {
      this.digits[i] = number.charAt(i + (this.isPositive ? 0 : 1));
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
    if (other.digits.length > this.digits.length)
      return other.add(this);
    if (!this.isPositive)
      return this.neg().add(other.neg()).neg();
    if (!other.isPositive)
      for (int i = 0;i<other.digits.length;i++)
        other.digits[i] = -other.digits[i];

    for (int i = 0;i<other.digits.length-1;i++) {
      this.digits[this.digits.length - i - 1] += other.digits[other.digits.length - i - 1];
      if (this.digits[this.digits.length - i - 1] > 9)
      {
        this.digits[this.digits.length - i - 1] -= 10;
        this.digits[this.digits.length - i - 2]++;
      }
    }
    this.digits[0] += other.digits[0];
    if (this.digits[0] > 9)
    {
      this.digits[0] -= 10;
      int[] ret = new int[this.digits.length + 1];
      ret[0] = 1;
      System.arraycopy(this.digits, 0, ret, 1, this.digits.length);
      return new BigInt(ret, true);
    }

  }

  public BigInt times(BigInt other) {
    throw new IllegalStateException("TODO task 3: implement multiplication");
  }

  @Override
  public String toString() {
    String s = "";
    if (!this.isPositive) s = "-";
    for (int digit : this.digits)
      s = s.concat(String.valueOf(digit));
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
