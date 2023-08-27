package dev.beabueno.wisteriammo.persistence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerManager {

    private static HashMap<UUID, CustomPlayer> customPlayers = new HashMap<>();
    private static PlayerManager instance = null;

    public static PlayerManager getInstance() {
        if (instance == null) {
            PlayerManager.instance = new PlayerManager();
        }
        return PlayerManager.instance;
    }

    public CustomPlayer getCustomPlayer(UUID uuid) {
        if (!customPlayers.containsKey(uuid)) {
            customPlayers.put(uuid, new CustomPlayer(uuid));
        }
        return customPlayers.get(uuid);
    }

}
