//Student Names : Suhaim Alhajeri, Mishari Almulaifi , Abdullah Abdullah
//Student IDs : 2231121808, 2231167795, 2231153435

import java.util.Arrays;

public class TaskHeap {
    public static final int EDF = 1;
    public static final int SJF = 2;
    public static final int SMART = 3;

    private Task[] heap;
    private int size;
    private int mode;

    public TaskHeap(int cap, int mode) {
        heap = new Task[cap];
        this.mode = mode;
    }

    public boolean isEmpty() { return size == 0; }

    public void insert(Task t) {
        if (size == heap.length)
            heap = Arrays.copyOf(heap, heap.length * 2);

        heap[size] = t;
        int i = size++;

        while (i > 0 && compare(heap[i], heap[(i - 1) / 2])) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    public Task removeMin() {
        Task min = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return min;
    }

    private void heapify(int i) {
        int l = 2*i+1, r = 2*i+2, s = i;

        if (l < size && compare(heap[l], heap[s])) s = l;
        if (r < size && compare(heap[r], heap[s])) s = r;

        if (s != i) {
            swap(i, s);
            heapify(s);
        }
    }

    private boolean compare(Task a, Task b) {
        if (mode == EDF) return a.getDeadline() < b.getDeadline();
        if (mode == SJF) return a.getExecutionTime() < b.getExecutionTime();
        return a.getPriority() > b.getPriority();
    }

    private void swap(int i, int j) {
        Task t = heap[i];
        heap[i] = heap[j];
        heap[j] = t;
    }
}
