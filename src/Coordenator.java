import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Coordenator {
    private Process executing;
    private Queue<Requisition> requisitionsList;

    public Coordenator() {
        this.requisitionsList = new LinkedList<>();
    }

    public boolean receiveRequisition(Requisition requisition) {
        if (nonNull(getExecuting())) {
            this.requisitionsList.add(requisition);
            System.out.println("Requisicao recusada");
            printRequisitionList();
            return false;
        }
        setExecuting(requisition.getProcess());
        getExecuting().execute(this);
        System.out.println("Requisicao aceita");
        printRequisitionList();
        return true;
    }

    public void endProcess() {
        setExecuting(null);
        if (hasNextProcess()) {
            setExecuting(getRequisitionsList().remove().getProcess());
            getExecuting().execute(this);
            System.out.println("Removendo item da Fila");
            printRequisitionList();
        }
    }

    public void printRequisitionList() {
        System.out.println("Fila: [" +
                getRequisitionsList().stream()
                        .map(Requisition::getProcess)
                        .map(Process::toString)
                        .collect(Collectors.joining(", "))
                + "]\n");
    }

    public Process getExecuting() {
        return executing;
    }

    public void setExecuting(Process executing) {
        this.executing = executing;
    }

    public Queue<Requisition> getRequisitionsList() {
        return requisitionsList;
    }

    public void setRequisitionsList(Queue<Requisition> requisitionsList) {
        this.requisitionsList = requisitionsList;
    }

    private boolean hasNextProcess() {
        return !this.requisitionsList.isEmpty();
    }
}
