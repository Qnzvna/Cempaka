package org.cempaka.cyclone.storage.repositories;

import java.util.Set;
import org.cempaka.cyclone.tests.Test;

public interface TestRepository
{
    Set<Test> getAll();

    Set<Test> getByName(String name);

    void putAll(Set<Test> tests);

    void delete(String id);

    Set<String> keys();
}
