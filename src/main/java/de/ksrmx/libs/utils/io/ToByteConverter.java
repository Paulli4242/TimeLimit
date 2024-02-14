package de.ksrmx.libs.utils.io;

@FunctionalInterface
public interface ToByteConverter<T> {
    byte[] toBytes(T t);
}
