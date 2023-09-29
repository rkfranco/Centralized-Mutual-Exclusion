public class Process {

    private final int PROCESS_RESOURCE_MIN = 5000;
    private final int PROCESS_RESOURCE_MAX = 15000;
    private final int REQUEST_RESOURCE_MIN = 10000;
    private final int REQUEST_RESOURCE_MAX = 25000;
    private long id;

    private Coordenator coordenator;

    public Process(int id, Coordenator coordenator) {
        setId(id);
        sendRequisition(coordenator);
    }

    public Coordenator getCoordenator() {
        return coordenator;
    }

    public void setCoordenator(Coordenator coordenator) {
        this.coordenator = coordenator;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void execute(Coordenator coordenatorParam) {
        new Thread(() -> {
            try {
                System.out.println("Iniciando processo");
                System.out.println();
                Thread.sleep(getRandomTime(PROCESS_RESOURCE_MIN, PROCESS_RESOURCE_MAX));
                System.out.println("Processo finalizado");
                System.out.println();
                coordenatorParam.endProcess();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendRequisition(Coordenator coordenator) {
        new Thread(() -> {
            Requisition requisition = new Requisition(this);
            while (true) {
                try {
                    Thread.sleep(getRandomTime(REQUEST_RESOURCE_MIN, REQUEST_RESOURCE_MAX));
                    System.out.println("Enviando requisicao");
                    System.out.println();
                    getCoordenator().receiveRequisition(requisition);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    private int getRandomTime(int upper, int lower) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }
}
