package org.example;

public class ItemList<T>{

    private T[] items;


    public ItemList(int size) {
        this.items = (T[]) new Object[size];
    }

    public void addItem(T item, int index) {
        this.items[index] = item;
    }

    public T getItem(int index) {
        if (index >= 0 && index < items.length) {
            return items[index];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    public int getSize() {
        return items.length;
    }

    public boolean exists(String name){
        for (T item : items) {
            if (item != null && name.equals(item.toString())) {
                return true;
            }
        }
        return false;
    }
//    public boolean nameExists(String name) {
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].) {
//                return true;
//            }
//        }
//        return false;
//    }
}
