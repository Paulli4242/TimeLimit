package de.ksrmx.libs.utils.iteration;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ConversionIteration<T,U> implements Iteration<U> {
    private final Iterator<T> iterator;
    private final Function<T,U> f;

    public ConversionIteration(Iterator<T> iterator, Function<T,U> f){
        this.iterator = iterator;
        this.f = f;
    }
    public ConversionIteration(Iterable<T> iterable, Function<T,U> f){
        this.iterator = iterable.iterator();
        this.f = f;
    }

    @SuppressWarnings("unchecked")
    public U[] toArray(Class<U> cls){
        U[] a = (U[]) Array.newInstance(cls,0);
        while(hasNext()){
            a=ArrayUtils.addAndExpand(a,next());
        }
        return a;
    }
    @SuppressWarnings("unchecked")
    public U[] toArray(Class<?> cls, int size){
        U[] a = (U[]) Array.newInstance(cls,size);
        size=0;
        while(size<a.length&&hasNext()){
            a[size]=next();
        }
        return a;
    }

    @Override
    public Iterator<U> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public U next() {
        return f.apply(iterator.next());
    }
}
