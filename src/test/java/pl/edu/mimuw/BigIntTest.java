package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntTest {

  @Test
  void testSmallAdd() {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    final var expectedResult = "18";

    final var result = x.add(y);

    assertEquals(expectedResult, result.toString());

    assertEquals("-1", new BigInt(69).add(new BigInt(-70)).toString());
    assertEquals("0", new BigInt(69).add(new BigInt(-69)).toString());
    assertEquals("1", new BigInt(69).add(new BigInt(-68)).toString());
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
    final var x = new BigInt("21");
    final var y = new BigInt("-37");
    final var expectedResult = "-777";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
    assertEquals("1", new BigInt(1).times(new BigInt(1)).toString());
    assertEquals("-1", new BigInt(1).times(new BigInt(-1)).toString());
    assertEquals("-1", new BigInt(-1).times(new BigInt(1)).toString());
    assertEquals("1", new BigInt(-1).times(new BigInt(-1)).toString());
    assertEquals("0", new BigInt(-1).times(new BigInt(0)).toString());
  }

  @Test
  void testBigTimes() {
    final var x = new BigInt("2100000000");
    final var y = new BigInt("-3700000000");
    final var expectedResult = "-7770000000000000000";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);

    assertEquals("0", new BigInt(0).times(new BigInt("-999999999999999999")).toString());
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
