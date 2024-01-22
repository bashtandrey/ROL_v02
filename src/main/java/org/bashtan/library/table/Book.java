package org.bashtan.library.table;

import jakarta.persistence.*;
import lombok.*;
import org.bashtan.library.constants.BookTC;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder

@Entity

@Table(name = BookTC.TABLE_NAME)
public class Book {
    @Id
    @GeneratedValue()
    @Column
    private long id;
    @Column(name = BookTC.NAME_BOOK, nullable = false, length = BookTC.LENGTH_NAME_BOOK)
    private String nameBook;

    @Column(name = BookTC.PUBLISHING_YEAR,nullable = false,length = BookTC.LENGTH_PUBLISHING_YEAR)
    private String publishingYear;

    @Column(name = BookTC.PUBLISHING_HOUSE, length = BookTC.LENGTH_PUBLISHING_HOUSE)
    private String publishingHouse;

    @Column(name = BookTC.DESCRIPTION, length = BookTC.LENGTH_DESCRIPTION)
    private String description;

    @Column(name = BookTC.SERIAL, nullable = false, unique = true, length = BookTC.LENGTH_SERIAL)
    private String serial;

    @ManyToOne()
    private People people;


    @Override
    public String toString() {
        return "Serial : " + serial + '\n' +
                "Name Book : " + nameBook + '\n' +
                "Publishing Year: " + publishingYear + '\n' +
                "Publishing House: " + publishingHouse;
    }
}
