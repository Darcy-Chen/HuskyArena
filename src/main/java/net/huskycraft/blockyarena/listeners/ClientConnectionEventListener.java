/*
 * This file is part of BlockyArena, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 HuskyCraft <https://www.huskycraft.net>
 * Copyright (c) 2018 Darcy-Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.huskycraft.blockyarena.listeners;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import net.huskycraft.blockyarena.BlockyArena;
import net.huskycraft.blockyarena.games.GamersManager;
import net.huskycraft.blockyarena.utils.Gamer;

public class ClientConnectionEventListener {


    public ClientConnectionEventListener() {
    }

    @Listener
    public void onGamerLogin(ClientConnectionEvent.Login event) {
        User user = event.getTargetUser();
        UUID uniqueId = user.getUniqueId();
        if (!GamersManager.getGamer(uniqueId).isPresent()) {
            GamersManager.register(uniqueId);

        }
        Gamer gamer = GamersManager.getGamer(uniqueId).get();
        gamer.setOnline(true);
        gamer.setName(user.getName());
        gamer.setPlayer(user.getPlayer().get());
        
        BlockyArena.getInstance().getLogger().debug("A new player logged in !");
    }

    @Listener
    public void onGamerLogout(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        Gamer gamer = GamersManager.getGamer(player.getUniqueId()).get();
        gamer.setOnline(false);
        if (gamer.getGame() != null) {
            gamer.quit();
            BlockyArena.getInstance().getLogger().debug("A player disconnected !");
        }
    }

}
