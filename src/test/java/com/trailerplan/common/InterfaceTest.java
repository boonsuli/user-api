package com.trailerplan.common;

public interface InterfaceTest<E> {

    void init() throws Exception;
    void cleanup() throws Exception;

    void shouldSave() throws Exception;
    void shouldUpdate() throws Exception;
    void shouldDeleteById() throws Exception;
    void shouldFindAll() throws Exception;
    void shouldFindById() throws Exception;
}
