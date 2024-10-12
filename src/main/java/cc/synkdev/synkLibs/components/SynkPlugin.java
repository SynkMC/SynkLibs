package cc.synkdev.synkLibs.components;

import java.util.Map;

public interface SynkPlugin {
    String name();
    String ver();
    String dlLink();
    String prefix();
    String lang();
    Map<String, String> langMap();
}
