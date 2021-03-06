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

package io.github.mrdarcychen.games;

import org.spongepowered.api.entity.living.player.Player;

public interface PlayerAssistant {
    /**
     * Prepares the player for the game.
     */
    void recruit(Player player);

    /**
     * Restores previous state of the player.
     */
    void dismiss(Player player);

    void eliminate(Player player);

    /**
     * Restores previous state of all players.
     */
    void dismissAll();

    void setSpectatorProperties(Player player, boolean status);

    void maxFoodAndHealth(Player player);

    boolean contains(Player player);
}
