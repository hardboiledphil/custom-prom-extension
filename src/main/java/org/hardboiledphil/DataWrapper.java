package org.hardboiledphil;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@ApplicationScoped
public class DataWrapper {

    private final List<DataThing> dataThingMap = new ArrayList<>();

    public void addEntry(final DataThing dataThing) {
        dataThingMap.add(dataThing);
    }

    public void removeEntry(final DataThing dataThing) {
        dataThingMap.remove(dataThing);
    }

}
