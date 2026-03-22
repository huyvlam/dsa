package myhelper;

import mymodel.Person;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class Helper {
    // Return true if the given object is of the same type as target element
    public static <E> boolean isCompatible(Object obj, E target) {
        return target.getClass().isInstance(obj);
    }

    static void main(String args[]) {
        Object obj = new Object();
        IO.println(isCompatible(new Object(), new Person("Zizi", 21)));
    }
}
