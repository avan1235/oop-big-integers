package pl.edu.mimuw;

import java.util.Arrays;

import static java.lang.Math.max;


public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    boolean isPositive = true;
    int first = 0;
      if(number.charAt(0) == '-')
      {
        first++;
        isPositive = false;
      }
      this.digits = new int[number.length()-first];
      this.isPositive = isPositive;
      for(int i = first; i<number.length(); i++) {
        this.digits[i] = number.charAt(i) - '0';
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
    int [] c = new int [max( this.digits.length, other.digits.length)];

  }

  public BigInt times(BigInt other) {
    throw new IllegalStateException("TODO task 3: implement multiplication");
  }

  @Override
  public String toString() {
    throw new IllegalStateException("TODO task 4: implement creating representation");
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
