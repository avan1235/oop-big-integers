package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private int[] digits;

  private boolean isPositive;

  private static int max(int x, int y) {
    return (x < y) ? y : x;
  }

  public BigInt(String number) {
    int i = 0;
    if (0 < number.length() && number.charAt(0) == '-') {
      isPositive = false;
      i = 1;
    } else {
      isPositive = true;
    }
    digits = new int[number.length()-i];
    for (; i<number.length(); i++)
      digits[number.length()-i-1] = Character.getNumericValue(number.charAt(i));
  }
  
  public BigInt(int[] digits, boolean isPositive) {
    this.digits = digits;
    this.isPositive = isPositive;
  }

  public BigInt(int number) {
    this(Integer.toString(number));
  }
  
  public BigInt neg() {
    final int[] digits = Arrays.copyOf(this.digits, this.digits.length); 
    return new BigInt(digits, !this.isPositive);
  }

  private int auxCmp(int[] num1, int[] num2) {
    int i = num1.length - 1;
    if (num1.length < num2.length)
      return -1;
    if (num2.length < num1.length)
      return 1;
    while (i >= 0 && num1[i] == num2[i])
      i--;
    if (i < 0)
      return 0;
    if (num1[i] < num2[i])
      return -1;
    return 1;
  }

  private int[] auxAdd(int[] num1, int[] num2) {
    int[] num3 = new int[1+max(num1.length, num2.length)];
    int i, s, x = 0;
    for (i=0; i<num3.length; i++) {
      s = x;
      if (i < num1.length)
        s += num1[i];
      if (i < num2.length)
        s += num2[i];
      if (s > 9) {
        num3[i] = s - 10;
        x = 1;
      } else {
        num3[i] = s;
        x = 0;
      }
    }
    if (x == 1)
       num3[num3.length-1] = 1;
    if (num3[num3.length-1] == 0 && num3.length > 1)
       num3 = Arrays.copyOf(num3, num3.length - 1);
    return num3;
  }

  private int[] auxSub(int[] num1, int[] num2) {
    int[] num3 = new int[num1.length];
    int i, s, x = 0;
    for (i=0; i<num1.length; i++) {
      s = num1[i] - x;
      if (i < num2.length)
        s -= num2[i];
      if (s >= 0) {
        num3[i] = s;
        x = 0;
      } else {
        num3[i] = s + 10;
        x = 1;
      }
    }
    i = num3.length - 1;
    while (i > 0 && num3[i] == 0)
      i--;
    num3 = Arrays.copyOf(num3, i+1);
    return num3;
  }

  private int[] cut(int[] num, int l, int r) {
    int[] frag;
    if (l >= num.length || l > r) {
      frag = new int[1];
      frag[0] = 0;
    } else {
      if (r >= num.length)
        r = num.length - 1;
      while (r > l && num[r] == 0)
        r--;
      frag = new int[r-l+1];
      for (int i=l; i<=r; i++)
        frag[i-l] = num[i];
    }
    return frag;
  }

  private int[] pow10(int[] num, int k) {
    if (num.length == 1 && num[0] == 0)
      return num;
    int[] res = new int[num.length+k];
    for (int i=0; i<k; i++)
      res[i] = 0;
    for (int i=0; i<num.length; i++)
      res[i+k] = num[i];
    return res;
  }

  private int[] karatsuba(int[] num1, int[] num2) {
   int x;
   int[] res, z0, z1, z2, left1, left2, right1, right2;
   if (num1.length == 1 && num2.length == 1) {
     x = num1[0] * num2[0];
     if (x > 9) {
       res = new int[2];
       res[0] = x % 10;
       res[1] = x / 10;
     } else {
       res = new int[1];
       res[0] = x;
     }
   } else {
     x = max(num1.length, num2.length) - 1;
     left1 = cut(num1, 0, x/2);
     right1 = cut(num1, x/2+1, x);
     left2 = cut(num2, 0, x/2);
     right2 = cut(num2, x/2+1, x);
     z0 = karatsuba(left1, left2);
     z2 = karatsuba(right1, right2);
     z1 = auxSub(auxSub(karatsuba(auxAdd(left1, right1), auxAdd(left2, right2)), z0), z2);
     res = auxAdd(auxAdd(pow10(z2, 2*(x/2+1)), pow10(z1, x/2+1)), z0);
   }
   return res;
  }

  public BigInt add(BigInt other) {
    BigInt res = new BigInt("0");
    if (isPositive && other.isPositive) {
      res.digits = auxAdd(digits, other.digits);
      res.isPositive = true;
    } else if (isPositive && !other.isPositive) {
       if (auxCmp(digits, other.digits) < 0) {
         res.digits = auxSub(other.digits, digits);
         res.isPositive = false;
       } else {
         res.digits = auxSub(digits, other.digits);
         res.isPositive = true;
       }
    } else if (!isPositive && other.isPositive) {
       if (auxCmp(digits, other.digits) <= 0) {
         res.digits = auxSub(other.digits, digits);
         res.isPositive = true;
       } else {
         res.digits = auxSub(digits, other.digits);
         res.isPositive = false;
       }
    } else {
       res.digits = auxAdd(digits, other.digits);
       res.isPositive = false;
    }
    return res;
  }

  public BigInt times(BigInt other) {
    BigInt res = new BigInt("0");
    if ((isPositive && other.isPositive)
    || (!isPositive && !other.isPositive)
    || (digits.length == 1 && digits[0] == 0)
    || (other.digits.length == 1 && other.digits[0] == 0))
      res.isPositive = true;
    else
      res.isPositive = false;
    res.digits = karatsuba(digits, other.digits);
    return res;
  }

  @Override
  public String toString() {
    String number = Arrays.toString(digits).replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
    if (!isPositive)
       number = number + "-";
    return new StringBuilder(number).reverse().toString();
  }
  
  @Override
  public int hashCode() {
    final int arrayHashCode = Arrays.hashCode(digits);
    final int positiveHashCode = Boolean.hashCode(isPositive);
    return arrayHashCode * 17 + positiveHashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BigInt)) return false;
    final BigInt other = (BigInt) obj;
    return Arrays.equals(this.digits, other.digits)
      && this.isPositive == other.isPositive;
  }
}
