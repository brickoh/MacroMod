package me.ethan.macro.command;

import me.ethan.macro.MacroMod;
import me.ethan.macro.macro.Macro;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class MacroCommand extends CommandBase {

    private final MacroMod macroMod;

    public MacroCommand(MacroMod macroMod) {
        this.macroMod = macroMod;
    }

    @Override
    public String getCommandName() {
        return "macro";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "§cUsage: /macro create <key> <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ChatComponentText chat;
        if(args.length == 0) {
            List<String> message = new ArrayList<>();
            message.add("");
            message.add("§5§lMacroMod Command Help;");
            message.add("");
            message.add("§r §d/macro create <key> <text>");
            message.add("§r §d/macro delete <key>");
            message.add("§r §d/macro list ");
            message.add("");
            message.forEach(msg -> sender.addChatMessage(new ChatComponentText(msg)));
            return;
        }

        String command = args[0].toLowerCase();

        switch (command) {
            case "create": {
                char key = args[1].charAt(0);
                String text = args[2];
                StringBuilder stringBuilder = new StringBuilder();
                Macro macro = macroMod.getMacroManager().findMacroByKey(key);
                for (int i = 2; i < args.length; i++) {
                    stringBuilder.append(args[i]);
                    if (i < args.length - 1) {
                        stringBuilder.append(" ");
                    }
                }
                text = stringBuilder.toString();
                if (macro == null) {
                    chat = new ChatComponentText("§dMacro has been created to key §f'" + key + "' §dwill run §f'" + text + "' §dupon key press!");
                    macro = new Macro(key, text);
                    macro.create();
                    sender.addChatMessage(chat);
                    return;
                }
                chat = new ChatComponentText("§dTheres already a macro bind to §f'" + key + "' §dit runs §f'" + macro.getText() + "'§d!");
                sender.addChatMessage(chat);
            }
            case "delete": {
                char key = args[1].charAt(0);
                Macro macro = macroMod.getMacroManager().findMacroByKey(key);
                if (macro != null) {
                    chat = new ChatComponentText("§dMacro has been deleted from key §f'" + key + "'§!");
                    macro.delete();
                    sender.addChatMessage(chat);
                    return;
                }
                chat = new ChatComponentText("§dNo macro found bound to §f'" + key + "'§d!");
                sender.addChatMessage(chat);
            }
            case "list": {
                List<String> message = new ArrayList<>();
                message.add("");
                message.add("§5§lMacros:");
                message.add("");
                for(Macro macros : MacroMod.getInstance().getMacroManager().getMacros()) {
                    message.add("§r §7- §d" + macros.getKey() + ": §f" + macros.getText());
                }
                message.add("");
                message.forEach(msg -> sender.addChatMessage(new ChatComponentText(msg)));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
