import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Coordinator {
    private final Map<Resource, Queue<Requisition>> resourcesRequisitions;

    public Coordinator() {
        this.resourcesRequisitions = new ConcurrentHashMap<>();

        // just for testing
        this.resourcesRequisitions.put(new Resource(1), new ConcurrentLinkedQueue<>());
        this.resourcesRequisitions.put(new Resource(2), new ConcurrentLinkedQueue<>());
    }

    public void requestResourceAccess(Requisition requisition) {
        Resource resource = requisition.getResource();

        if (resource.isBeingAccessed()) {
            System.out.printf("%s - Requisição de acesso do processo ao recurso recusada\n", requisition);
            this.resourcesRequisitions.get(resource).add(requisition);
            printResourceQueues();
            return;
        }

        System.out.printf("%s - Requisição de acesso do processo ao recurso aceita\n", requisition);
        resource.setBeingAccessed(true);
        requisition.getProcess().executeProcessing(resource);
    }

    public void releaseResource(Requisition requisition) {
        Resource resource = requisition.getResource();
        resource.setBeingAccessed(false);
        System.out.printf("%s - Recurso liberado pelo processo\n", requisition);

        Queue<Requisition> requisitionQueue = this.resourcesRequisitions.get(resource);
        if (!requisitionQueue.isEmpty()) {
            printResourceQueues();
            Requisition nextRequisition = requisitionQueue.remove();
            System.out.printf("%s - Próximo processo na fila de acesso ao recurso\n", nextRequisition);
            resource.setBeingAccessed(true);
            nextRequisition.getProcess().executeProcessing(resource);
        }
    }

    public Resource getRandomResource() {
        int randomIndex = new Random().nextInt(this.resourcesRequisitions.size());
        Resource[] keysArray = this.resourcesRequisitions.keySet().toArray(new Resource[0]);
        return keysArray[randomIndex];
    }

    private void printResourceQueues() {
        StringBuilder sb = new StringBuilder();
        this.resourcesRequisitions.forEach((resource, requisitionQueue) -> {
            sb.append("Fila de espera do ").append(resource).append(" -> ");
            for (Requisition requisition : requisitionQueue) {
                sb.append(requisition.getProcess()).append(", ");
            }
            sb.append("\n");
        });
        System.out.println(sb);
    }
}
