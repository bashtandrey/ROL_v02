package org.bashtan.library.table;

import jakarta.persistence.*;
import lombok.*;
import org.bashtan.library.constants.PeopleTC;
import org.bashtan.library.constructor.Gender;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder


@Entity
@Table(name = PeopleTC.TABLE_NAME)
public class People {
    @Id
    @GeneratedValue()
    @Column
    private long id;
    @Column(name = PeopleTC.LAST_NAME,nullable = false,length = PeopleTC.LENGTH_LAST_NAME)
    private String lastName;

    @Column(name = PeopleTC.FIRST_NAME,nullable = false,length = PeopleTC.LENGTH_FIRST_NAME)
    private String firstName;

    @Column(name = PeopleTC.BIRTHDAY)
    private LocalDate birthday;

    @Column(name = PeopleTC.EMAIL,length = PeopleTC.LENGTH_EMAIL)
    private String email;

    @Column(name = PeopleTC.PHONE,length = PeopleTC.LENGTH_PHONE)
    private String phone;

    @Column(name = PeopleTC.GENDER, nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "people",fetch = FetchType.EAGER)
    private List<Book> books;


    @Override
    public String toString() {
        return  "Last Name : " + lastName + '\n' +
                "First Name : " + firstName + '\n' +
                "Birthday : " + birthday +'\n' +
                "Email : " + email + '\n' +
                "Phone : " + phone + '\n' +
                "Gender :" + gender;
    }
}
