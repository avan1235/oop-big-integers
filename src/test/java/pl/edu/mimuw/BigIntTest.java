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
    final var x = new BigInt("-1000");
    final var y = new BigInt(-213769420);
    final var expectedResult = "213769420000";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
    //throw new IllegalStateException("TODO task 5: write test for multiplication of small numbers");
  }

  @Test
  void testBigTimes() {
    final var x = new BigInt("14654314369");
    final var y = new BigInt(-2137692115);
    final var expectedResult = "-31326412277342500435";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
    //throw new IllegalStateException("TODO task 6: write test for multiplication of big numbers");
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
