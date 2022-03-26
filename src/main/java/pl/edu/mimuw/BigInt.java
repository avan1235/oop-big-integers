package pl.edu.mimuw;

import java.util.Arrays;
public final class BigInt{

  private final int[] digits;

  private final boolean isPositive;

  //reprezentujemy dluga liczbe jako jej zapis od najmniej znaczacego znaku
  public BigInt(String number) {
    int len = number.length();
    if(number.charAt(0) != '-'){
      this.digits = new int[len];
      for(int i=0; i < len;i++){
        this.digits[i]= Character.getNumericValue(number.charAt(len-1-i));
      }
      isPositive = true;
    }
    else
    {
    this.digits = new int[len-1];
      for(int i=0; i<len-1;i++){
        this.digits[i]=Character.getNumericValue(number.charAt(len-1-i));
      }
    isPositive = false;
    }
  }
  public BigInt(int[] digits, boolean isPositive) {
    this.digits = digits;
    this.isPositive = isPositive;
  }

  public BigInt(int number) {
    this(Integer.toString(number));
  }

  //trim przyjmuje liczbe ktora moze miec zera na poczatku i zwraca taka bez zer
  public BigInt trim(){
    int len = this.digits.length;
    int realLen = len;
    
    while((this.digits[realLen-1]==0)&&(realLen>1)){
      realLen--;
    }
    int[] newDigits = new int[realLen];
    for(int i=0; i<realLen; i++){
      newDigits[i]=digits[i];
    }
    //jesli w wyniku pojawi sie samo "0", to zmieniamy znak na dodatni, by uniknac roznych reprezentacji zera
    if(realLen==1){
      if(newDigits[0]==0){
        return new BigInt(newDigits, true);
      }
    }
    return new BigInt(newDigits, this.isPositive);
  }

  public BigInt neg() {
    final var digits = Arrays.copyOf(this.digits, this.digits.length);
    return new BigInt(digits, !this.isPositive);
  }


  public BigInt add(BigInt other) {
    boolean newSign=true;
    int[] newDigits;
    int carriage=0;
    int sum = 0;
    int len=java.lang.Math.max(other.digits.length, this.digits.length);

    //dzielimy na dwa przypadki - tych samych i roznych znakow
    //te same znaki
    if(this.isPositive==other.isPositive){
      //jesli maja te same znaki, to ostateczna liczba bedzie miala taki sam znak jak skladniki
        if(!this.isPositive){
          newSign=this.isPositive;
        }
        //maksymalnie nowa liczba ma o znak wiecej 
        newDigits=new int[len+1];
        for(int i=0; i<newDigits.length; i++){
            //dodajemy jak w zwyklym dodawaniu pisemnym, carriage przechowuje wartość nadmiarową, sum to wartosc w danej kolumnie
            sum=carriage;
            if(i<other.digits.length){
              sum=sum+other.digits[i];
            }
            if(i<this.digits.length){
              sum=sum+this.digits[i];
            }
            if(sum>=10){
              sum=sum-10;
              carriage=1;
            }
            else{
              carriage=0;
            }
            newDigits[i]=sum;
        }
    }
    //rozne znaki
    else{
        int signThis=0;
        int signOther=0;
        boolean signOfNumber=true;
        //nowa liczba ma maksymalnie tyle znakow, ile wieksza z dodawanychs
        newDigits=new int[len];

        //ustawiamy znaki do mnozenia przez nie cyfr na podstawie wartosci isPositive
        if(this.isPositive){
          signThis = 1;
        }
        else{
          signThis = -1;
        } 

        if(other.isPositive){
          signOther = 1;
        }
        else{
          signOther = -1;
        }
        //dodajemy liczby w kazdej kolumnie
        for(int i=0; i<newDigits.length; i++){
          
          sum=0;
          if(i<other.digits.length){
            sum=sum+other.digits[i]*signOther;
          }
          if(i<this.digits.length){
            sum=sum+this.digits[i]*signThis;
          }
          newDigits[i]=sum;
        }
        //sprawdzamy czy otrzymana liczba bedzie dodatnia czy ujemna - mowi o tym znak cyfry odpowiadajcej najwiekszej potedze dziesiatki
        for(int i=0; i<newDigits.length; i++){
          if(newDigits[i]>0){
            signOfNumber=true;
          }
          else if(newDigits[i]<0){
            signOfNumber=false;
          }
        }
        //jesli liczba jest dodatnia - zamienamy wszystkie ujemne cyfry na dodatnie dodajac do nich 10 i przerzucamy -1 do nastepnej kolumny
        if(signOfNumber){
          for(int i=0; i<newDigits.length; i++){
            sum=carriage+newDigits[i];
            if(sum<0){
              sum=sum+10;
              carriage=(-1);
            }else{
              carriage=0;
            }
            newDigits[i]=sum;
          }
          newSign = true;
        }
        //jesli liczba jest ujemna - zamienamy wszystkie ujemne cyfry na ujemne odejmujac od nich 10 i przerzucamy 1 do nastepnej kolumny
        else{
          for(int i=0; i<newDigits.length; i++){
            sum=carriage+newDigits[i];
            if(sum>0){
              sum=sum-10;
              carriage=(1);
            }else{
              carriage=0;
            }
            newDigits[i]=(-sum);
          }
          newSign=false;
        }
    }
  //zwracamy liczbe po obcieciu poczatkowych zer
    return (new BigInt(newDigits, newSign)).trim();
  }
  
  // absTimesDigit zwraca |x|*digit, gdzie 0<=digit<10
  private BigInt absTimesDigit(int digit){
    int len = this.digits.length;
    int newDigits[]=new int[len+1];
    int carriage=0;
    int sum=0;
    //mnozymy pisemnie przez jedna cyfre
    for(int i=0; i<len; i++){
        sum=carriage+this.digits[i]*digit;
        newDigits[i]=sum%10;
        carriage=(sum/10);
    }
    newDigits[len]=carriage;
    return ((new BigInt(newDigits, true)).trim());
  }

  //absAddZeros zwraca |x|*10^(numberOfZeros)
  private BigInt absAddZeros(int numberOfZeros){
    int len=this.digits.length;
    int newDigits[]=new int[len+numberOfZeros];
    for(int i=0; i<numberOfZeros; i++){
      newDigits[i]=0;
    }
    for(int i=numberOfZeros; i<newDigits.length; i++){
      newDigits[i]=this.digits[i-numberOfZeros];
    }
    return ((new BigInt(newDigits, true)).trim());
  }

  //times rozpisuje mnozenie liczba jako mnozenie pierwszej kolejno przez cyfre jednosci, cyfre dziesiatek*10, cyfre setek*100...
  //uzywamy prywatnych funkcji absTimesDigit i absAddZeros
  public BigInt times(BigInt other) {
    boolean newSign=(this.isPositive==other.isPositive);
    int otherLen = other.digits.length;
    int zero[] = new int[1];
    zero[0]=0;
    var num=(new BigInt(zero, true));
    for (int i=0; i<otherLen; i++){
      num=(num.add((this.absTimesDigit(other.digits[i])).absAddZeros(i)));
    }
    return new BigInt(num.digits, newSign);
  }
  

  @Override
  public String toString() {
    String output = "";
    int len = this.digits.length;
    if(!this.isPositive){
      output = output + "-";
    }
    for (int i=(len-1); i>=0; i--){
      output = output + String.valueOf(this.digits[i]);
    }
    return output;
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

