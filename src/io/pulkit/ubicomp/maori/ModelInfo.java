package io.pulkit.ubicomp.maori;

public class ModelInfo {
    private String name;
    private String version;
    private boolean active;

    public ModelInfo(String name, String version, boolean active) {
        this.name = name;
        this.version = version;
        this.active = active;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelInfo modelInfo = (ModelInfo) o;

        if (name != null ? !name.equals(modelInfo.name) : modelInfo.name != null) return false;
        if (version != null ? !version.equals(modelInfo.version) : modelInfo.version != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ModelInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", active=" + active +
                '}';
    }
}
