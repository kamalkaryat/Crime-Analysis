package com.k2dev.ca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crime {
    @Id
    @NotNull
    private String crimeId;
    @NotNull
    @NotEmpty(message = "Crime can't be empty")
    private String crimeName;
    @Size(min = 0, max = 30,message = "Description length must be less than 31")
    private String description;
    private String criminal;
}
