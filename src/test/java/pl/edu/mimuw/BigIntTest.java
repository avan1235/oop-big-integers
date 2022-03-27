package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntTest {

  @Test
  void testSmallAdd() {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    final var expectedResult = "18";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigAdd() {
    final var x = new BigInt("-853487563485263495358327542");
    final var y = new BigInt("123432958723478523239");
    final var expectedResult = "-853487440052304771879804303";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testSmallTimes() {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    final var expectedResult = "-1008";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigTimes() {
    final var x = new BigInt("-853487563485263495");
    final var y = new BigInt("-12343295872347");
    final var expectedResult = "10534849519467151015195179072765";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testNegation() {
    final var small = new BigInt("37");
    final var big = new BigInt("-3453458734658736");

    final var expectedSmallNegated = "-37";
    final var expectedBigNegated = "3453458734658736";

    final var smallNegated = small.neg().toString();
    final var bigNegated = big.neg().toString();

    assertEquals(expectedSmallNegated, smallNegated);
    assertEquals(expectedBigNegated, bigNegated);
  }
}
