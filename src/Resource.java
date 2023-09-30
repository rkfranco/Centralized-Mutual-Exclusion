import java.text.MessageFormat;

public class Resource {
    private final int id;
     private boolean isBeingAccessed;

    public Resource(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isBeingAccessed() {
        return isBeingAccessed;
    }

    public void setBeingAccessed(boolean beingAccessed) {
        this.isBeingAccessed = beingAccessed;
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Resource))
            return false;

        if (this == obj)
            return true;

        Resource resource = (Resource) obj;
        return resource.getId() == this.getId();
    }

    @Override
    public String toString() {
        return MessageFormat.format("Recurso: [{0}]", String.valueOf(this.getId()));
    }
}
