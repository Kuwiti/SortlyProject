//Student Names : Suhaim Alhajeri, Mishari Almulaifi , Abdullah Abdullah
//Student IDs : 2231121808, 2231167795, 2231153435

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class SortlyScheduler {

    private static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        TaskManager heapManager = new TaskManager(2000);
        BasicTaskManager arrayManager = new BasicTaskManager(2000);

        loadTasksFromFile(heapManager, arrayManager);

        while (choice != 0) {
            printMenu();
            System.out.print("Choose option: ");

            try {
                if (!input.hasNextInt()) {
                    System.out.println("\nPlease enter a number (0-8).");
                    input.next();
                    continue;
                }

                choice = input.nextInt();

                switch (choice) {
                    case 1:
                        addTask(input, heapManager, arrayManager);
                        break;
                    case 2:
                        heapManager.viewTasks();
                        break;
                    case 3:
                        heapManager.scheduleTasks(TaskHeap.EDF);
                        break;
                    case 4:
                        heapManager.scheduleTasks(TaskHeap.SJF);
                        break;
                    case 5:
                        heapManager.scheduleTasks(TaskHeap.SMART);
                        break;
                    case 6:
                        runPerformanceTest(heapManager, arrayManager);
                        break;
                    case 7:
                        loadSampleTasks(heapManager, arrayManager);
                        break;
                    case 8:
                        handleRemoval(input, heapManager, arrayManager);
                        break;
                    case 0:
                        saveTasksToFile(heapManager);
                        System.out.println("Program ended.");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred.");
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
        System.out.println("7. Load Sample Tasks (20000 random tasks)");
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
            saveTasksToFile(h);

        } else if (choice == 2) {
            h.resetAll();
            a.resetAll();
            saveTasksToFile(h);
            System.out.println("All tasks cleared.");
        }
    }

    private static void runPerformanceTest(TaskManager heapM, BasicTaskManager arrayM) {
        int[] testSizes = {1000, 5000, 10000, 20000};

        System.out.println("\n--- PERFORMANCE ANALYSIS ---");

        for (int size : testSizes) {
            long totalH = 0;
            long totalA = 0;

            for (int t = 0; t < 5; t++) {
                long s = System.nanoTime();
                heapM.simulateScheduling(size);
                totalH += (System.nanoTime() - s);

                s = System.nanoTime();
                arrayM.simulateScheduling(size);
                totalA += (System.nanoTime() - s);
            }

            System.out.println(size + " -> Heap: " + (totalH / 5) + " ns | Array: " + (totalA / 5) + " ns");
        }
    }

    private static void addTask(Scanner input, TaskManager h, BasicTaskManager a) {
        input.nextLine();

        System.out.print("Task Name: ");
        String name = input.nextLine();

        System.out.print("Deadline: ");
        int d = input.nextInt();

        System.out.print("Priority (1-5): ");
        int p = input.nextInt();

        System.out.print("Exec Time: ");
        int e = input.nextInt();

        Task t = new Task(name, d, p, e);
        h.addTask(t);
        a.addTask(t);
        saveTasksToFile(h);

        System.out.println("Task added.");
    }

    private static void loadSampleTasks(TaskManager h, BasicTaskManager a) {
        for (int i = 0; i < 20000; i++) {
            Task t = new Task("T" + i,
                    (int)(Math.random() * 50),
                    (int)(Math.random() * 5) + 1,
                    (int)(Math.random() * 10) + 1);

            h.addTask(t);
            a.addTask(t);
        }
        saveTasksToFile(h);
    }

    private static void saveTasksToFile(TaskManager h) {
        try {
            PrintWriter writer = new PrintWriter(FILE_NAME);

            for (int i = 0; i < h.getSize(); i++) {
                Task t = h.getTask(i);
                writer.println(t.getName() + "," + t.getDeadline() + "," +
                        t.getPriority() + "," + t.getExecutionTime());
            }

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Save error.");
        }
    }

    private static void loadTasksFromFile(TaskManager h, BasicTaskManager a) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String[] p = reader.nextLine().split(",");
                Task t = new Task(p[0], Integer.parseInt(p[1]),
                        Integer.parseInt(p[2]), Integer.parseInt(p[3]));

                h.addTask(t);
                a.addTask(t);
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Load error.");
        }
    }
}
