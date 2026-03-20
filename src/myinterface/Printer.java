package myinterface;

@FunctionalInterface
public interface Printer<E> {
    String print(E e);
}
