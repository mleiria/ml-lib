package pt.mleiria.collections.immutable;

import org.junit.jupiter.api.Test;
import pt.mleiria.dto.Pair;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static pt.mleiria.collections.immutable.CollectionUtilities.list;

class CollectionUtilitiesTest {

    @Test
    void foldLeft() {
        final List<Integer> list = list(1, 2, 3, 4, 5);
        final Function<Integer, Function<Integer, Integer>> f = x -> y -> x + y;
        final Integer res = CollectionUtilities.foldLeft(list, 0, f);
        assertEquals(15, res);

        // Same but using Java functional interfaces
        final BinaryOperator<Integer> bf = (x, y) -> x + y;
        int res1 = list.stream().reduce(0, bf);
        assertEquals(15, res1);
    }

    @Test
    void unfold() {
        assertEquals("[1, 2, 4, 8]", CollectionUtilities.unfold(1, x -> x * 2, x -> x < 10).toString());
        assertEquals("[x, xx, xxx, xxxx]", CollectionUtilities.unfold("x", x -> x + "x", x -> x.length() < 5).toString());
        assertEquals("[1, 2, 3, 4, 5]", CollectionUtilities.unfold(1, x -> x + 1, x -> x <= 5).toString());
    }

    @Test
    void range() {
        assertEquals("[]", CollectionUtilities.range(0, 0).toString());
        assertEquals("[0]", CollectionUtilities.range(0, 1).toString());
        assertEquals("[0, 1, 2, 3, 4]", CollectionUtilities.range(0, 5).toString());
        assertEquals("[]", CollectionUtilities.range(5, 1).toString());
    }

    private static String addIS(Integer i, String s) {
        return "(" + i + " + " + s + ")";
    }

    @Test
    void foldRight() {
        List<Integer> list = list(1, 2, 3, 4, 5);
        String identity = "0";
        Function<Integer, Function<String, String>> f = x -> y -> addIS(x, y);
        assertEquals("(1 + (2 + (3 + (4 + (5 + 0)))))", CollectionUtilities.foldRight(list, identity, f));
    }

    @Test
    void zipListsEqualSize() {
        final List<String> men = list("Manuel", "Leiria");
        final List<String> women = list("Sofia", "Quental");
        final List<Pair<String, String>> couples = CollectionUtilities.zip(men, women);
        assertEquals(2, couples.size());
        assertEquals("[Pair[_1=Manuel, _2=Sofia], Pair[_1=Leiria, _2=Quental]]", couples.toString());
    }
    @Test
    void zipListsDifferentSizeFirst() {
        final List<String> men = list("Manuel", "Aleixo", "Leiria");
        final List<String> women = list("Sofia", "Quental");
        final List<Pair<String, String>> couples = CollectionUtilities.zip(men, women);
        assertEquals(2, couples.size());
        assertEquals("[Pair[_1=Manuel, _2=Sofia], Pair[_1=Aleixo, _2=Quental]]", couples.toString());
    }

    @Test
    void zipListsDifferentSizeSecond() {
        final List<String> men = list("Manuel",  "Leiria");
        final List<String> women = list("Sofia", "Aboim", "Quental");
        final List<Pair<String, String>> couples = CollectionUtilities.zip(men, women);
        assertEquals(2, couples.size());
        assertEquals("[Pair[_1=Manuel, _2=Sofia], Pair[_1=Leiria, _2=Aboim]]", couples.toString());
    }


}