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

package io.github.mrdarcychen.commands;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import static org.spongepowered.api.command.args.GenericArguments.*;

public class CommandManager {
    private static final CommandManager INSTANCE = new CommandManager();

    private CommandManager() {
        registerCommands();
    }

    public static CommandManager getInstance() {
        return INSTANCE;
    }

    private CommandSpec rootCmd;

    // registers user commands to CommandManager

    private void registerCommands() {
        CommandSpec cmdCreate = CommandSpec.builder()
                .arguments(
                        onlyOne(string(Text.of("type"))),
                        onlyOne(string(Text.of("id"))))
                .executor(CmdCreate.getInstance())
                .permission("blockyarena.create")
                .build();

        CommandSpec cmdRemove = CommandSpec.builder()
                .arguments(
                        onlyOne(string(Text.of("type"))),
                        onlyOne(string(Text.of("id"))))
                .executor(CmdRemove.getInstance())
                .permission("blockyarena.remove")
                .build();

        CommandSpec cmdJoin = CommandSpec.builder()
                .arguments(
                        onlyOne(string(Text.of("mode"))),
                        optionalWeak(flags().valueFlag(playerOrSource(Text.of("player")), "p")
                                .buildWith(none()))
                )
                .executor(CmdJoin.getInstance())
                .build();

        CommandSpec cmdQuit = CommandSpec.builder()
                .executor(CmdQuit.getInstance())
                .build();

        CommandSpec cmdEdit = CommandSpec.builder()
                .arguments(
                        onlyOne(string(Text.of("id"))),
                        onlyOne(string(Text.of("type"))),
                        optional(onlyOne(string(Text.of("param"))))
                )
                .executor(CmdEdit.getInstance())
                .permission("blockyarena.edit")
                .build();

        CommandSpec cmdKit = CommandSpec.builder()
                .arguments(
                        onlyOne(string(Text.of("id"))),
                        optionalWeak(flags().valueFlag(playerOrSource(Text.of("player")), "p")
                                .buildWith(none()))
                )
                .executor(CmdKit.getInstance())
                .build();

        rootCmd = CommandSpec.builder()
                .child(cmdEdit, "edit")
                .child(cmdCreate, "create")
                .child(cmdRemove, "remove")
                .child(cmdJoin, "join")
                .child(cmdQuit, "quit")
                .child(cmdKit, "kit")
                .build();
    }

    public CommandSpec getCommandCallable() {
        return rootCmd;
    }
}
