package pt.mleiria.collections.immutable;

import org.junit.jupiter.api.Test;

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
}