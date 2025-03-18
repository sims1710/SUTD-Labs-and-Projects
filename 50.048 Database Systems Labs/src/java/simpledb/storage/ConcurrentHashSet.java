package simpledb.storage;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;

public class ConcurrentHashSet<E> implements Iterable<E> {

    private final ConcurrentHashMap<E, Boolean> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    public boolean add(E element) {
        return map.put(element, Boolean.TRUE) == null;
    }

    public boolean contains(E element) {
        return map.containsKey(element);
    }

    public boolean remove(E element) {
        return map.remove(element) != null;
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return ((map == null) || map.isEmpty());
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public Set<E> keySet() {
        return map.keySet();
    }
}
