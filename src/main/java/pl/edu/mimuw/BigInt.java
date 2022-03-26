package pl.edu.mimuw;

import java.util.Arrays;
import java.math.BigInteger;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    if(number.charAt(0) == '-'){
      this.digits = new int[number.length() - 1];
      this.isPositive = false;

      for(int i = 1; i<number.length(); i++){
        this.digits[i-1] = number.charAt(i) - '0';
      }
    }
    else {
      this.digits = new int[number.length()];
      this.isPositive = true;

      for(int i = 0; i<number.length(); i++){
        this.digits[i] = number.charAt(i) - '0';
      }
    }
    //throw new IllegalStateException("TODO task 1: parse number from valid String");
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
    BigInteger sum;
    BigInteger a = new BigInteger(this.toString());
    BigInteger b = new BigInteger(other.toString());
    sum = a.add(b);
    return new BigInt(sum.toString());
  }

  public BigInt times(BigInt other) {
    final int maxL = Math.max(this.digits.length, other.digits.length);
    int[][] arr = new int[maxL*2][maxL+1];
    int h = 0;

    for(int i=this.digits.length-1; i>=0; i--) {
      for(int j=other.digits.length-1; j>=0; j--) {
        int v = this.digits[i] * other.digits[j] + h;
        if(v > 9){
          h = (v - v % 10) / 10;
          v %= 10;
        }
        else {
          h = 0;
        }
        arr[maxL*2-1-(other.digits.length-1-j)][this.digits.length - i - 1] = v;

        if(j == 0 && h != 0) {
          arr[maxL*2-1-(other.digits.length-1-1)][this.digits.length - i - 1] = h;
          h = 0;
        }
      }
    }

    int x = 0;

    for(int i=2*maxL-1; i>=0; i--) {
      int v = 0;
      for(int j=0; j<maxL; j++) {
        v += arr[i][j];
      }

      v += h;

      if(x == 0 && v == 0) x = i;

      if(v > 9){
        h = (v - v % 10) / 10;
        v %= 10;
      }
      else {
        h = 0;
      }

      arr[i][maxL] = v;
    }

    final int[] res = new int[maxL*2-1-x];

    for(int i=x+1; i<2*maxL; i++){
      res[i-x-1] = arr[i][maxL];
    }

    return new BigInt(res, this.isPositive == other.isPositive);
  }

  @Override
  public String toString() {
    final var l = this.digits.length;
    char d[] = new char[l];

    for(int i=0; i<l; i++){
      d[i] = (char)(this.digits[i] + '0');
    }

    return this.isPositive ? new String(d) : "-" + new String(d);
    //throw new IllegalStateException("TODO task 4: implement creating representation");
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
