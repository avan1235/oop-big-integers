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
        this.digits[i-first] = number.charAt(i) - '0';
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

    int [] c = new int [max( this.digits.length, other.digits.length) + 1];
    int size = max( this.digits.length, other.digits.length) + 1;
    boolean same_sign = false;
    BigInt result = other;
      if(this.isPositive == other.isPositive){
          same_sign = true;
      }
      if(same_sign == true ) //same sign, no matter if add or substract
      {
          int nr = 0;
          //rewriting everything to one array
          for(int i = this.digits.length-1; i>=0; i--)
          {
           c[size-1-nr] += this.digits[i];
            nr++;
          }
          nr = 0;
          int dalej = 0;
          //addition of other arr, remembering about numbers >=10
          for(int i = other.digits.length-1; i>=0; i--)
          {
            c[size-1-nr] += other.digits[i]+dalej;
            if(c[size-1-nr] >= 10)
            {
              c[size-1-nr] -=10;
              dalej = 1;
            }
            else
              dalej = 0;
            nr++;
          }
          nr = 0;
          // avoiding leading 0
          for(int i=0; i<size; i++)
          {
            if(c[i]!=0)
              break;
            nr++;
          }
          int c2[] = new int[size-nr];
          int j = 0;
          // rewriting to second arr
          for(int i=nr; i<size; i++)
          {
            c2[j] = c[i];
            j++;
          }
          boolean iP = this.isPositive;
          result = new BigInt(c2, iP);
      }
      else //substracting one from another
      {
          int which_bigger = 0;
          if(this.digits.length > other.digits.length)
          {
            which_bigger = 1;
          }
          else if(this.digits.length < other.digits.length){
            which_bigger = 2;
          }
          else
          {
            for(int i=0; i<this.digits.length; i++)
            {
              if(this.digits[i] > other.digits[i])
              {
                which_bigger = 1;
                break;
              }
              else if(this.digits[i] < other.digits[i])
              {
                which_bigger = 2;
                break;
              }
            }

          }
          if(which_bigger == 0) // sum is equal to 0, signs are different
          {
            int [] d = new int[1];
            d[0] = 0;
            result = new BigInt(d, true);
          }
          else //substracting one from another
          {

              int [] bigger = this.digits, smaller = this.digits;
              boolean sign = true;
              if(which_bigger == 1)
              {
                sign = this.isPositive;
                bigger = this.digits;
                smaller = other.digits;
              }
              else
              {
                sign = other.isPositive;
                bigger = other.digits;
                smaller = this.digits;
              }
              int [] smaller2 = new int [bigger.length];
              int numer = smaller2.length-1;
              //increasing the smaller number with leading 0
              for(int i=smaller.length-1; i>=0; i--)
              {
                smaller2[numer] = smaller[i];
                numer--;
              }
              while(numer >= 0)
              {
                smaller2[numer] = 0;
                numer --;
              }

              //substracion

              for(int i = bigger.length-1; i>=0; i--)
              {
                  if(bigger[i] >= smaller2[i])
                  {
                    bigger[i] -= smaller2[i];
                  }
                  else //finding non zero on the left
                  {
                    int where = 0;
                    for(int j=i-1; j>=0; j--)
                    {
                        if(bigger[j] != 0)
                        {
                          where = j;
                          break;
                        }
                    }
                    bigger[where] --;
                    where--;
                    while(where > i)
                    {
                      bigger[where] = 9;
                      where--;
                    }
                    bigger[i] += 10;
                    bigger[i] -= smaller2[i];
                  }
              }

              //getting rid of leading 0
               int nr = 0;

            size = bigger.length;
              for(int i=0; i<size; i++)
            {
              if(bigger[i]!=0)
                break;
              else
              nr++;
            }
            int c2[] = new int[size-nr];
            int j = 0;
            // rewriting to second arr

            for(int i=nr; i<size; i++)
            {
              c2[j] = bigger[i];
              j++;
            }
            result = new BigInt (other.digits, true);

            result = new BigInt(c2, sign);
          }
      }
      return result;
  }

  public BigInt times(BigInt other) {

    boolean sign = false;
    if (this.isPositive == other.isPositive)
      sign = true;
    int[] c = new int[this.digits.length + other.digits.length+1];
    int where = 0;
    int size = this.digits.length + other.digits.length;

    for (int i = this.digits.length - 1; i >= 0; i--)
    {
      int pass = 0, start = 0;
        for(int j=other.digits.length-1; j>=0; j--) {
          c[size - start - where-1] += pass + this.digits[i] * other.digits[j];
          if (c[size - start - where-1] >= 10) {
            pass = c[size - start - where-1] / 10;
            c[size - start - where-1] = c[size - start - where-1] % 10;
          } else
            pass = 0;
          start++;
        }
          c[size - start - where - 1] += pass;
          if (c[size - start - where-1] >= 10) {
            pass = c[size - start - where - 1] % 10;
            c[size - start - where - 1] = c[size - start - where - 1] % 10;
          }
        where++;
    }

    //getting rid of leading 0s
    int nr = 0;
    for(int i=0; i<size; i++)
    {
      if(c[i]!=0)
        break;
      nr++;
    }

    int c2[] = new int[size-nr];
    int j = 0;
    // rewriting to second arr
    for(int i=nr; i<size; i++)
    {
      c2[j] = c[i];
      j++;
    }
    BigInt result = new BigInt (c2, sign);
    return result;
  }

  @Override
  public String toString() {

    String S = "";
    if(!this.isPositive)
      S += "-";
    for(int i=0; i<this.digits.length; i++)
    {
      char c = (char)(digits[i] + '0');
      S += c;
    }
    return S;
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
