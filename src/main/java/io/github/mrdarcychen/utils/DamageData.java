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

package io.github.mrdarcychen.utils;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.source.BlockDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;

import java.util.Optional;

/**
 * A DamageData represents the information about a specific damage dealt on a Gamer.
 */
public class DamageData {

    private final Player victim;
    private Player attacker;
    private DamageType damageType;

    public DamageData(Player victim, Cause cause) {
        this.victim = victim;
        Optional<DamageSource> optDamageSourcecause = cause.first(DamageSource.class);
        if (optDamageSourcecause.isPresent()) {
            DamageSource damageSource = optDamageSourcecause.get();
            damageType = damageSource.getType();
            if (damageSource instanceof IndirectEntityDamageSource) {
                IndirectEntityDamageSource indirectEntityDamageSource =
                        (IndirectEntityDamageSource) damageSource;
                Entity indirectSource = indirectEntityDamageSource.getIndirectSource();
                if (indirectSource instanceof Player) {
                    attacker = (Player) indirectSource;
                }
//                Optional<Player> owner = cause.getContext().get(EventContextKeys.OWNER).get()
//                        .getPlayer();

            } else if (damageSource instanceof EntityDamageSource) {
                EntityDamageSource entityDamageSource = (EntityDamageSource) damageSource;
                Entity directSource = entityDamageSource.getSource();
                if (directSource instanceof Player) {
                    attacker = (Player) directSource;
                }
            } else if (damageSource instanceof BlockDamageSource) {
                // TODO
            }
        }
    }

    public String getDeathMessage() {
        if (damageType.getName().equalsIgnoreCase("attack") && attacker != null) {
            return victim.getName() + " was killed by " + attacker.getName() + ".";
        } else if (damageType.getName().equalsIgnoreCase("fall")) {
            return victim.getName() + " fell from a high place.";
        } else if (damageType.getName().equalsIgnoreCase("fire")) {
            return victim.getName() + " was burned to death.";
        } else if (damageType.getName().equalsIgnoreCase("magic") && attacker != null) {
            return victim.getName() + " was killed by " + attacker.getName() + "'s magic.";
        } else if (damageType.getName().equalsIgnoreCase("void")) {
            return victim.getName() + " fell into the void.";
        }
        return victim.getName() + " died.";
    }

    public Optional<Player> getAttacker() {
        return Optional.ofNullable(attacker);
    }

    public Player getVictim() {
        return victim;
    }

    public DamageType getDamageType() {
        return damageType;
    }
}