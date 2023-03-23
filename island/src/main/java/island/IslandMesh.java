package island;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class IslandMesh {
    private final Structs.Mesh inputMesh;
    private Mode mode;

    public IslandMesh(Structs.Mesh input, Mode mode){
        this.inputMesh = input;
        this.mode = mode;
    }
}