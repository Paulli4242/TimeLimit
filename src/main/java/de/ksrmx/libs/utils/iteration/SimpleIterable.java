package de.ksrmx.libs.utils.iteration;

import java.util.Iterator;

@SuppressWarnings("unused")
public record SimpleIterable<T>(Iterator<T> iterator) implements Iterable<T> {

}
