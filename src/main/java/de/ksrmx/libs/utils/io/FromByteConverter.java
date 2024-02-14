package de.ksrmx.libs.utils.io;

@FunctionalInterface
public interface FromByteConverter<T> {
    T fromBytes(byte[] b);
}
