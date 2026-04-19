import java.util.Scanner;
import java.util.Arrays;

public class SortlyScheduler {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        TaskManager heapManager = new TaskManager(2000);
        BasicTaskManager arrayManager = new BasicTaskManager(2000);

        while (choice != 0) {
            printMenu();
            System.out.print("Choose option: ");

            try {
                if (!input.hasNextInt()) {
                    System.out.println("\n[!] Error: Please enter a number (0-8).");
                    input.next(); 
                    continue;
                }
                choice = input.nextInt();

                switch (choice) {
                    case 1: addTask(input, heapManager, arrayManager); break;
                    case 2: heapManager.viewTasks(); break;
                    case 3: heapManager.scheduleTasks(TaskHeap.EDF); break;
                    case 4: heapManager.scheduleTasks(TaskHeap.SJF); break;
                    case 5: heapManager.scheduleTasks(TaskHeap.SMART); break;
                    case 6: runPerformanceTest(heapManager, arrayManager); break;
                    case 7: loadSampleTasks(heapManager, arrayManager); break;
                    case 8: handleRemoval(input, heapManager, arrayManager); break;
                    case 0: System.out.println("Program ended."); break;
                    default: System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                input.nextLine(); 
            }
        }
        input.close();
    }

    private static void printMenu() {
        System.out.println("\n--- SORTLY TASK SCHEDULER ---");
        System.out.println("1. Add Task Manually");
        System.out.println("2. View All Tasks");
        System.out.println("3. Schedule: EDF (Deadline)");
        System.out.println("4. Schedule: SJF (Execution Time)");
        System.out.println("5. Schedule: Smart (Priority)");
        System.out.println("6. Run Performance Comparison");
        System.out.println("7. Load Sample Tasks (100)");
        System.out.println("8. Remove / Reset Tasks");
        System.out.println("0. Exit");
    }

    private static void handleRemoval(Scanner input, TaskManager h, BasicTaskManager a) {
        System.out.println("\n--- Removal Options ---");
        System.out.println("1. Remove specific task by ID");
        System.out.println("2. Reset ALL tasks to 0");
        System.out.print("Choice: ");
        int choice = input.nextInt();

        if (choice == 1) {
            System.out.print("Enter Task ID to remove: ");
            int id = input.nextInt();
            h.removeTaskById(id);
            a.removeTaskById(id);
        } else if (choice == 2) {
            h.resetAll();
            a.resetAll();
            System.out.println("All tasks cleared. ID counter is back to 1.");
        }
    }

    private static void runPerformanceTest(TaskManager heapM, BasicTaskManager arrayM) {
        int[] testSizes = {1000, 5000, 10000, 20000};

        System.out.println("\n--- PERFORMANCE ANALYSIS (Avg of 5 trials) ---");

        System.out.println("Size\tHeap (O(N log N))\tArray (O(N^2))");



        for (int size : testSizes) {

            long totalH = 0, totalA = 0;

            for (int t = 0; t < 5; t++) {

                long s = System.nanoTime();

                heapM.simulateScheduling(size);

                totalH += (System.nanoTime() - s);



                s = System.nanoTime();

                arrayM.simulateScheduling(size);

                totalA += (System.nanoTime() - s);

            }

            System.out.println(size + "\t" + (totalH / 5) + " ns\t\t" + (totalA / 5) + " ns");

        }

    }

    private static void addTask(Scanner input, TaskManager h, BasicTaskManager a) {
        input.nextLine(); 
        System.out.print("Task Name: "); String name = input.nextLine();
        System.out.print("Deadline (By Days): "); int d = input.nextInt();
        System.out.print("Priority (1-5): "); int p = input.nextInt();
        System.out.print("Exec Time (in Hours): "); int e = input.nextInt();
        
        Task t = new Task(name, d, p, e);
        h.addTask(t); 
        a.addTask(t);
        System.out.println("Task added successfully.");
    }

    private static void loadSampleTasks(TaskManager h, BasicTaskManager a) {
        for (int i = 0; i < 100; i++) {
            Task t = new Task("T" + i, (int)(Math.random()*50), (int)(Math.random()*5)+1, (int)(Math.random()*10)+1);
            h.addTask(t); 
            a.addTask(t);
        }
        System.out.println("100 sample tasks loaded.");
    }
}

class TaskManager {
    private Task[] tasks;
    private int size;

    public TaskManager(int cap) { tasks = new Task[cap]; size = 0; }
    
    public void addTask(Task t) { 
        if (size == tasks.length) tasks = Arrays.copyOf(tasks, tasks.length * 2);
        tasks[size++] = t; 
    }
    
    public void resetAll() { size = 0; }

    public void removeTaskById(int id) {
        int indexToRemove = id - 1; // Convert ID back to array index

    if (indexToRemove < 0 || indexToRemove >= size) {
        System.out.println("Invalid ID.");
        return;
    }

    for (int j = indexToRemove; j < size - 1; j++) {
        tasks[j] = tasks[j+1];
    }
    
    size--;
    System.out.println("Task removed. ");
}
        
    

    public void viewTasks() {
        if (size == 0) {
        System.out.println("List is empty.");
        return;
    }
    for (int i = 0; i < size; i++) {
        if (size == 0) {
        System.out.println("List is empty.");
        return;
        }else{
        tasks[i].print(i + 1); 
    }
    }
}

    public void scheduleTasks(int mode) {
        if (size == 0) { System.out.println("No tasks to schedule."); return; }
        TaskHeap heap = new TaskHeap(size, mode);
        for (int i = 0; i < size; i++) heap.insert(tasks[i]);
        
        System.out.println("\n--- Executing Schedule ---");
        while (!heap.isEmpty()) {
            Task t = heap.removeMin();
            System.out.println("Running: " + t.getName() + " [DeadLine :" + t.getDeadline() + " Days | P:" + t.getPriority() + "]" + 
            " for " + t.getExecutionTime() + " hours.");
        }
    }

    public void simulateScheduling(int limit) {
        int currentLimit = Math.min(limit, size);
        if (currentLimit == 0) return;
        TaskHeap heap = new TaskHeap(currentLimit, TaskHeap.EDF);
        for (int i = 0; i < currentLimit; i++) heap.insert(tasks[i]);
        while (!heap.isEmpty()) heap.removeMin();
    }
}

class BasicTaskManager {
    private Task[] tasks;
    private int size;

    public BasicTaskManager(int cap) { tasks = new Task[cap]; size = 0; }
    
    public void addTask(Task t) { 
        if (size == tasks.length) tasks = Arrays.copyOf(tasks, tasks.length * 2);
        tasks[size++] = t; 
    }
    
    public void resetAll() { size = 0; }

    public void removeTaskById(int id) {
        for (int i = 0; i < size; i++) {
            if (tasks[i].getId() == id) {
                for (int j = i; j < size - 1; j++) tasks[j] = tasks[j+1];
                size--;
                return;
            }
        }
    }

    public void simulateScheduling(int limit) {
        int currentLimit = Math.min(limit, size);
        if (currentLimit == 0) return;
        Task[] copy = Arrays.copyOf(tasks, currentLimit);
        for (int i = 0; i < currentLimit - 1; i++) {
            int min = i;
            for (int j = i + 1; j < currentLimit; j++) {
                if (copy[j].getDeadline() < copy[min].getDeadline()) min = j;
            }
            Task temp = copy[min];
            copy[min] = copy[i];
            copy[i] = temp;
        }
    }
}

class Task {
  private String name;
    private int deadline, priority, exec;

    public Task(String name, int d, int p, int e) {
        this.name = name; 
        this.deadline = d; 
        this.priority = p; 
        this.exec = e;
    }

    public int getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    public void print(int displayId) { 
        System.out.println("ID=" + displayId + " | " + name + " | Deadline:" + deadline + 
                           " days | Priority:" + priority + " | Exec Time:" + exec + " hrs");
    }

    public String getName() { return name; }
    public int getDeadline() { return deadline; }
    public int getPriority() { return priority; }
    public int getExecutionTime() { return exec; }
}



class TaskHeap {
    public static final int EDF = 1, SJF = 2, SMART = 3;
    private Task[] heap;
    private int size, mode;

    public TaskHeap(int cap, int mode) { heap = new Task[cap]; size = 0; this.mode = mode; }
    public boolean isEmpty() { return size == 0; }

    public void insert(Task t) {
        if (size == heap.length) heap = Arrays.copyOf(heap, heap.length * 2);
        heap[size] = t;
        int cur = size++;
        while (cur > 0 && compare(heap[cur], heap[(cur-1)/2])) {
            swap(cur, (cur-1)/2);
            cur = (cur-1)/2;
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
        if (s != i) { swap(i, s); heapify(s); }
    }

    private boolean compare(Task a, Task b) {
        if (mode == EDF) return a.getDeadline() < b.getDeadline();
        if (mode == SJF) return a.getExecutionTime() < b.getExecutionTime();
        return a.getPriority() > b.getPriority();
    }

    private void swap(int i, int j) { Task temp = heap[i]; heap[i] = heap[j]; heap[j] = temp; }
}