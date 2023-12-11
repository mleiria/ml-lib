package pt.mleiria.collections.immutable;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static pt.mleiria.collections.immutable.List.list;

class ListTest {

    @Test
    void testList() {
        List<Integer> ex1 = list();
        List<Integer> ex2 = list(1);
        List<Integer> ex3 = list(1, 2);
        System.out.println(ex1.isEmpty());
        System.out.println(ex2.head());
        System.out.println(ex3.head());
    }

    @Test
    void foldRightSum() {
        List<Integer> integerList = list(1, 2, 3, 4, 5);
        int identity = 0;
        Function<Integer, Function<Integer, Integer>> sum = x -> y -> x + y;
        assertEquals(15, integerList.foldLeft(identity, sum));
    }

    @Test
    void foldRightProduct() {
        List<Integer> integerList = list(1, 2, 3, 4, 5);
        int identity = 1;
        Function<Integer, Function<Integer, Integer>> prod = x -> y -> x * y;
        assertEquals(120, integerList.foldLeft(identity, prod));
    }

    @Test
    void length() {
        List<Integer> integerList = list(1, 2, 3, 4, 5);
        assertEquals(5, integerList.length());}
}