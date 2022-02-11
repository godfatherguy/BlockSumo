package org.godfather.blocksumo.manager.game;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Wool;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockManager {

    private GameManager gameManager;

    public BlockManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private Set<Block> placedBlocks = new HashSet<>();

    public Set<Block> getPlacedBlocks() {
        return this.placedBlocks;
    }

    public void addBlock(Block block) {
        getPlacedBlocks().add(block);
    }

    public void removeBlock(Block block) {
        getPlacedBlocks().remove(block);
    }

    public void clear() {
        for (Block block : getPlacedBlocks()) {
            block.setType(Material.AIR);
        }
        this.placedBlocks.clear();
    }

    public void setRandomColor(Wool wool) {
        int rand = new Random().nextInt(12);
        switch (rand) {
            case 0:
                wool.setColor(DyeColor.GRAY);
                break;
            case 1:
                wool.setColor(DyeColor.BLUE);
                break;
            case 2:
                wool.setColor(DyeColor.BROWN);
                break;
            case 3:
                wool.setColor(DyeColor.CYAN);
                break;
            case 4:
                wool.setColor(DyeColor.GREEN);
                break;
            case 5:
                wool.setColor(DyeColor.LIGHT_BLUE);
                break;
            case 6:
                wool.setColor(DyeColor.LIME);
                break;
            case 7:
                wool.setColor(DyeColor.YELLOW);
                break;
            case 8:
                wool.setColor(DyeColor.ORANGE);
                break;
            case 9:
                wool.setColor(DyeColor.PINK);
                break;
            case 10:
                wool.setColor(DyeColor.PURPLE);
                break;
            case 11:
                wool.setColor(DyeColor.RED);
                break;
            case 12:
                wool.setColor(DyeColor.MAGENTA);
                break;
        }
    }
}
