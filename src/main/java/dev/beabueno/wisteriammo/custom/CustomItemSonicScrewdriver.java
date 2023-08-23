package dev.beabueno.wisteriammo.custom;

import dev.beabueno.wisteriammo.WisteriaMMO;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomItemSonicScrewdriver extends CustomItem implements Listener {

    private NamespacedKey keyPower = new NamespacedKey(WisteriaMMO.getPluginInstance(), "power");
    private NamespacedKey keyCoordsX = new NamespacedKey(WisteriaMMO.getPluginInstance(), "coordsx");
    private NamespacedKey keyCoordsY = new NamespacedKey(WisteriaMMO.getPluginInstance(), "coordsy");
    private NamespacedKey keyCoordsZ = new NamespacedKey(WisteriaMMO.getPluginInstance(), "coordsz");


    @Override
    public int getCustomItemId() {
        return 1;
    }

    @Override
    public String getCustomItemName() {
        return "Sonic Screwdriver";
    }

    public List<String> getLore(ItemStack itemStack, ItemMeta itemMeta) {
        List<String> lore = new ArrayList<>();

        Double power = itemMeta.getPersistentDataContainer().get(keyPower, PersistentDataType.DOUBLE);
        lore.add(ChatColor.BLUE + "Power: " + ChatColor.DARK_BLUE + power);
        if (itemMeta.getPersistentDataContainer().has(keyCoordsX) &&
                itemMeta.getPersistentDataContainer().has(keyCoordsY) &&
                itemMeta.getPersistentDataContainer().has(keyCoordsZ) ) {

            int coordsX = itemMeta.getPersistentDataContainer().get(keyCoordsX, PersistentDataType.INTEGER);
            int coordsY = itemMeta.getPersistentDataContainer().get(keyCoordsY, PersistentDataType.INTEGER);
            int coordsZ = itemMeta.getPersistentDataContainer().get(keyCoordsZ, PersistentDataType.INTEGER);

            lore.add(String.format(ChatColor.BLUE + "Pos: " + ChatColor.DARK_BLUE + "(%d, %d, %d)", coordsX, coordsY, coordsZ));
        }

        return lore;
    }

    public void createItemStackCustom(ItemStack itemStack, ItemMeta itemMeta) {
        itemMeta.getPersistentDataContainer().set(keyPower, PersistentDataType.DOUBLE, 0.0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent e){
        Player p = e.getPlayer();

        ItemStack itemStack = p.getItemInHand();

        if (isThisCustomItem(itemStack) && e.getHand().equals(EquipmentSlot.HAND)) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
                e.getPlayer().sendMessage(ChatColor.AQUA + " Sonic Screwdriver clicked on air!");
                if (e.getPlayer().isSneaking()) {
                    itemMeta.getPersistentDataContainer().remove(keyCoordsX);
                    itemMeta.getPersistentDataContainer().remove(keyCoordsY);
                    itemMeta.getPersistentDataContainer().remove(keyCoordsZ);

                    itemMeta.setLore(getLore(itemStack, itemMeta));
                    itemStack.setItemMeta(itemMeta);

                    e.getPlayer().sendMessage(ChatColor.AQUA + " Cleared coordinates!");
                } else {
                    Double power = itemMeta.getPersistentDataContainer().get(keyPower, PersistentDataType.DOUBLE);

                    itemMeta.getPersistentDataContainer().set(keyPower, PersistentDataType.DOUBLE, power+0.1);
                    itemMeta.setLore(getLore(itemStack, itemMeta));
                    itemStack.setItemMeta(itemMeta);
                }
            } else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Material clickedMaterial = e.getClickedBlock().getBlockData().getMaterial();
                e.getPlayer().sendMessage(ChatColor.AQUA + " Sonic Screwdriver clicked on: " + clickedMaterial.toString() + "!");
                if (e.getPlayer().isSneaking()) {
                    int coordsX = e.getClickedBlock().getLocation().getBlockX();
                    int coordsY = e.getClickedBlock().getLocation().getBlockY();
                    int coordsZ = e.getClickedBlock().getLocation().getBlockZ();

                    itemMeta.getPersistentDataContainer().set(keyCoordsX, PersistentDataType.INTEGER, coordsX);
                    itemMeta.getPersistentDataContainer().set(keyCoordsY, PersistentDataType.INTEGER, coordsY);
                    itemMeta.getPersistentDataContainer().set(keyCoordsZ, PersistentDataType.INTEGER, coordsZ);

                    itemMeta.setLore(getLore(itemStack, itemMeta));
                    itemStack.setItemMeta(itemMeta);

                    e.getPlayer().sendMessage(ChatColor.AQUA + " Saved coordinates!");
                }

            }
        }
        e.setCancelled(true);
    }
}
