package engine;

import data.Explorer;

public class ExplorerFactory {

    public static Explorer constructExplorer(int explorer_type){
        Explorer explorer =null;
        switch (explorer_type){
            case Explorer.REACTIVE_EXPLORER :
                explorer = new Explorer(Explorer.REACTIVE_HEALTH, Explorer.REACTIVE_STRENGTH);
                break;

            case Explorer.COGNITIVE_EXPLORER :
                explorer = new Explorer(Explorer.COGNITIVE_HEALTH, Explorer.COGNITIVE_STRENGTH);
                break;
            case Explorer.COMMUNICATIVE_EXPLORER:
                explorer = new Explorer(Explorer.COMMUNICATIVE_HEALTH, Explorer.COMMUNICATIVE_STRENGTH);
                break;

        }
        return explorer;

    }
}
