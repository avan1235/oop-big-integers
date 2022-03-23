package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int len = number.length();
    if(number.charAt(0) != '-'){
    this.digits = new int[len];
      for(int i=0; i < len;i++){
        this.digits[i]=number.charAt(len-1-i);
      }
      isPositive = true;
    }
    else
    {
    this.digits = new int[len-1];
      for(int i=0; i<len-1;i++){
        this.digits[i]=number.charAt(len-1-i);
      }
    isPositive = true;
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
    throw new IllegalStateException("TODO task 2: implement addition");
  }

  public BigInt times(BigInt other) {
    throw new IllegalStateException("TODO task 3: implement multiplication");
  }

  @Override
  public String toString() {
    String output = "";
    for (int i=0; i<this.digits.length; i++){
      output = output + String.valueOf(digits[i]);
    }
    return output;
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

