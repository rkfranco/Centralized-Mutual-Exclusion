import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CentralizedMutualExclusion extends Thread {
    private final int KILL_COORDENATOR = 60000;
    private final int CREATE_PROCESS = 40000;


    private List<Process> processList;
    private Coordenator coordenator;

    public CentralizedMutualExclusion() {
        this.processList = new ArrayList<>();
        setNewCoordenator();
    }

    private void killCoordenator() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(KILL_COORDENATOR);
                    setNewCoordenator();
                    System.out.println("Coordenador eliminado - Toda a execucao do processo atual foi perdida");
                    System.out.println();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void createProcess() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CREATE_PROCESS);
                    Process process = new Process(createProcessId(), getCoordenator());
                    process.sendRequisition(getCoordenator());
                    addProcess(process);
                    System.out.println("Processo criado");
                    printProcesses();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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

    public Coordenator getCoordenator() {
        return this.coordenator;
    }

    private void setNewCoordenator() {
        this.coordenator = new Coordenator();
        getProcessList().forEach(p -> p.setCoordenator(getCoordenator()));
    }

    private void printProcesses() {
        String msg = getProcessList().stream().map(Process::toString).collect(Collectors.joining(", "));
        System.out.println("Processos: [" + msg + "]\n");
    }

    @Override
    public void run() {
        this.killCoordenator();
        this.createProcess();
    }
}
