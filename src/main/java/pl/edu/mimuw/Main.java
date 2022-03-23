package pl.edu.mimuw;

public class Main {

  public static void main(String[] args) {
    for (String arg : args) {
      System.out.println(arg + " ");
    }
    final var number = new BigInt("42");
    System.out.println(number);
  }
}
