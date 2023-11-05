package me.ethan.macro.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {

    private List<Macro> macros;

    public MacroManager() {
        this.macros = new ArrayList<>();
    }

    public void addMacroToCache(Macro macro) {
        if(macros.contains(macro)) return;
        macros.add(macro);
    }

    public void removeMacroFromCache(Macro macro) {
        if(!macros.contains(macro)) return;
        macros.remove(macro);
    }

    public Macro findMacroByKey(char key) {
        return macros
                .stream()
                .filter(macro -> macro.getKey() == key)
                .findFirst()
                .orElse(null);
    }

    public List<Macro> getMacros() {
        return macros;
    }

}
