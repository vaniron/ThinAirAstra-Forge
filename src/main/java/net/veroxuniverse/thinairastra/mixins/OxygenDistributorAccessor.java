package net.veroxuniverse.thinairastra.mixins;

import earth.terrarium.adastra.common.blockentities.machines.OxygenDistributorBlockEntity;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = OxygenDistributorBlockEntity.class, remap = false)
public interface OxygenDistributorAccessor {
    @Accessor("lastDistributedBlocks")
    Set<BlockPos> getLastDistributedBlocks();
}