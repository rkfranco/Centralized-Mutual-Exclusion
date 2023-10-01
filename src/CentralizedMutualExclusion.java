import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CentralizedMutualExclusion extends Thread {
    private final int KILL_COORDENATOR = 60000;
    private final int CREATE_PROCESS = 40000;

    private final List<Process> processes;
    public static Coordenator coordenator;

    public CentralizedMutualExclusion() {
        this.processes = new ArrayList<>();
        setNewCoordenator();
    }

    private void killCoordenator() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(KILL_COORDENATOR);
                    System.out.println("\n---> Coordenador eliminado - Toda a execucao do processo atual foi perdida");
                    setNewCoordenator();
                    for (Process process : this.processes) {
                        process.stopCurrentProcessing();
                        process.sendResourceRequisition();
                    }
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
                    Process process = new Process(createProcessId());
                    process.sendResourceRequisition();
                    this.processes.add(process);
                    System.out.println("\n---> Criação de um novo processo");
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
        return id == 0 || this.processes.stream().anyMatch(p -> p.getId() == id);
    }

    private void setNewCoordenator() {
        coordenator = new Coordenator();
    }

    private void printProcesses() {
        String msg = this.processes.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.joining(", "));
        System.out.println("Processos: [" + msg + "]");
    }

    @Override
    public void run() {
        System.out.println("Algoritmo de exclusão mútua - Centralizado");
        this.killCoordenator();
        this.createProcess();
    }
}
