package modelos;

public class Usuarios {
    private int id;
    private String name;
    private String email;
    private String genero;
    private String estado;

    public Usuarios() {
    }

    public Usuarios(int id, String name, String email, String genero, String estado) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.genero = genero;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", genero='" + genero + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
