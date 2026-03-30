package edu.ccrm.domain;

public interface Persistable {
    String getUniqueId();

    default String toPersistableString() {
        return getUniqueId() + "," + getClass().getSimpleName(); // Basic example
    }

}
