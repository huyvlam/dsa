package myhelper;

public class Checker {
    public static <E> void checkData(E data) {
        checkData(data, "Data");
    }

    public static <E> void checkData(E data, String name) {
        if (data == null)
            throw new IllegalArgumentException(name + " cannot be null");
    }

    public static void checkIndex(int i, int n) {
        if (i < 0 || i >= n)
            throw new IndexOutOfBoundsException("Index cannot be out of bound");
    }
}
