package dev.beabueno.wisteriammo.custom;

import dev.beabueno.wisteriammo.WisteriaMMO;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CustomItem implements Listener {

    private static List<? extends CustomItem> customItems = Arrays.asList(
            new CustomItemSonicScrewdriver()
    );
    protected static Material material = Material.KNOWLEDGE_BOOK;

    public abstract int getCustomItemId();

    public abstract String getCustomItemName();

    public List<String> getLore(ItemStack itemStack, ItemMeta itemMeta) {
        return new ArrayList<>();
    }

    public static void registerCustomItems() {
        for (CustomItem item : customItems) {
            Bukkit.getPluginManager().registerEvents(item, WisteriaMMO.getPluginInstance());
        }
    }

    public boolean isThisCustomItem(ItemStack itemStack) {
        if(itemStack.getType().equals(CustomItem.material) && itemStack.getItemMeta().getCustomModelData() == getCustomItemId()){
            return true;
        }
        return false;
    }

    public void createItemStackCustom(ItemStack itemStack, ItemMeta itemMeta) {
    }

    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(CustomItem.material);
        KnowledgeBookMeta itemMeta = (KnowledgeBookMeta) itemStack.getItemMeta();
        createItemStackCustom(itemStack, itemMeta);

        itemMeta.setCustomModelData(getCustomItemId());
        itemMeta.setDisplayName(getCustomItemName());
        itemMeta.setLore(getLore(itemStack, itemMeta));
        itemMeta.setRecipes(new ArrayList<>());

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


}
