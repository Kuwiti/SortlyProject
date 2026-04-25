//Student Names : Suhaim Alhajeri, Mishari Almulaifi , Abdullah Abdullah
//Student IDs : 2231121808, 2231167795, 2231153435

public class Task {
    private String name;
    private int deadline;
    private int priority;
    private int exec;

    public Task(String name, int d, int p, int e) {
        this.name = name;
        this.deadline = d;
        this.priority = p;
        this.exec = e;
    }

    public void print(int id) {
        System.out.println("ID=" + id + " | " + name +
                " | Deadline: " + deadline +
                " | Priority: " + priority +
                " | Exec: " + exec);
    }

    public String getName() { return name; }
    public int getDeadline() { return deadline; }
    public int getPriority() { return priority; }
    public int getExecutionTime() { return exec; }
}
