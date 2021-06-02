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

package io.github.mrdarcychen.games.states;

import io.github.mrdarcychen.commands.RewardService;
import io.github.mrdarcychen.games.GameSession;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;

import java.util.List;

public class StoppingState extends MatchState {

    private final Team winner;
    private final List<Team> losers;

    public StoppingState(GameSession gameSession, List<Player> players, Team winner, List<Team> losers) {
        super(gameSession, players);
        this.winner = winner;
        this.losers = losers;

        announceGameResult();
        proceedToLeavingStateShortly();
    }

    private void proceedToLeavingStateShortly() {
        new Timer(5, (tMinus) -> {
            if (tMinus == 0) {
                gameSession.setMatchState(new LeavingState(gameSession, players));
                winner.getPlayers().forEach(RewardService::offer);
            }
        });
    }

    private void announceGameResult() {
        Title victory = buildResultTitle("VICTORY!", TextColors.GOLD, winner.toString());
        Title gameOver = buildResultTitle("GAME OVER!", TextColors.RED, winner.toString());

        winner.broadcast(victory);
        winner.getPlayers().forEach(it -> it.playSound(
                SoundTypes.ENTITY_PLAYER_LEVELUP, it.getLocation().getPosition(), 100
        ));
        losers.forEach(it -> {
            it.broadcast(gameOver);
            it.getPlayers().forEach(player -> {
                player.playSound(SoundTypes.BLOCK_GLASS_BREAK, player.getHeadRotation(), 100);
            });
        });
    }

    private Title buildResultTitle(String title, TextColor color, String winner) {
        return Title.builder()
                .title(Text.builder(title)
                        .color(color).style(TextStyles.BOLD)
                        .build())
                .subtitle(Text.of(winner + " won the game."))
                .fadeIn(1).stay(60).fadeOut(2)
                .build();
    }
}
