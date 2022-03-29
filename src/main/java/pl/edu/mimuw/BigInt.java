package pl.edu.mimuw;

import java.util.Arrays;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int b;
    if(number.substring(0,1) == "-")
      b=1;
    else
      b=0;
    String digitchars=number.substring(b,number.length());
    int digitints[] = new int[digitchars.length()], p;
    for (int i=0; i<digitchars.length(); i++){
      p=digitchars.charAt(i);
      digitints[i]=p-'0';
    }
    this.digits=digitints;
    this.isPositive=(b==0);
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
    int a, b, pa, pb, ta[], tb[];
    boolean sign;
      if (this.digits.length>other.digits.length) {
        sign=this.isPositive;
        a=this.digits.length;
        ta=this.digits;
        if(this.isPositive)
          pa=1;
        else
          pa=-1;
        b=other.digits.length;
        tb=other.digits;
        if(other.isPositive)
          pb=1;
        else
          pb=-1;
      } else {
        if (this.digits.length<other.digits.length){
          sign=other.isPositive;
        } else {
          int i=0;
          while (this.digits[i]==other.digits[i]){
            i++;
          }
          if(this.digits[i]>other.digits[i])
            sign=this.isPositive;
          else
            sign=other.isPositive;
        }
        b=this.digits.length;
        tb=this.digits;
        if(this.isPositive)
          pb=1;
        else
          pb=-1;
        a=other.digits.length;
        ta=other.digits;
        if(other.isPositive)
          pa=1;
        else
          pa=-1;
      }
      int r = 0, da, db, helper;
      int[] tab = new int[a + 1];
      for (int i = 1; i <= b; i++) {
        da = pa * ta[a - i];
        db = pb * tb[b - i];
        helper=da+db+r;
        if (helper<0 && sign) {
          tab[a + 1 + i] = abs((-helper)%10);
          r=helper/10;
        } else {
          tab[a+1+i] =helper%10;
          r=helper/10;
        }
      }
      for (int i=b+1; i<a; i++){
        tab[a-i]=abs((pa*ta[a-i]+r))%10;
        r=(pa*ta[a-i]+r)/10;
      }
      if (r!=0) {
        tab[0] = abs(r);
        return new BigInt(tab, (r>0));
      }
      else {
        return new BigInt(tab, sign);
      }
    //throw new IllegalStateException("TODO task 2: implement addition");
  }

  public BigInt multi(int m) {
    String s="";
    int r=0;
    for (int i=1; i<this.digits.length; i++){
      s+='0'+(m*this.digits[this.digits.length-i]+r)%10;
      r=(m*this.digits[this.digits.length-i]+r)/10;
    }
    if (!this.isPositive)
      s+='-';
    StringBuilder reverseString = new StringBuilder(s);
    reverseString.reverse();
    return new BigInt(reverseString.toString());
  }
  public BigInt expo(int m) {
    for (int i=0; i<m; i++){
      this.digits[this.digits.length+m]=0;
    }
    return this;
  }
  public BigInt times(BigInt other) {
    boolean sign = (this.isPositive==other.isPositive);
    BigInt acc = new BigInt(new int[0], true);
    for (int i=1; i<=this.digits.length; i++){
      acc=acc.add((other.multi(this.digits[this.digits.length-i])).expo(i-1));
    }
    return new BigInt(acc.digits, sign);
    //throw new IllegalStateException("TODO task 3: implement multiplication");
  }

  @Override
  public String toString() {
    int l=this.digits.length;
    String s="";
    if (!this.isPositive)
      s+="-";
    for (int i=0; i<l; i++)
      s+='0'+this.digits[i];
    return s;
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
