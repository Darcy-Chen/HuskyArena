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

package net.huskycraft.blockyarena.games.states;

import net.huskycraft.blockyarena.arenas.Arena;
import net.huskycraft.blockyarena.arenas.SpawnPoint;
import net.huskycraft.blockyarena.games.Game;
import net.huskycraft.blockyarena.utils.DamageData;
import net.huskycraft.blockyarena.utils.Gamer;
import net.huskycraft.blockyarena.utils.GamerStatus;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import java.util.List;

public abstract class MatchState {

    protected final Game game;
    protected List<Gamer> gamers; // only store active gamers

    public MatchState(Game game, List<Gamer> gamers) {
        this.game = game;
        this.gamers = gamers;
    }

    /*
     * Called when a new player try to join an arena.
     */
    public void recruit(Gamer gamer) {
        // execute only if the gamer is accepted to the game
        gamer.saveLocation();
        gamer.saveInventory();
        gamer.setGame(game);
        Player player = gamer.getPlayer();
        player.getInventory().clear();  // TODO: allow bringing personal kit
        player.sendMessage(Text.of("Sending you to " + game.getArena().getName() + " ..."));
        Arena arena = game.getArena();
        SpawnPoint spawnPoint = arena.getLobbySpawn();
        player.setTransform(spawnPoint.getTransform());
        // TODO: refer to game logistics for the following parameters
        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
        player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).get());
        player.offer(Keys.FOOD_LEVEL, 20);
    }

    /*
     * Called when you quit the game
     */
    public void dismiss(Gamer gamer) {
        System.out.println("dismiss" + gamer.getName());
        gamer.retrieveInventory();
        gamer.setLocation(gamer.getSavedLocation());
        gamer.setGame(null);
        Player player = gamer.getPlayer();
        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
        System.out.println(player.getName() + ": " + player.get(Keys.GAME_MODE));
        player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).get());
        player.offer(Keys.FOOD_LEVEL, 20);
    }

    /*
     * Called when you kill a @Gamer gamer
     */
    public void eliminate(Gamer gamer, Text cause) {
        broadcast(cause);
        Player player = gamer.getPlayer();
        player.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
        player.setTransform(game.getArena().getSpectatorSpawn().getTransform());
        Text deathText = Text.builder("YOU DIED!")
                .color(TextColors.RED).build();
        Title deathTitle = Title.builder()
                .title(deathText).fadeOut(2).stay(16).build();
        gamer.getPlayer().sendTitle(deathTitle);
        // gamer.spectate(game);
    }

    public void analyze(DamageEntityEvent event, DamageData damageData) {
        if (damageData.getDamageType().getName().equalsIgnoreCase("void")) {
            damageData.getVictim().getPlayer().setTransform(game.getArena().getLobbySpawn().getTransform());
        }
        event.setCancelled(true);
    }

    /**
     * Broadcasts the given message to all connected Gamers in this Game.
     *
     * @param msg the message to be delivered
     */
    public void broadcast(Text msg) {
        gamers.forEach(it -> it.getPlayer().sendMessage(msg));
    }
}
