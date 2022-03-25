package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    var isPositive = number.charAt(0) != '-';
    number = new StringBuilder(number).reverse().toString();
    var digits = new int[number.length()];
    for (int i = 0; i < number.length() && number.charAt(i) != '-'; ++i)
      digits[i] = number.charAt(i) - '0';
    var actually = new BigInt(digits, isPositive).normalized();

    this.digits = actually.digits;
    this.isPositive = actually.isPositive;
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
    return new BigInt(digits, !this.isPositive).normalized();
  }

  // The number is stored as an array of digits, least significant first. There's
  // an *intermediate* form where negative signs of the digits and leading zeroes
  // are permitted, and a normalised form where they are not.

  private int signedDigitAt(int i) {
    if (i >= this.digits.length)
      return 0;
    return this.digits[i] * (this.isPositive ? 1 : -1);
  }

  private BigInt normalized() {
    final var n = this.digits.length;
    var digits = Arrays.copyOf(this.digits, n);

    // Extract the information from isPositive.
    if (!isPositive)
      for (int i = 0; i < digits.length; ++i)
        digits[i] = -digits[i];

    // Carry the digits, do it twice sometimes because we will have had done
    // something of very little practical value if the number is negative.
    var isPositive = true;
    for (;;) {
      for (int i = 0; i + 1 < n; ++i) {
        digits[i + 1] += Math.floorDiv(digits[i], 10);
        digits[i] = Math.floorMod(digits[i], 10);
      }
      if (digits[n - 1] >= 0)
        break;

      isPositive = false;
      for (int i = 0; i < n; ++i)
        digits[i] = -digits[i];
    }

    // Remove leading (trailing) zeroes.
    int m = n;
    while (m > 1 && digits[m - 1] == 0)
      --m;
    digits = Arrays.copyOf(digits, m);

    return new BigInt(digits, isPositive);
  }

  public BigInt add(BigInt other) {
    final var n = this.digits.length + other.digits.length + 10;
    var c = new int[n];
    for (int i = 0; i < n; ++i) {
      c[i] += this.signedDigitAt(i) + other.signedDigitAt(i);
    }
    return new BigInt(c, true).normalized();
  }

  public BigInt times(BigInt other) {
    final var n = this.digits.length + other.digits.length + 10;
    var c = new int[n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n && i + j < n; ++j) {
        c[i + j] += this.signedDigitAt(i) * other.signedDigitAt(j);
      }
    }
    return new BigInt(c, true).normalized();
  }

  @Override
  public String toString() {
    var s = new StringBuilder();
    if (!this.isPositive)
      s.append("-");
    for (int i = this.digits.length - 1; i >= 0; --i)
      s.append(this.digits[i]);
    return s.toString();
  }

  @Override
  public int hashCode() {
    final var arrayHashCode = Arrays.hashCode(digits);
    final var positiveHashCode = Boolean.hashCode(isPositive);

    return arrayHashCode * 17 + positiveHashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BigInt))
      return false;
    final var other = (BigInt) obj;

    return Arrays.equals(this.digits, other.digits)
        && this.isPositive == other.isPositive;
  }
}
