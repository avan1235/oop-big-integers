package pl.edu.mimuw;

import java.net.SocketOption;

import java.util.Arrays;

public final class BigInt {

  private final int[] digits;

  private final boolean isPositive;

  public BigInt(String number) {
    if(number.charAt(0)=='-') {
      digits = new int[number.length()-1];
      this.isPositive=false;
    }
    else {
      digits = new int[number.length()];
      this.isPositive=true;
    }
    for(int i=0;i<this.digits.length;i++) digits[this.digits.length-1-i]=number.charAt(number.length()-i-1)-'0';
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
    int[] tmp=new int[Math.max(this.digits.length, other.digits.length)+1];
    for(int i=0;i<tmp.length;i++) tmp[i]=0;
    int i=0;
    int k=0;
    if(this.isPositive==other.isPositive) {
      //dodawanie
      while(i<Math.min(this.digits.length, other.digits.length)) {
        k+=this.digits[this.digits.length-1-i]+other.digits[other.digits.length-1-i];
        tmp[tmp.length-i-1]= k%10;
        k/=10;
        i++;
      }
      tmp[tmp.length-i-1]=k;
      while(i<this.digits.length) {
        tmp[tmp.length-i-1]+=this.digits[this.digits.length-i-1];
        i++;
      }
      while(i<other.digits.length) {
        tmp[tmp.length-i-1]+=other.digits[other.digits.length-i-1];
        i++;
      }
      if(tmp[0]==0) {
        int[] tmp2=new int[tmp.length-1];
        for(int j=1;j<tmp.length;j++) tmp2[j-1]=tmp[j];
        return new BigInt(tmp2, this.isPositive);
      }
      else return new BigInt(tmp, this.isPositive);
    }
    else {
      //odejmowanie
      while(i<Math.max(this.digits.length, other.digits.length)) {
        if(i<this.digits.length && i<other.digits.length) k+=this.digits[this.digits.length-1-i]-other.digits[other.digits.length-1-i];
        else if(i>=this.digits.length) k-=other.digits[other.digits.length-1-i];
        else k+=this.digits[this.digits.length-i-1];
        if(k<0) {
          tmp[tmp.length-1-i] = k+10;
          k=-1;
        }
        else {
          tmp[tmp.length-1-i]=k;
          k=0;
        }
        i++;
      }
      tmp[tmp.length-i-1]+=k;
      while(i<this.digits.length) {
        tmp[tmp.length-i-1]+=this.digits[this.digits.length-i-1];
        i++;
      }
      while(i<other.digits.length) {
        tmp[tmp.length-i-1]+=other.digits[other.digits.length-i-1];
        i++;
      }
      int ilezer=0;
      while(ilezer<tmp.length-1 && tmp[ilezer]==0) ilezer++;
      boolean isPositive2;
      if(tmp[ilezer]<0) {
        int j=tmp.length-1;
        while(tmp[j]==0) j--;
        tmp[j]=10-tmp[j];
        j--;
        for ( ; j >ilezer; j--) tmp[j] = 9 - tmp[j];
        isPositive2 = !this.isPositive;
        ilezer++;
        j++;
      }
      else isPositive2=this.isPositive;
      while(ilezer<tmp.length-1 && tmp[ilezer]==0) ilezer++;
      int[] tmp2=new int[tmp.length-ilezer];
      for(int j=0;j<tmp.length-ilezer;j++) tmp2[j]=tmp[j+ilezer];
      return new BigInt(tmp2, isPositive2);
    }
  }

  public BigInt times(BigInt other) {
    int[] tmp=new int[this.digits.length+other.digits.length];
    for(int i=0;i<tmp.length;i++) tmp[i]=0;
    for(int i=0;i<this.digits.length;i++) {
      for(int j=0;j<other.digits.length;j++) tmp[tmp.length-i-j-1]+=this.digits[this.digits.length-i-1]*other.digits[other.digits.length-j-1];
    }
    int k=0;
    for(int i=tmp.length-1;i>=0;i--) {
      tmp[i] += k;
      k = tmp[i] / 10;
      tmp[i] %= 10;
    }
    int ilezer=0;
    while(ilezer<tmp.length-1 && tmp[ilezer]==0) ilezer++;
    int[] tmp2=new int[tmp.length-ilezer];
    for(int j=0;j<tmp.length-ilezer;j++) tmp2[j]=tmp[j+ilezer];
    return new BigInt(tmp2, (this.isPositive == other.isPositive));
  }

  @Override
  public String toString() {
    char[] ch=new char[this.digits.length];
    for(int i=0;i<this.digits.length;i++) ch[i]=(char) (this.digits[i]+'0');
    String tmp = new String(ch);
    if(!this.isPositive) tmp="-"+tmp;
    return tmp;
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
