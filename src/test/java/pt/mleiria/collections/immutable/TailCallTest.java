package pt.mleiria.collections.immutable;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pt.mleiria.collections.immutable.TailCall.ret;
import static pt.mleiria.collections.immutable.TailCall.sus;

class TailCallTest {

    /**
     * Add method
     *
     * @param x
     * @param y
     * @return
     */
    public static TailCall<Integer> add(int x, int y) {
        return y == 0
                ? new TailCall.Return<>(x)
                : new TailCall.Suspend<>(() -> add(x + 1, y - 1));
    }

    /**
     * Add method
     *
     * @param x
     * @param y
     * @return
     */
    public static TailCall<Integer> add_v1(int x, int y) {
        return y == 0
                ? ret(x)
                : sus(() -> add_v1(x + 1, y - 1));
    }

    /**
     * Add function
     */
    public static Function<Integer, Function<Integer, TailCall<Integer>>> add_v2 =
            a -> b -> b == 0
                    ? ret(a)
                    : sus(() -> TailCallTest.add_v2.apply(a + 1).apply(b - 1));

    public static Function<Integer, Function<Integer, Integer>> add_v3 = x -> y -> {
        class AddHelper {
            Function<Integer, Function<Integer, TailCall<Integer>>> addHelper =
                    a -> b -> b == 0
                            ? ret(a)
                            : sus(() -> this.addHelper.apply(a + 1).apply(b - 1));
        }
        return new AddHelper().addHelper.apply(x).apply(y).eval();
    };

    @Test
    void testAdd() {
        int res = add(3, 100000).eval();
        assertEquals(100003, res);
    }

    @Test
    void testAdd_V1() {
        int res = add_v1(3, 100000).eval();
        assertEquals(100003, res);
    }

    @Test
    void testAdd_V2() {
        int res = add_v2.apply(3).apply(100000).eval();
        assertEquals(100003, res);
    }

    @Test
    void testAdd_V3() {
        int res = add_v3.apply(3).apply(100000);
        assertEquals(100003, res);
    }
}