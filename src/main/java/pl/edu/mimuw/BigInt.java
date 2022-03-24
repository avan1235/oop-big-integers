package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    int i;
    if (number.charAt(0) == '-') {
      this.isPositive = false;
      i = 1;
      this.digits = new int[number.length() - 1];
    } else {
      this.isPositive = true;
      i = 0;
      this.digits = new int[number.length()];
    }

    for (; i < number.length(); i++) {
      int digit = number.charAt(i) - '0';
      if (this.isPositive) {
        this.digits[i] = digit;
      } else {
        this.digits[i - 1] = digit;
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
    int d1 = this.digits.length;
    int d2 = other.digits.length;
    int[] tab = new int[Math.max(d1, d2) + 1];
    boolean que;

    if ((!this.isPositive && !other.isPositive) || (this.isPositive && other.isPositive)) {
      // dwie liczby dodatnie lub dwie ujemne
      que = this.isPositive;
      int reszta = 0;
      int p1 = d1 - 1;
      int p2 = d2 - 1;
      for (int i = 0; i <= Math.max(d1, d2); i++) {
        if (p1 >= 0 && p2 >= 0) {
          int sum = this.digits[p1] + other.digits[p2] + reszta;
          int dig = sum % 10;
          reszta = sum / 10;
          tab[i] = dig;
        } else if (p1 >= 0) {
          int sum = this.digits[p1] + reszta;
          int dig = sum % 10;
          reszta = sum / 10;
          tab[i] = dig;
        } else if (p2 >= 0) {
          int sum = this.digits[p2] + reszta;
          int dig = sum % 10;
          reszta = sum / 10;
          tab[i] = dig;
        } else {
          tab[i] = reszta;
          reszta = 0;
        }
        p1--;
        p2--;
      }
    } else {
      // jedna liczba dodatnia druga ujemna
      int p1 = d1 - 1;
      int p2 = d2 - 1;
      int bit = 0;
      // od dluzszej odejmuje krotsza
      if (d1 >= d2) {
        que = this.isPositive;
        for (int i = 0; i <= d1; i++) {
          if (p1 >= 0 && p2 >= 0) {
            int dig;
            if (this.digits[p1] - bit < other.digits[p2]) {
              if (this.digits[p1] == 0) {
                dig = 10 - bit - other.digits[p2];
              } else {
                dig = this.digits[p1] + 10 - bit - other.digits[p2];
              }
              bit = 1;
            } else {
              dig = this.digits[p1] - bit - other.digits[p2];
              bit = 0;
            }
            tab[i] = dig;
          } else if (p1 >= 0) {
            tab[i] = this.digits[p1] - bit;
            bit = 0;
          } else {
            tab[i] = 0;
          }
          p1--;
          p2--;
        }
      } else {
        que = other.isPositive;
        for (int i = 0; i <= d2; i++) {
          if (p1 >= 0 && p2 >= 0) {
            int dig;
            if (other.digits[p2] - bit < this.digits[p1]) {
              if (other.digits[p2] == 0) {
                dig = 10 - bit - this.digits[p1];
              } else {
                dig = other.digits[p2] + 10 - bit - this.digits[p1];
              }
              bit = 1;
            } else {
              dig = other.digits[p2] - bit - this.digits[p1];
              bit = 0;
            }
            tab[i] = dig;
          } else if (p2 >= 0) {
            tab[i] = other.digits[p2] - bit;
            bit = 0;
          } else {
            tab[i] = 0;
          }
          p1--;
          p2--;
        }
      }
    }

    // petla do odwracania tablicy, zeby liczba byla w poprawnej kolejnosci
    int i;
    int d;
    int[] resultArr;
    if (tab[tab.length - 1] == 0) {
      d = tab.length - 1;
      i = tab.length - 2;
    } else {
      d = tab.length;
      i = tab.length - 1;
    }
    resultArr = new int[d];
    for (int j = 0; j < d; j++) {
      resultArr[j] = tab[i];
      i--;
    }

    return new BigInt(resultArr, que);
  }

  public BigInt times(BigInt other) {
    int d1 = this.digits.length;
    int d2 = other.digits.length;
    int[] tab = new int[d1 + d2];
    int[] helper = new int[d1 + d2];
    for (int i = 0; i < d1 + d2; i++) tab[i] = 0;
    boolean que;
    que = (this.isPositive && other.isPositive) || (!this.isPositive && !other.isPositive);

    BigInt result = new BigInt(tab, true);

    int reszta = 0;
    int p1 = d1 - 1;

    for (int i = p1; i >= 0; i--) {
      int p2 = d2 - 1;
      int pf = p1 + p2 + 1;
      for (int j = p2; j >= 0; j--) {
        int tim = this.digits[p1] * other.digits[p2] + reszta;
        int dig = tim % 10;
        reszta = tim / 10;
        helper[pf] = dig;
        pf--;
        p2--;
      }
      helper[pf] = reszta;
      reszta = 0;

      result = result.add(new BigInt(helper, true));

      for (int g = 0; g < d1 + d2; g++) helper[g] = 0;
      p1--;
    }

    BigInt exit = new BigInt(result.digits, que);
    return exit;
  }

  @Override
  public String toString() {
    String result = Arrays.toString(this.digits).replaceAll("\\[|\\]|,|\\s", "");

    result = result.replaceAll("^0+(?!$)", "");

    if (!this.isPositive) result = '-' + result;

    return result;
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
