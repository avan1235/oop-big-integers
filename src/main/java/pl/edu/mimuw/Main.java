package pl.edu.mimuw; 
public class Main {

  public static void main(String[] args) {
    final var number = new BigInt("-12");
    final var nom = new BigInt("-122323");
    System.out.println(number.times(nom));
  }
}
