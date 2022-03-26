package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int size = number.length();
    if(number.charAt(0) == '-') {
      this.isPositive = false;
      size--;
    }
    else {
      this.isPositive = true;
    }
    this.digits = new int[size];
    for(int i = 0; i < number.length(); i++) {
      if(this.isPositive)
        this.digits[size - i - 1] = Character.getNumericValue(number.charAt(i));
      else {
        if(i != 0) {
          this.digits[size - i] = Character.getNumericValue(number.charAt(i));
        }
      }
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
    int maxLength = Math.max(other.digits.length, this.digits.length);
    int minLength = Math.min(other.digits.length, this.digits.length);
    var sum = new int[maxLength + 1];
    int rem = 0;
    if(other.isPositive == this.isPositive) {
      for(int i = 0; i < minLength; i++) {
        sum[i] = other.digits[i] + this.digits[i] + rem;
        rem = sum[i] / 10;
        sum[i] %= 10;
      }
      if(other.digits.length > this.digits.length) {
        for(int i = minLength; i < maxLength; i++) {
          sum[i] = other.digits[i] + rem;
          rem = sum[i] / 10;
          sum[i] %= 10;
        }
      }
      else {
        for(int i = minLength; i < maxLength; i++) {
          sum[i] = this.digits[i] + rem;
          rem = sum[i] / 10;
          sum[i] %= 10;
        }
      }
      sum[sum.length-1] = rem;
      return new BigInt(sum, this.isPositive);
    }
    else {
      boolean positive = this.isPositive;
      for(int i = 0; i < minLength; i++) {
        sum[i] = this.digits[i] - other.digits[i] + rem;
        if(sum[i] < 0) {
          rem = -1;
          sum[i] += 10;
        }
        else {
          rem = 0;
        }
      }
      if(other.digits.length > this.digits.length) {
        for(int i = minLength; i < maxLength; i++) {
          sum[i] = -other.digits[i] + rem;
          if(sum[i] < 0) {
            rem = -1;
            sum[i] += 10;
          }
          else {
            rem = 0;
          }
        }
      }
      else {
        for(int i = minLength; i < maxLength; i++) {
          sum[i] = this.digits[i] + rem;
          if(sum[i] < 0) {
            rem = -1;
            sum[i] += 10;
          }
          else {
            rem = 0;
          }
        }
      }
      if(rem != 0) {
        positive = !positive;
        int it = maxLength - 1;
        while(it > 0) {
          sum[it] = 9 - sum[it];
          it--;
        }
        sum[0] = 10 - sum[0];
      }
      return new BigInt(sum, positive);
    }
  }

  public BigInt times(BigInt other) {
    var prod = new BigInt("0");
    for(int i = 0; i < other.digits.length; i++) {
      int digit = other.digits[i];
      var digitProduct = new int[this.digits.length + 1];
      int rem  = 0;
      for(int j = 0; j < this.digits.length; j++) {
        digitProduct[j] = this.digits[j] * digit + rem;
        rem = digitProduct[j] / 10;
        digitProduct[j] %= 10;
      }
      digitProduct[this.digits.length] = rem;
      String prodWithZeros = (new BigInt(digitProduct, true)).toString();
      for(int j = 0; j < i; j++) {
        prodWithZeros += "0";
      }
      prod = prod.add(new BigInt(prodWithZeros));
    }
    if(this.isPositive ^ other.isPositive) {
      return prod.neg();
    }
    return prod;
  }

  @Override
  public String toString() {
    String s = "";
    if(!this.isPositive) s = "-";
    boolean leadingZero = true;
    for(int i = this.digits.length - 1; i >= 0; i--) {
      if(this.digits[i] != 0) leadingZero = false;
      if(!leadingZero)  s += Integer.toString(this.digits[i]);
    } 
    if((s == "") || (s == "-")) s = "0";
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
