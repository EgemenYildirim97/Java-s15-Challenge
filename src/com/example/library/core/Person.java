package com.example.library.core;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public abstract class Person {
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public abstract String getRole();
}
