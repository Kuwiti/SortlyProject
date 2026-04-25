//Student Names : Suhaim Alhajeri, Mishari Almulaifi , Abdullah Abdullah
//Student IDs : 2231121808, 2231167795, 2231153435

import java.util.Arrays;

public class BasicTaskManager {
    private Task[] tasks;
    private int size;

    public BasicTaskManager(int cap) {
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

    public void simulateScheduling(int limit) {
        Task[] copy = Arrays.copyOf(tasks, Math.min(limit, size));

        for (int i = 0; i < copy.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < copy.length; j++)
                if (copy[j].getDeadline() < copy[min].getDeadline())
                    min = j;

            Task temp = copy[min];
            copy[min] = copy[i];
            copy[i] = temp;
        }
    }
}
