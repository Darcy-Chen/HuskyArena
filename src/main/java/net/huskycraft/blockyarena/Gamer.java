package net.huskycraft.blockyarena;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.Location;

/**
 * The Gamer class represents a player's gaming profile.
 */
public class Gamer {

    private User user;
    private Player player;

    private Session session;
    private GamerStatus status;

    private Spawn lastLocation;
    private Closet closet;

    /**
     * Constructs a unique Gamer profile for the given user.
     */
    public Gamer(User user) {
        this.user = user;
        this.player = user.getPlayer().get();
    }

    /**
     * Saves and then clear the inventory of this Gamer.
     */
    public void saveInventory() {
        closet = new Closet(player);
    }

    /**
     * Retrieves the most recently saved inventory of this Gamer.
     */
    public void retrieveInventory() {
        closet.equip(player);
    }

    /**
     * Spawns this Gamer at the given Spawn point.
     */
    public void spawnAt(Spawn spawn) {
        player.setLocationAndRotation(spawn.getSpawnLocation(), spawn.getSpawnRotation());
    }

    public void spawnAt(Location location) {}

    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the session the player is currently in.
     */
    public void setSession(Session session) {
        this.session = session;
        setStatus(GamerStatus.INGAME);
    }

    /**
     * Gets the session the player is currently in, if there is one.
     * @return null if the player is not in any session
     */
    public Session getSession() {
        return session;
    }

    public void setStatus(GamerStatus status) {
        this.status = status;
    }

    public GamerStatus getStatus() {
        return status;
    }

    public void setLastLocation() {
        this.lastLocation = new Spawn(player.getLocation(), player.getHeadRotation());
    }

    public Spawn getLastLocation() {
        return lastLocation;
    }
}
