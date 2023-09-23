import java.util.*;
import java.util.stream.Collectors;

public class CentralizedMutualExclusion extends Thread {
    private final int KILL_COORDENATOR = 60000;
    private final int CREATE_PROCESS = 40000;

    private List<Process> processList;

    private Queue<ResourceRequisition> resourceRequisitions;

    public CentralizedMutualExclusion() {
        this.processList = new ArrayList<>();
        this.resourceRequisitions = new LinkedList<>();
    }

    private void killCoordenator() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(KILL_COORDENATOR);
                    setProcessList(getProcessList().stream().filter(Process::isNotCoordenator).collect(Collectors.toList()));
                    setResourceRequisitions(new LinkedList<>());
                    setNewCoordenator();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setNewCoordenator() {
        getProcessList().stream().map(Process::getId).max();
    }

    private void createProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_PROCESS);
                    addProcess(new Process(createProcessId()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private int createProcessId() {
        Random generator = new Random();
        int newID;
        do {
            newID = generator.nextInt(1000);
        } while (isInvalidId(newID));
        return newID;
    }

    private boolean isInvalidId(int id) {
        return id == 0 || getProcessList().stream().anyMatch(p -> p.getId() == id);
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public void addProcess(Process process) {
        this.getProcessList().add(process);
    }

    public Queue<ResourceRequisition> getResourceRequisitions() {
        return resourceRequisitions;
    }

    public void setResourceRequisitions(Queue<ResourceRequisition> resourceRequisitions) {
        this.resourceRequisitions = resourceRequisitions;
    }
}
