package wu.a.lib.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@SuppressWarnings("unchecked")
public final class FixedLengthQueue<E> implements Queue<E> {
    private Queue<E> mList = new LinkedList<E>();
    private int mLength;

    public FixedLengthQueue(int length){
        mLength = length;
    }

    @Override
    public E poll(){
        return mList.poll();
    }

    @Override
    public boolean add(E value) {
        return mList.add(value);
    }

    @Override
    public boolean addAll(Collection collection) {
        int iCount = mList.size() >= mLength ? mLength : mList.size();
        int i = 0;
        for (Iterator it = mList.iterator(); it.hasNext();) {
            mList.add((E) it.next());
            i++;
            if (i == iCount) break;
        }
        return iCount >0;
    }

    @Override
    public boolean offer(E o) {
        if (mList.size() == mLength){
            this.poll();
        }
        return mList.offer(o);
    }

    @Override
    public E element() {
        return mList.element();
    }

    @Override
    public E peek() {
        return mList.peek();
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public Iterator iterator() {
        return mList.iterator();
    }

    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @Override
    public Object[] toArray(Object[] o) {
        return mList.toArray(o);
    }

    @Override
    public boolean containsAll(Collection list) {
        return list.containsAll(list);
    }

    @Override
    public boolean removeAll(Collection list) {
        return mList.removeAll(list);
    }

    @Override
    public boolean retainAll(Collection list) {
        return mList.retainAll(list);
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public E remove() {
        return mList.remove();
    }

    @Override
    public boolean contains(Object o) {
        return mList.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        return mList.remove(o);
    }

    @Override
    public String toString() {
        return mList.toString();
    }

    @Override
    public int hashCode() {
        return mList.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object queue) {
        return mList.equals(queue);
    }
}
