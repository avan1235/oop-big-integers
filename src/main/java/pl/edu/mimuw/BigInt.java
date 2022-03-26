package pl.edu.mimuw;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.Collections.swap;

public final class BigInt
{

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number)
  {
    if (number.charAt(0) == '-')
    {
      this.isPositive = false;
    }
    else this.isPositive = true;
    this.digits = new int[number.length() - (this.isPositive ? 0 : 1)];
    for (int i = 0;i<this.digits.length;i++)
    {
      this.digits[i] = number.charAt(i + (this.isPositive ? 0 : 1)) - '0';
    }
  }

  public BigInt(int[] digits, boolean isPositive)
  {
    this.digits = digits;
    this.isPositive = isPositive;
  }

  public BigInt(int number)
  {
    this(Integer.toString(number));
  }

  public BigInt neg()
  {
    final var digits = Arrays.copyOf(this.digits, this.digits.length);
    return new BigInt(digits, !this.isPositive);
  }

  public BigInt add(BigInt other)
  {
    if (!this.isPositive)
      return this.neg().add(other.neg()).neg();
    if (other.digits.length > this.digits.length)
      return other.add(this);
    if (!other.isPositive)
      for (int i = 0;i<other.digits.length;i++)
        other.digits[i] = -other.digits[i];

    for (int i = 0;i<other.digits.length-1;i++)
    {
      this.digits[this.digits.length - i - 1] += other.digits[other.digits.length - i - 1];
      if (this.digits[this.digits.length - i - 1] > 9)
      {
        this.digits[this.digits.length - i - 1] -= 10;
        this.digits[this.digits.length - i - 2]++;
      }
      if (this.digits[this.digits.length - i - 1] < 0)
      {
        this.digits[this.digits.length - i - 1] += 10;
        this.digits[this.digits.length - i - 2]--;
      }
    }
    this.digits[this.digits.length - other.digits.length] += other.digits[0];
    for (int i = this.digits.length - other.digits.length;i>0;i--) {
      if (this.digits[i] > 9)
      {
        this.digits[i] -= 10;
        this.digits[i - 1]++;
      }
      if (this.digits[i] < 0)
      {
        this.digits[i] += 10;
        this.digits[i - 1]--;
      }
    }
    if (this.digits[0] > 9)
    {
      this.digits[0] -= 10;
      int[] ret = new int[this.digits.length + 1];
      ret[0] = 1;
      System.arraycopy(this.digits, 0, ret, 1, this.digits.length);
      return new BigInt(ret, true);
    }
    if (this.digits[0] < 0)
    {
      this.digits[0] = -this.digits[0];
      for (int i = 1;i<this.digits.length;i++)
        this.digits[i] = 10 - this.digits[i];
      return this.neg();
    }
    return this;
  }

  public BigInt times(BigInt other)
  {
    if (!this.isPositive)
      return this.neg().times(other.neg());
    if (!other.isPositive)
      return this.times(other.neg()).neg();
    if (other.equals(new BigInt(10)))
    {
      if (this.equals(new BigInt(0)))
        return new BigInt(0);
      int[] tmp = new int[this.digits.length + 1];
      System.arraycopy(this.digits, 0, tmp, 0, this.digits.length);
      tmp[this.digits.length] = 0;
      return new BigInt(tmp, true);
    }
    BigInt ret = new BigInt(0);
    for (int i = 0;i<other.digits.length;i++)
    {
      ret = ret.times(new BigInt(10)).add(this.times(other.digits[i]));
    }
    return ret;
  }

  private BigInt times(int other)
  {
    BigInt ret = new BigInt(0);
    for (int digit : this.digits)
    {
      ret = ret.times(new BigInt(10)).add(new BigInt(digit * other));
    }
    return ret;
  }

  @Override
  public String toString()
  {
    String s = "";
    if (!this.isPositive) s = "-";
    int i;
    for (i = 0;i<this.digits.length;i++)
      if (this.digits[i] != 0)
        break;
    if (i == this.digits.length)
      return "0";
    for (;i<this.digits.length;i++)
      s = s.concat(String.valueOf(this.digits[i]));
    return s;
  }

  @Override
  public int hashCode()
  {
    final var arrayHashCode = Arrays.hashCode(digits);
    final var positiveHashCode = Boolean.hashCode(isPositive);

    return arrayHashCode * 17 + positiveHashCode;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof BigInt)) return false;
    final var other = (BigInt) obj;

    return Arrays.equals(this.digits, other.digits)
      && this.isPositive == other.isPositive;
  }
}
