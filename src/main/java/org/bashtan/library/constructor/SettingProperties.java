package org.bashtan.library.constructor;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@ToString

public class SettingProperties implements Serializable {
    private String url;
    private String username;
    private String password;
}
