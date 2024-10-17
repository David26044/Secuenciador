/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

/**
 *
 * @author ACER
 */
public class Stack<T> {

    private T[] stack;
    private int last;

    public Stack(int size) {
        stack = (T[]) new Object[size];
        last = -1;
    }

    public void push(T item) {
        last++;
        stack[last] = item;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T temp = stack[last];
        stack[last] = null;
        last--;
        return temp;
    }

    public boolean isEmpty() {
        if (last == -1) {
            return true;
        }
        return false;
    }
}
