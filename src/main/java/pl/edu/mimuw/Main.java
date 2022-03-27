package pl.edu.mimuw;

public class Main {

  public static void main(String[] args) {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    // final var expectedResult = "-1008";

    System.out.println(x.times(y).toString());
  }
}
