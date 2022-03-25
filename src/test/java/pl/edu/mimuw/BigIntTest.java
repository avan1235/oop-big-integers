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
    void testSmallAdd2() {
        final var x = new BigInt(12345);
        final var y = new BigInt(-54321);
        final var expectedResult = "-41976";

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
    void testBigNegative() {
        final var x = new BigInt("-2404241241120101010101010101010");
        final var y = new BigInt("-4200000000000010101010101010101");
        final var expectedResult = "-6604241241120111111111111111111";

        final var result = x.add(y).toString();

        assertEquals(expectedResult, result);
    }

    @Test
    void testSmallTimes() {
        final var x = new BigInt(42);
        final var y = new BigInt(-42);
        final var expectedResult = "-1764";

        final var result = x.times(y).toString();
        assertEquals(expectedResult, result);
    }

    @Test
    void testBigTimes() {
        final var x = new BigInt("-42424242424242424242");
        final var y = new BigInt("1000004000000002000001");
        final var expectedResult = "-42424412121212206060224240727272726424242";

        final var result = x.times(y).toString();
        assertEquals(expectedResult, result);
    }

    @Test
    void testReduction() {
        final var x = new BigInt("-4242");
        final var y = new BigInt("4242");
        final var expectedResult = "0";

        final var result = x.add(y).toString();

        assertEquals(expectedResult, result);
    }

    @Test
    void testMultipleOperations() {
        final var x = new BigInt("-4242");
        final var y = new BigInt("10000000");
        final var z = new BigInt("-10000000");
        final var expectedResult = "-279945640000000";

        var result = x.add(y);
        result = result.add(z);
        result = result.times(x);
        result = result.add(y);
        result = result.times(z);


        assertEquals(expectedResult, result.toString());
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
