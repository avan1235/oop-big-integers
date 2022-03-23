package pl.edu.mimuw;

public class Main {

  public static void main(String[] args) {
    final var number = new BigInt("-10");
    final var number2 = new BigInt("2");
    System.out.println(number);
    System.out.println(number2);
    System.out.println(number.add(number2));
  }
}
