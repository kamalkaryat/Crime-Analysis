package com.k2dev.ca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name {
    @Column(nullable = false)
    @NotBlank(message = "First Name can't be empty")
    private String firstName;
    private String middleName;
    private String lastName;
}
