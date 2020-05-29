package com.simulation.dice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = Sequence.COLLECTION_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sequence {

    public static final String COLLECTION_NAME = "sequences";

    @Id
    private String id;

    @Field(value = "sequence")
    private Long current;

}