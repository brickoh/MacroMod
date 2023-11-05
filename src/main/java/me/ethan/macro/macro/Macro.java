package me.ethan.macro.macro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ethan.macro.MacroMod;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Macro {

    private static final File macrosFile = new File(Loader.instance().getConfigDir(), "macros.json");

    private final char key;
    private final String text;

    public Macro(char key, String text) {
        this.key = key;
        this.text = text;
    }

    public void create() {
        try {
            JsonObject macrosData;
            if (Loader.instance().getConfigDir().exists()) {
                String fileContent = new String(Files.readAllBytes(macrosFile.toPath()));
                macrosData = new JsonParser().parse(fileContent).getAsJsonObject();
            } else {
                macrosData = new JsonObject();
            }

            macrosData.addProperty(String.valueOf(key), text);
            Files.write(macrosFile.toPath(), macrosData.toString().getBytes());
            MacroMod.getInstance().getMacroManager().addMacroToCache(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void delete() {
        try {
            JsonObject macrosData;
            if (macrosFile.exists()) {
                String fileContent = new String(Files.readAllBytes(macrosFile.toPath()));
                macrosData = new JsonParser().parse(fileContent).getAsJsonObject();
                if (macrosData.has(String.valueOf(key))) {
                    macrosData.remove(String.valueOf(key));
                    Files.write(macrosFile.toPath(), macrosData.toString().getBytes());
                    MacroMod.getInstance().getMacroManager().removeMacroFromCache(this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public char getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}
