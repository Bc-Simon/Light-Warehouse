package com.simon.model;

import java.io.Serializable;
import java.util.Objects;

public class Sku implements Serializable {

    private String location;
    private String code;

    public Sku() {
    }

    public Sku(String location, String code) {
        this.location = location;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return location.equals(sku.location) &&
                code.equals(sku.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, code);
    }
}
