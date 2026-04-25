//Student Names : Suhaim Alhajeri, Mishari Almulaifi , Abdullah Abdullah
//Student IDs : 2231121808, 2231167795, 2231153435

import java.util.Arrays;

public class TaskManager {
    private Task[] tasks;
    private int size;

    public TaskManager(int cap) {
        tasks = new Task[cap];
        size = 0;
    }

    public void addTask(Task t) {
        if (size == tasks.length)
            tasks = Arrays.copyOf(tasks, tasks.length * 2);

        tasks[size++] = t;
    }

    public void resetAll() { size = 0; }

    public void removeTaskById(int id) {
        int i = id - 1;
        if (i < 0 || i >= size) return;

        for (int j = i; j < size - 1; j++)
            tasks[j] = tasks[j + 1];

        size--;
    }

    public void viewTasks() {
        for (int i = 0; i < size; i++)
            tasks[i].print(i + 1);
    }

    public void scheduleTasks(int mode) {
        TaskHeap heap = new TaskHeap(size, mode);

        for (int i = 0; i < size; i++)
            heap.insert(tasks[i]);

        while (!heap.isEmpty())
            heap.removeMin().print(0);
    }

    public void simulateScheduling(int limit) {
        TaskHeap heap = new TaskHeap(limit, TaskHeap.EDF);

        for (int i = 0; i < Math.min(limit, size); i++)
            heap.insert(tasks[i]);

        while (!heap.isEmpty())
            heap.removeMin();
    }

    public int getSize() { return size; }
    public Task getTask(int i) { return tasks[i]; }
}
