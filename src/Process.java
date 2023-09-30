import java.text.MessageFormat;

public class Process {

    private final int PROCESS_RESOURCE_MIN = 5000;
    private final int PROCESS_RESOURCE_MAX = 15000;
    private final int REQUEST_RESOURCE_MIN = 10000;
    private final int REQUEST_RESOURCE_MAX = 25000;

    private final int id;

    public Process(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void executeProcessing(Coordenator coordenator, Resource resource) {
        try {
            int processingDuration = getRandomValueBetween(PROCESS_RESOURCE_MIN, PROCESS_RESOURCE_MAX);
            System.out.printf("%s %s - Processo iniciou execução do recurso - Duração %s seg\n", this, resource, (processingDuration / 1000));
            Thread.sleep(processingDuration);
            System.out.printf("%s %s - Processo finalizou execução do recurso - Duração %s seg\n", this, resource, (processingDuration / 1000));
            coordenator.releaseResource(new Requisition(this, resource));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResourceRequisition(Coordenator coordenator) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(getRandomValueBetween(REQUEST_RESOURCE_MIN, REQUEST_RESOURCE_MAX));
                    Resource resource = coordenator.getRandomResource();
                    System.out.println("\n---> Processo enviando requisição de acesso ao recurso - " + this + " " + resource);
                    coordenator.requestResourceAccess(new Requisition(this, resource));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private int getRandomValueBetween(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Processo: [{0}]", String.valueOf(this.getId()));
    }
}
