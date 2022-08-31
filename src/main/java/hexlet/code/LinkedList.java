package hexlet.code;


import java.util.Objects;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class LinkedList<T> implements List<T> {

    private Item<T> firstInList = null;

    private Item<T> lastInList = null;

    private int size;

    @Override
    public final int size() {
        return this.size;
    }

    @Override
    public final boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public final boolean contains(final Object o) {
        if (firstInList == null) {
            return false;
        }
        for (T elementInItem : this) {
            if (Objects.equals(o, elementInItem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final Iterator<T> iterator() {
        return new ElementsIterator(0);
    }

    @Override
    public final Object[] toArray() {
        final T[] newM = (T[]) new Object[this.size()];
        int i = 0;

        for (T element : this) {
            newM[i++] = element;
        }
        return newM;
    }

    @Override
    public final <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            a = (T1[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;

        Object[] result = a;
        for (Item<T> e = firstInList; e != null; e = e.nextItem) {
            result[i++] = e.element;
        }
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public final boolean add(final T newElement) {
        // BEGIN (write your solution here)
        size++;
        if (firstInList == null) {
            firstInList = new Item<>(newElement, null, null);
            lastInList = firstInList;

        } else {
            lastInList = new Item<>(newElement, this.getItemByIndex(size - 2), null);
            this.getItemByIndex(size - 2).nextItem = lastInList;
        }
        return true;
        // END
    }

    @Override
    public final void add(final int index, final T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(final Object o) {
        // BEGIN (write your solution here)
        if (!this.contains(o)) {
            return true;
        }

        if (this.size == 1 && this.firstInList.element.equals(o)) {
            this.size = 0;
            this.firstInList = null;
            this.lastInList = null;
        } else if (lastInList.element.equals(o)) {
            this.size--;
            this.lastInList = this.getItemByIndex(size - 1);
            this.lastInList.nextItem = null;
        } else if (firstInList.element.equals(o)) {
            this.firstInList = this.getItemByIndex(1);
            this.firstInList.prevItem = null;
            this.size--;
        } else {
            for (int i = 0; i < size; i++) {
                if (this.getItemByIndex(i).element.equals(o)) {
                    this.getItemByIndex(i + 1).prevItem = this.getItemByIndex(i).getPrevItem();
                    this.getItemByIndex(i - 1).nextItem = this.getItemByIndex(i).getNextItem();
                    break;
                }
            }
            size--;
        }
        return true;
        // END
    }

    @Override
    public final T remove(final int index) throws IndexOutOfBoundsException {
        // BEGIN (write your solution here)
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        T elementToRemove = this.getItemByIndex(index).element;
        this.remove(elementToRemove);
        return elementToRemove;
        // END
    }

    private void remove(final Item<T> current) {
        // BEGIN (write your solution here)
        this.remove(current.element);
        // END
    }

    @Override
    public final boolean containsAll(final Collection<?> c) {
        for (final Object item : c) {
            if (!this.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean addAll(final Collection<? extends T> c) {
        for (final T item : c) {
            add(item);
        }
        return true;
    }

    @Override
    public final boolean addAll(final int index, final Collection elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean removeAll(final Collection<?> c) {
        for (final Object item : c) {
            this.remove(item);
        }
        return true;
    }

    @Override
    public final boolean retainAll(final Collection<?> c) {
        this.removeIf(item -> !c.contains(item));
        return true;
    }

    @Override
    public final void clear() {
        this.firstInList = null;
        this.lastInList = null;
        size = 0;
    }

    @Override
    public final List<T> subList(final int start, final int end) {
        return null;
    }

    @Override
    public final ListIterator<T> listIterator() {
        return new ElementsIterator(0);
    }

    @Override
    public final ListIterator<T> listIterator(final int index) {
        return new ElementsIterator(index);
    }

    @Override
    public final int lastIndexOf(final Object target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int indexOf(final Object o) {
        int i = 0;
        if (o == null) {
            for (Item<T> current = firstInList; current != null; current = current.nextItem) {
                if (current.element == null) {
                    return i;
                }
                i++;
            }
        } else {
            for (Item<T> current = firstInList; current != null; current = current.nextItem) {
                if (o.equals(current.element)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    @Override
    public final T set(final int index, final T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        final Item<T> item = getItemByIndex(index);
        T tempElement = item.element;
        item.element = element;
        return tempElement;
    }

    @Override
    public final T get(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return getItemByIndex(index).element;
    }

    private Item<T> getItemByIndex(final int index) {
        int i = 0;
        Item<T> current = firstInList;
        while (i != index) {
            current = current.nextItem;
            i++;
        }
        return current;
    }

    private class ElementsIterator implements ListIterator<T> {

        private Item<T> currentItemInIterator;

        private Item<T> lastReturnedItemFromIterator;

        private int index;

        ElementsIterator(final int index) {
            this.currentItemInIterator = (index == size) ? null : getItemByIndex(index);
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return this.index < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturnedItemFromIterator = currentItemInIterator;
            currentItemInIterator = currentItemInIterator.getNextItem();
            index++;
            return lastReturnedItemFromIterator.element;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        //CHECKSTYLE: stop InnerAssignment check
        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (currentItemInIterator == null) {
                lastReturnedItemFromIterator = currentItemInIterator = lastInList;
            } else {
                lastReturnedItemFromIterator
                        = currentItemInIterator
                        = currentItemInIterator.getPrevItem();
            }
            /*lastReturnedItemFromIterator
            = currentItemInIterator
            = (currentItemInIterator == null) ? lastInList : currentItemInIterator.prevItem;*/
            index--;
            return lastReturnedItemFromIterator.element;
        }
        //CHECKSTYLE: resume InnerAssignment check

        @Override
        public void add(final T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final T element) {
            if (lastReturnedItemFromIterator == null) {
                throw new IllegalStateException();
            }
            lastReturnedItemFromIterator.element = element;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public void remove() {
            if (lastReturnedItemFromIterator == null) {
                throw new IllegalStateException();
            }
            LinkedList.this.remove(lastReturnedItemFromIterator);
            lastReturnedItemFromIterator = null;
            index--;
        }
    }

    private static class Item<T> {

        private T element;

        private Item<T> nextItem;

        private Item<T> prevItem;

        Item(final T element, final Item<T> prevItem, final Item<T> nextItem) {
            this.element = element;
            this.nextItem = nextItem;
            this.prevItem = prevItem;
        }

        public Item<T> getNextItem() {
            return nextItem;
        }

        public Item<T> getPrevItem() {
            return prevItem;
        }
    }
}
