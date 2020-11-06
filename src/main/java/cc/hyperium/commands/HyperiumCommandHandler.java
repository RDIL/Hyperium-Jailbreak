/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.SendChatMessageEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandBase;
import net.minecraft.util.EnumChatFormatting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The class that controls the command system.
 */
public class HyperiumCommandHandler {
    private final Set<String> disabledCommands = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    /**
     * A {@link java.util.Map} of all the registered commands. Please don't modify it.
     */
    public final Map<String, BaseCommand> commands = new HashMap<>();
    private final GeneralChatHandler chatHandler;
    private final Minecraft mc;
    private String[] latestAutoComplete;

    /**
     * A constructor that initializes the system.
     */
    public HyperiumCommandHandler() {
        this.mc = Minecraft.getMinecraft();
        this.chatHandler = GeneralChatHandler.instance();
        loadDisabledCommands();
    }

    @InvokeEvent
    public void onChat(SendChatMessageEvent event) {
        final String chatLine = event.getMessage();
        // Attempt to execute command if necessary
        if (chatLine.startsWith("/") && chatLine.length() > 1 && executeCommand(chatLine)) {
            // It is one of our commands, we'll cancel the event so it isn't
            // sent to the server, and we'll close the currently opened gui
            this.mc.displayGuiScreen(null);
            event.setCancelled(true);
        }
    }

    /**
     * Executes the specified command.
     *
     * @param command The command to execute.
     * @return If the command was executed.
     */
    public boolean executeCommand(String command) {
        final String commandLine = command.startsWith("/") ? command.substring(1) : command;
        String commandName;
        String[] args = new String[]{};

        // Check if arguments are provided.
        if (commandLine.contains(" ")) {
            String[] syntax = commandLine.split(" ");
            commandName = syntax[0].toLowerCase();
            args = Arrays.copyOfRange(syntax, 1, syntax.length);
        } else {
            commandName = commandLine.toLowerCase();
        }

        // Disabled commands will be ignored
        if (isCommandDisabled(commandName)) {
            return false;
        }

        // Loop through our commands, if the identifier matches the expected command, active the base
        String[] finalArgs = args;
        return this.commands.entrySet().stream().filter(e -> commandName.equals(e.getKey()) && !e.getValue().tabOnly()).findFirst().map(e -> {
            final BaseCommand baseCommand = e.getValue();

            try {
                baseCommand.onExecute(finalArgs);
            } catch (CommandUsageException usageEx) {
                // Throw a UsageException to trigger
                this.chatHandler.sendMessage(ChatColor.RED + baseCommand.getUsage(), false);
            } catch (Exception exception) {
                exception.printStackTrace();
                this.chatHandler.sendMessage(ChatColor.RED + "An internal error occurred whilst performing this command", false);
                return false;
            }

            return true;
        }).orElse(false);
    }

    /**
     * Registers a command.
     *
     * @param command The command to register.
     */
    public void registerCommand(BaseCommand command) {
        this.commands.put(command.getName(), command);

        if (command.getCommandAliases() != null && !command.getCommandAliases().isEmpty()) {
            for (String alias : command.getCommandAliases()) {
                this.commands.put(alias, command);
            }
        }
    }

    /**
     * Returns if the specified command is disabled or not.
     *
     * @param input The command to check.
     * @return If the command is disabled.
     */
    public boolean isCommandDisabled(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty() ||
            input.equalsIgnoreCase("disablecommand") || input.equalsIgnoreCase("hyperium")) {
            return false;
        }

        return this.disabledCommands.contains(input.trim());
    }

    /**
     * Adds or removes a command to the list of disabled commands.
     *
     * @param input The command to add or remove.
     * @return If the command is disabled after the operation is performed.
     */
    public boolean addOrRemoveCommand(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty() || input.equalsIgnoreCase("disablecommand") || input.equalsIgnoreCase("hyperium")) {
            return false;
        }
        input = input.trim();
        if (isCommandDisabled(input)) {
            this.disabledCommands.remove(input);
            return false;
        } else {
            this.disabledCommands.add(input);
            return true;
        }
    }

    /**
     * Updates autocompletion context.
     *
     * @param leftOfCursor The text to the left of the keyboard cursor.
     */
    public void autoComplete(String leftOfCursor) {
        latestAutoComplete = null;
        if (leftOfCursor.length() == 0)
            return;
        if (leftOfCursor.charAt(0) == '/') {
            leftOfCursor = leftOfCursor.substring(1);
            if (mc.currentScreen instanceof GuiChat) {
                List<String> completions = getTabCompletionOptions(leftOfCursor);
                if (completions != null && !completions.isEmpty()) {
                    if (leftOfCursor.indexOf(' ') == -1) {
                        for (int i = 0; i < completions.size(); i++) {
                            completions.set(i, EnumChatFormatting.GRAY + "/" + completions.get(i) + EnumChatFormatting.RESET);
                        }
                    }
                    Collections.sort(completions);
                    latestAutoComplete = completions.toArray(new String[0]);
                }
            }
        }
    }

    private List<String> getTabCompletionOptions(String input) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];
        if (astring.length == 1) {
            List<String> list = Lists.newArrayList();

            for (Entry<String, BaseCommand> entry : this.commands.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey())) {
                    list.add(entry.getKey());
                }
            }

            return list;
        } else {
            BaseCommand command = this.commands.get(s);

            if (command != null) {
                return command.onTabComplete(dropFirstString(astring));
            }

            return null;
        }
    }

    private String[] dropFirstString(String[] input) {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    /**
     * Get the latest autocompleted strings.
     *
     * @return The latest autocompleted strings.
     */
    public String[] getLatestAutoComplete() {
        return latestAutoComplete;
    }

    /**
     * Clears all registered commands.
     */
    public void clear() {
        this.commands.clear();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadDisabledCommands() {
        File disabledCommandFile = new File(Hyperium.folder, "disabledcommands.txt");

        try {
            if (!disabledCommandFile.getParentFile().exists() && !disabledCommandFile.getParentFile().mkdirs()) {
                return;
            }

            if (!disabledCommandFile.exists()) {
                disabledCommandFile.createNewFile();
                return;
            }

            FileReader fileReader = new FileReader(disabledCommandFile);
            BufferedReader reader = new BufferedReader(fileReader);

            this.disabledCommands.addAll(reader.lines().collect(Collectors.toList()));

            reader.close();
            fileReader.close();
        } catch (IOException ignored) {}

        disabledCommands.add("l");
        disabledCommands.add("lobby");
        disabledCommands.add("hub");
        disabledCommands.add("spawn");
    }

    /**
     * Saves the list of disabled commands to the configuration file.
     */
    public void saveDisabledCommands() {
        File disabledCommandFile = new File(Hyperium.folder, "disabledcommands.txt");

        try {
            if (!disabledCommandFile.getParentFile().exists() && !disabledCommandFile.getParentFile().mkdirs()) return;
            if (!disabledCommandFile.exists() && !disabledCommandFile.createNewFile()) return;

            FileWriter fileWriter = new FileWriter(disabledCommandFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (String s : this.disabledCommands) {
                writer.write(s + System.lineSeparator());
            }

            writer.close();
            fileWriter.close();
        } catch (IOException ignored) {}
    }
}
