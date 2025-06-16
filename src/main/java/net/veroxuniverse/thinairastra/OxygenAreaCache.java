package net.veroxuniverse.thinairastra;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class OxygenAreaCache {

    private static final Map<ResourceKey<Level>, Map<BlockPos, Set<BlockPos>>> CURRENT = new ConcurrentHashMap<>();
    private static final Map<ResourceKey<Level>, Set<BlockPos>> LAST = new ConcurrentHashMap<>();

    public static void add(Level lvl, BlockPos distributorPos, Set<BlockPos> area) {
        ResourceKey<Level> key = lvl.dimension();
        CURRENT.computeIfAbsent(key, k -> new HashMap<>()).put(distributorPos, area);
    }

    public static void nextTick() {
        LAST.clear();

        for (var entry : CURRENT.entrySet()) {
            Set<BlockPos> merged = new HashSet<>();
            for (Set<BlockPos> set : entry.getValue().values()) {
                merged.addAll(set);
            }
            LAST.put(entry.getKey(), merged);
        }

        CURRENT.clear();
    }

    public static boolean contains(Level lvl, BlockPos pos) {
        ResourceKey<Level> key = lvl.dimension();
        return LAST.getOrDefault(key, Set.of()).contains(pos);
    }
}
