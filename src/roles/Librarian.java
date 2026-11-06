package roles;

import services.Library;

public class Librarian {
    private final int id;
    private final String name;

    public Librarian(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void processAllRequests(Library library) {
        System.out.println("Librarian " + name + " starts working");
        while (library.hasRequests()) {
            library.processNextRequest();
        }
        System.out.println("Queue is empty, " + name + " is done");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Librarian)) return false;
        Librarian other = (Librarian) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
