package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntTest
{

  @Test
  void testSmallAdd()
  {
    final var x = new BigInt(42);
    final var y = new BigInt(-24);
    final var expectedResult = "18";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testBigAdd()
  {
    final var x = new BigInt("-853487563485263495358327542");
    final var y = new BigInt("123432958723478523239");
    final var expectedResult = "-853487440052304771879804303";

    final var result = x.add(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testSmallTimes()
  {
    for (int i = 0;i<20;i++)
      for (int j = 0;j<20;j++)
      {
        var x = new BigInt(i);
        var y = new BigInt(j);
        var expectedResult = Integer.toString(i * j);
        var result = x.times(y).toString();
        assertEquals(expectedResult, result);
      }
  }

  @Test
  void testBigTimes()
  {
    final var x = new BigInt("-17258671298054151056129805");
    final var y = new BigInt("-979124789426384953216754171");
    final var expectedResult = "16898392900486464517411379656530173692104355351166655";

    final var result = x.times(y).toString();

    assertEquals(expectedResult, result);
  }

  @Test
  void testNegation()
  {
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
