public class Process {

    private final int PROCESS_RESOURCE_MIN = 5000;
    private final int PROCESS_RESOURCE_MAX = 15000;
    private final int REQUEST_RESOURCE_MIN = 10000;
    private final int REQUEST_RESOURCE_MAX = 25000;
    private long id;
    private boolean isCoordenator;

    private Resource resource;

    public Process(int id) {
        setId(id);
        setCoordenator(false);
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCoordenator() {
        return isCoordenator;
    }

    public boolean isNotCoordenator() {
        return !isCoordenator;
    }

    public void setCoordenator(boolean coordenator) {
        isCoordenator = coordenator;
    }

    public void setCoordenator() {
        setCoordenator(true);
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        new Thread(() -> {
            try {
                this.resource = resource;
                Thread.sleep(getRandomTime(PROCESS_RESOURCE_MAX, PROCESS_RESOURCE_MIN));
                this.resource = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendResourceRequisition(Process coordenator) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(getRandomTime(REQUEST_RESOURCE_MAX, REQUEST_RESOURCE_MIN));

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private int getRandomTime(int upper, int lower) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }

    public ProcessRequisition createRequisition() {
        return new ProcessRequisition((int) getId());
    }

    public void receiveRequisition(ProcessRequisition processRequisition) {
        processRequisition.setId((int) Math.max(processRequisition.getId(), getId()));
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }
}
