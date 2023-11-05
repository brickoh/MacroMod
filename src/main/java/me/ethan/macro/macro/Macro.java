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

                // Check if the macro with the specified key exists
                if (macrosData.has(String.valueOf(key))) {
                    // Remove the macro from the JSON data
                    macrosData.remove(String.valueOf(key));

                    // Write the updated JSON data back to the file
                    Files.write(macrosFile.toPath(), macrosData.toString().getBytes());

                    // Remove the macro from the cache
                    MacroMod.getInstance().getMacroManager().removeMacroFromCache(this);
                } else {
                    // Handle the case where the macro with the specified key does not exist
                    System.out.println("Macro with key " + key + " does not exist.");
                }
            } else {
                // Handle the case where the macros file does not exist
                System.out.println("Macros file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public char getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}
