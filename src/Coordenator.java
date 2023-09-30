import java.util.*;

public class Coordenator {
    private final HashMap<Resource, Queue<Requisition>> resourcesRequisitions;

    public Coordenator() {
        this.resourcesRequisitions = new HashMap<>();

        // just for testing
        this.resourcesRequisitions.put(new Resource(1), new LinkedList<>());
        this.resourcesRequisitions.put(new Resource(2), new LinkedList<>());
    }

    public boolean requestResourceAccess(Requisition requisition) {
        Resource resource = requisition.getResource();

        if (resource.isBeingAccessed()) {
            System.out.printf("%s - Requisição de acesso do processo ao recurso recusada\n", requisition);
            this.resourcesRequisitions.get(resource).add(requisition);
            return false;
        }

        System.out.printf("%s - Requisição de acesso do processo ao recurso aceita\n", requisition);
        resource.setBeingAccessed(true);
        requisition.getProcess().executeProcessing(this, resource);
        return true;
    }

    public void releaseResource(Requisition requisition) {
        Resource resource = requisition.getResource();
        resource.setBeingAccessed(false);
        System.out.printf("%s - Recurso liberado pelo processo\n", requisition);

        Queue<Requisition> requisitionQueue = this.resourcesRequisitions.get(resource);
        if (!requisitionQueue.isEmpty()) {
            Requisition nextRequisition = requisitionQueue.remove();
            System.out.printf("%s - Próximo processo na fila de acesso ao recurso\n", requisition);
            resource.setBeingAccessed(true);
            nextRequisition.getProcess().executeProcessing(this, resource);
        }
    }

    public Resource getRandomResource() {
        int randomIndex = new Random().nextInt(this.resourcesRequisitions.size());
        Resource[] keysArray = this.resourcesRequisitions.keySet().toArray(new Resource[0]);
        return keysArray[randomIndex];
    }
}
