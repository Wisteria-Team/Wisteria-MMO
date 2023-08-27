package dev.beabueno.wisteriammo.persistence;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Log
public class CustomPlayer {

    private static final String SELECT_PLAYER = "SELECT UUID, username, lastKnownServer, lastKnownWorldUUID, lastKnownCoords, registrationTimestamp FROM player WHERE UUID = ?";
    private static final String INSERT_PLAYER = "INSERT INTO player(UUID, username, lastKnownServer, lastKnownWorldUUID, lastKnownCoords, registrationTimestamp) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PLAYER = "UPDATE player SET username = ?, lastKnownServer = ?, lastKnownWorldUUID = ?, lastKnownCoords = ?, registrationTimestamp = ? WHERE UUID = ?";


    private final UUID uuid;
    private String username;
    private String lastKnownServer;
    private Location lastKnownLocation;
    private LocalDateTime registrationTimestamp;

    public CustomPlayer(UUID uuid) {
        this.uuid = uuid;

        if (!runSelect()) {
            runInsert();
        }
    }

    @Synchronized
    private boolean runSelect() {
        JSONParser parser = new JSONParser();

        try {
            @Cleanup PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_PLAYER);
            stmt.setString(1, this.uuid.toString() );
            @Cleanup ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                World lastKnownWorld = Bukkit.getWorld(UUID.fromString(rs.getString("lastKnownWorldUUID")));
                JSONObject lastWorldCoordinates = (JSONObject) parser.parse(rs.getString("lastKnownCoords"));

                this.username = rs.getString("username");
                this.lastKnownServer = rs.getString("lastKnownServer");
                this.lastKnownLocation = new Location(lastKnownWorld, (double)lastWorldCoordinates.get("x"), (double)lastWorldCoordinates.get("y"), (double)lastWorldCoordinates.get("z"));
                this.registrationTimestamp = rs.getTimestamp("registrationTimestamp").toLocalDateTime();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Synchronized
    private boolean runInsert() {
        this.username = Bukkit.getPlayer(this.uuid).getName();
        this.lastKnownServer = Bukkit.getPlayer(this.uuid).getServer().getName();
        this.lastKnownLocation = Bukkit.getPlayer(this.uuid).getLocation();
        this.registrationTimestamp = LocalDateTime.now();

        World lastKnownWorld = this.lastKnownLocation.getWorld();
        JSONObject lastWorldCoordinates = new JSONObject();
        lastWorldCoordinates.put("x", this.lastKnownLocation.getX());
        lastWorldCoordinates.put("y", this.lastKnownLocation.getY());
        lastWorldCoordinates.put("z", this.lastKnownLocation.getZ());

        try {
            @Cleanup PreparedStatement stmt = Database.getConnection().prepareStatement(INSERT_PLAYER);
            stmt.setString(1, this.uuid.toString() );
            stmt.setString(2, this.username );
            stmt.setString(3, this.lastKnownServer );
            stmt.setString(4, lastKnownWorld.getUID().toString() );
            stmt.setString(5, lastWorldCoordinates.toString() );
            stmt.setTimestamp(6, Timestamp.valueOf(this.registrationTimestamp));

            int count = stmt.executeUpdate();

            if (count == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Synchronized
    private boolean runUpdate() {

        World lastKnownWorld = this.lastKnownLocation.getWorld();
        JSONObject lastWorldCoordinates = new JSONObject();
        lastWorldCoordinates.put("x", this.lastKnownLocation.getX());
        lastWorldCoordinates.put("y", this.lastKnownLocation.getY());
        lastWorldCoordinates.put("z", this.lastKnownLocation.getZ());

        try {
            @Cleanup PreparedStatement stmt = Database.getConnection().prepareStatement(UPDATE_PLAYER);

            stmt.setString(1, this.username );
            stmt.setString(2, this.lastKnownServer );
            stmt.setString(3, lastKnownWorld.getUID().toString() );
            stmt.setString(4, lastWorldCoordinates.toString() );
            stmt.setTimestamp(5, Timestamp.valueOf(this.registrationTimestamp));

            stmt.setString(6, this.uuid.toString() );

            int count = stmt.executeUpdate();

            // TODO inform sibling instance of plugin of the update, to sync data

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setLastKnownServer(String lastKnownServer) {
        this.lastKnownServer = lastKnownServer;
        runUpdate();
    }
    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
        runUpdate();
    }

    public static CustomPlayer getCustomPlayer(UUID uuid) {
        return PlayerManager.getInstance().getCustomPlayer(uuid);
    }

    public static CustomPlayer getCustomPlayer(Player player) {
        return getCustomPlayer(player.getUniqueId());
    }
}
