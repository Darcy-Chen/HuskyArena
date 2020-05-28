/*
 * Copyright 2017-2020 The BlockyArena Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.huskycraft.blockyarena.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import net.huskycraft.blockyarena.arenas.ArenaManager;
import net.huskycraft.blockyarena.utils.KitManager;

public class CmdRemove implements CommandExecutor {

    private static final CmdRemove instance = new CmdRemove();

    /* enforce the singleton property with a private constructor */
    private CmdRemove() {}

    public static CmdRemove getInstance() {
        return instance;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String type = args.<String>getOne("type").get().toLowerCase();
        String id = args.<String>getOne("id").get().toLowerCase();
        switch (type) {

            case "arena":
                try {
                	ArenaManager.getInstance().remove(id);
                    break;
                } catch (IllegalArgumentException e) {
                    src.sendMessage(Text.of(e.getMessage()));
                    return CommandResult.empty();
                }

            case "kit":
                try {
                	KitManager.getInstance().remove(id);
                    break;
                } catch (IllegalArgumentException e) {
                    src.sendMessage(Text.of(e.getMessage()));
                    return CommandResult.empty();
                }

            default:
                src.sendMessage(Text.of(type + " is not a valid type."));
                return CommandResult.empty();
        }
        src.sendMessage(Text.of(id + " has been removed."));
        return CommandResult.success();
    }
}
