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
  void constructorTest() {
    final var a = new BigInt("-100");
    final var b = new BigInt("100");
    final var c = new BigInt("-1244121233123");
    final var d = new BigInt("11111111111111111111111");

    assertEquals("-100", a.toString());
    assertEquals("100", b.toString());
    assertEquals("-1244121233123", c.toString());
    assertEquals("11111111111111111111111", d.toString());
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
    final var x = new BigInt("-16");
    final var y = new BigInt("54");
    final var expectedResult = "-864";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testSmallTimes2() {
    final var x = new BigInt("16");
    final var y = new BigInt("54");
    final var expectedResult = "864";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testSmallTimes3() {
    final var x = new BigInt("-16");
    final var y = new BigInt("-54");
    final var expectedResult = "864";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigTimes() {
    final var x = new BigInt("-1648713333122897313");
    final var y = new BigInt("511111123333331234");
    final var expectedResult = "-842675723747084792503220933997574242";

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
