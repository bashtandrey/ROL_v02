package org.bashtan.library.table;

import jakarta.persistence.*;
import lombok.*;
import org.bashtan.library.constants.UserTC;
import org.bashtan.library.constructor.Level;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = UserTC.TABLE_NAME)
public class User {

    @Id
    @GeneratedValue()
    private long id;
    @Column(name = UserTC.LOGIN, nullable = false, unique = true, length = UserTC.LengthLogin)
    private String login;

    @Column(name = UserTC.PASSWORD, nullable = false)
    private String password;

    @Column(name = UserTC.LEVEL, nullable = false, length = UserTC.LENGTH_LEVEL)
    private Level level;

    @OneToOne(fetch = FetchType.EAGER)
    private People people;

    @Override
    public String toString() {
        return "Login : " + login + '\n' +
                "Password : " + password + '\n' +
                "Level : " + level;
    }
}