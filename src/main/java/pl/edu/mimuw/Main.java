package pl.edu.mimuw;

public class Main {

  public static void main(String[] args) {
    final var number = new BigInt("99");
    final var number2 = new BigInt("-0");
    System.out.println(number);
    System.out.println(number2);
    System.out.println(number.times(number2));
  }
}
