package myutil;

public class ObjectUtil {
    // Return true if the given object is of the same type as target element
    public static <E> boolean isCompatible(Object obj, E target) {
        return target.getClass().isInstance(obj);
    }
}
