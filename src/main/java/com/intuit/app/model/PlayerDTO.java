package com.intuit.app.model;

import com.intuit.app.domain.Gender;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
@AllArgsConstructor
public class PlayerDTO implements Serializable {
    Long id;
    String name;
    Gender gender;
    String email;
}
