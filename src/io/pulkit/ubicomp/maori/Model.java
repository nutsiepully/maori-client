package io.pulkit.ubicomp.maori;

public class Model {

    private int id;
    private String name;
    private String version;
    private boolean active;
    private byte[] payload;

    public Model(int id, String name, String version, boolean active, byte[] payload) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.active = active;
        this.payload = payload;
    }

    public Model(String name, String version, boolean active, byte[] payload) {
        this.name = name;
        this.version = version;
        this.active = active;
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isActive() {
        return active;
    }

    public byte[] getPayload() {
        return payload;
    }
}
