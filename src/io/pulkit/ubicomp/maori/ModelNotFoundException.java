package io.pulkit.ubicomp.maori;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String name) {
        super("Model not found : " + name);
    }
}
