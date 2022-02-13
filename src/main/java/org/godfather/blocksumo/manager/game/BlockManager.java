package org.godfather.blocksumo.manager.game;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockManager {

    private GameManager gameManager;

    public BlockManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private Set<Block> placedBlocks = new HashSet<>();

    public Set<Block> getPlacedBlocks() {
        return placedBlocks;
    }

    public void addBlock(Block block) {
        placedBlocks.add(block);
    }

    public void removeBlock(Block block) {
        placedBlocks.remove(block);
    }

    public void clear() {
        for (Block block : placedBlocks) {
            block.setType(Material.AIR);
        }
        placedBlocks.clear();
    }
}
