package org.godfather.blocksumo.manager.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockManager {

    private final Set<Location> placedBlocks = new HashSet<>();

    public Set<Location> getPlacedBlocks() {
        return placedBlocks;
    }

    public void addBlock(Block block) {
        placedBlocks.add(block.getLocation());
    }

    public void removeBlock(Block block) {
        placedBlocks.remove(block.getLocation());
    }

    public void clear() {
        for (Location loc : placedBlocks) {
            loc.getBlock().setType(Material.AIR);
        }
        placedBlocks.clear();
    }
}
