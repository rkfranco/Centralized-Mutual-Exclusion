import java.text.MessageFormat;

public class Requisition {
    private final Process process;
    private final Resource resource;

    public Requisition(Process process, Resource resource) {
        this.process = process;
        this.resource = resource;
    }

    public Process getProcess() {
        return process;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} {1}", this.getProcess().toString(), this.getResource().toString());
    }
}
