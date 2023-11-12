package org.hardboiledphil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataThing {

    private String bench;
    private String chain;
    private int    chainNumber;
    private Status status;

}

