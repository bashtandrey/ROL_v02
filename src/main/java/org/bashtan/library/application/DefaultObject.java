package org.bashtan.library.application;


import org.bashtan.library.table.People;

import java.time.LocalDate;

import static org.bashtan.library.constructor.Gender.MALE;

public class DefaultObject {
    final public static People PEOPLE_ADMIN = People.builder()
            .firstName("admin")
            .lastName("admin")
            .birthday(LocalDate.now())
            .gender(MALE)
            .email(null)
            .phone(null)
            .build();
}