package pl.edu.mimuw;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    boolean isPositive = true;
    int i_s = 0;
    if(number.charAt(0) == '-'){
      isPositive = false;
      i_s=1;
    }
    this.isPositive=isPositive;

    this.digits = new int[number.length()-i_s];
    for(int i=0 ;(number.length()-i_s)>i;i++ ){
      this.digits[i] = (number.charAt(i+i_s)-48);
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

  private int readNth(int i){
    if(i<=this.digits.length){
      return this.digits[this.digits.length-i];
    }
    return 0;
  }

  private boolean biggerThan(BigInt that){
    if (this.digits.length > that.digits.length){return true;}
    if (this.digits.length < that.digits.length){return false;}
    for(int i=0;this.digits.length>i;i++) {
      if (this.digits[i] != that.digits[i]) {
        return (this.digits[i] - that.digits[i] > 0);
      }
    }
    return true;
  }

  public BigInt add(BigInt other) {
    if(!this.biggerThan(other)){return other.add(this);}

    boolean diff= (this.isPositive != other.isPositive);

    int n = Math.max(this.digits.length,other.digits.length)+1;
    int last=0;
    int[] digits = new int[n];
    for(int i=1;n>i;i++){
      if (diff){
        digits[n-i] += this.readNth(i) - other.readNth(i);
      }
      else{
        digits[n-i] += this.readNth(i) + other.readNth(i);
      }

      if(digits[n-i] <0){
        digits[n-i]+=10;
        digits[n-i-1]--;
      }
      if(digits[n-i] >9){
        digits[n-i]-=10;
        digits[n-i-1]++;
      }
    }

    for(int i=0;n>i;i++){
      if(digits[i] != 0 ){
          n=n-i;
          break;
      }
    }
    int[] out = new int[n];
    out[0]=last;
    for(int i=1;n>=i;i++){
      out[n-i] = digits[digits.length-i];
    }
    return new BigInt(out,this.isPositive);
  }

  public BigInt times(BigInt other) {
    boolean isPositive = (this.isPositive == other.isPositive);

    int n = other.digits.length +this.digits.length+1;
    int[] digits = new int[n];
    BigInt ans = new BigInt(0);
    for(int i=0;other.digits.length>i;i++) {
      for(int o=0;n>o;o++) digits[o]=0;
      for(int o=0;this.digits.length>o;o++) {
        digits[n-i-o-1] += this.digits[this.digits.length-o-1]*other.digits[other.digits.length-i-1];
        digits[n-i-o-2] += digits[n-i-o-1]/10;
        digits[n-i-o-1] %= 10;
      }
      ans = ans.add(new BigInt(digits,true));
    }
    return new BigInt(ans.digits,isPositive);
  }

  @Override
  public String toString() {
    String out="";
    if(!this.isPositive){out += "-";}
    for(int i=0;this.digits.length>i;i++){
      out += (char)(this.digits[i]+48);
    }
    return out;
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
