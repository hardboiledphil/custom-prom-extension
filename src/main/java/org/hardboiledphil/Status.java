package org.hardboiledphil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum Status {
    WAITING("WAITING"),
    PROCESSING("PROCESSING");

    private final String name;
}
