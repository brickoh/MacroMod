package me.ethan.macro;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.ethan.macro.command.MacroCommand;
import me.ethan.macro.macro.Macro;
import me.ethan.macro.macro.MacroManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Mod(modid = "MacroMod", useMetadata=true)
public class MacroMod {

    private static MacroMod instance;

    private MacroManager macroManager;

    @Mod.EventHandler
    public void preInt(FMLPreInitializationEvent event) {
        instance = this;
        macroManager = new MacroManager();
        File macros = new File(Loader.instance().getConfigDir(), "macros.json");
        Gson gson = new Gson();

        if (!macros.exists()) {
            try {
                JsonObject initialData = new JsonObject();
                String initialDataJson = gson.toJson(initialData);
                Files.write(macros.toPath(), initialDataJson.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(macros.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject macrosData = gson.fromJson(fileContent, JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : macrosData.entrySet()) {
            String key = entry.getKey();
            String action = entry.getValue().getAsString();

            Macro macro = new Macro(key.charAt(0), action);
            macroManager.addMacroToCache(macro);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new MacroCommand(this));
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
    }


    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus ) {
            for (int keyCode = 0; keyCode < Keyboard.KEYBOARD_SIZE; keyCode++) {
                if (Keyboard.isKeyDown(keyCode)) {
                    char keyChar = Keyboard.getEventCharacter();
                    Macro macro = macroManager.findMacroByKey(keyChar);
                    if(macro == null) return;
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(macro.getText());
                }
            }
        }
    }

    public static MacroMod getInstance() {
        return instance;
    }

    public MacroManager getMacroManager() {
        return macroManager;
    }
}