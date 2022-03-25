package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

    private final int[] digits;

    private final boolean isPositive;

    public BigInt(String number) {
        this.isPositive = number.charAt(0) != '-';
        final int shift = this.isPositive ? 0 : 1;
        this.digits = new int[number.length() - shift];

        for (int i = 0; i < number.length() - shift; i++)
            this.digits[number.length() - shift - i - 1] = Character.getNumericValue(number.charAt(i + shift));
    }

    public BigInt(int[] digits, boolean isPositive) {
        var numLength = digits.length - 1;
        while (numLength > -1) {
            if (digits[numLength] != 0) break;
            numLength--;
        }
        this.digits = Arrays.copyOf(digits, numLength + 1);
        this.isPositive = isPositive;
    }

    public BigInt(int number) {
        this(Integer.toString(number));
    }

    public BigInt neg() {
        final var digits = Arrays.copyOf(this.digits, this.digits.length);
        return new BigInt(digits, !this.isPositive);
    }

    /**
     * absCompare compares absolute values of two BigInts
     *
     * @param other compared number
     * @return 1 when Abs(this) > Abs(other), 0 when Abs(This) = Abs(Other), -1 otherwise.
     */
    private int absCompare(BigInt other) {
        if (this.digits.length > other.digits.length)
            return 1;
        else if (other.digits.length > this.digits.length)
            return -1;
        else
            return Integer.compare(this.digits[this.digits.length - 1],
                    other.digits[other.digits.length - 1]);
    }

    public BigInt add(BigInt other) {
        // Name numbers as smaller/bigger based on their absolute values.
        BigInt smaller = this.absCompare(other) == 1 ? other : this;
        BigInt bigger = this.absCompare(other) == 1 ? this : other;

        final int minIndex = smaller.digits.length;
        final int sign = smaller.isPositive == bigger.isPositive ? 1 : -1;

        int[] newDigits = Arrays.copyOf(bigger.digits, bigger.digits.length + 1);

        for (int i = 0; i < minIndex; i++) {
            newDigits[i] += sign * smaller.digits[i];

            if (newDigits[i] > 9) {
                newDigits[i + 1]++;
                newDigits[i] -= 10;
            } else if (newDigits[i] < 0) {
                newDigits[i + 1]--;
                newDigits[i] += 10;
            }
        }
        return new BigInt(newDigits, bigger.isPositive);
    }

    public BigInt times(BigInt other) {
        BigInt smaller = this.absCompare(other) == 1 ? other : this;
        BigInt bigger = this.absCompare(other) == 1 ? this : other;

        final int minIndex = smaller.digits.length;
        final int maxIndex = bigger.digits.length;

        int[] newDigits = new int[bigger.digits.length + smaller.digits.length];

        for (int i = 0; i < minIndex; i++) {
            for (int j = 0; j < maxIndex; j++) {
                newDigits[j + i] += smaller.digits[i] * bigger.digits[j];

                if (newDigits[j + i] > 9) {
                    newDigits[j + i + 1] += newDigits[j + i] / 10;
                    newDigits[j + i] %= 10;
                }
            }
        }
        return new BigInt(newDigits, bigger.isPositive == smaller.isPositive);
    }

    @Override
    public String toString() {
        if (this.digits.length == 0) return "0";
        final var result = new StringBuilder();
        for (int i = 0; i < this.digits.length; i++) {
            if (i == 0 && !this.isPositive) result.append("-");
            result.append(this.digits[this.digits.length - i - 1]);
        }
        return result.toString();
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
