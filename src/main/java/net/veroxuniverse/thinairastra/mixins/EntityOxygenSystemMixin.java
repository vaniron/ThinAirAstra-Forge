package net.veroxuniverse.thinairastra.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import earth.terrarium.adastra.common.systems.OxygenApiImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OxygenApiImpl.class)
public abstract class EntityOxygenSystemMixin {

    /**
     * Make air exists if Create Air
     */
    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/items/armor/SpaceSuitItem;hasOxygen(Lnet/minecraft/world/entity/Entity;)Z"),
            method = "entityTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    private boolean redirectHasOxygen(boolean original, ServerLevel level, LivingEntity entity) {

        boolean createOxygen = true;
        ItemStack helmet = DivingHelmetItem.getWornItem(entity);
        boolean hasHelmet = helmet != null && !helmet.isEmpty();
        boolean hasBacktankWithAir = !BacktankUtil.getAllWithAir(entity).isEmpty();

        createOxygen &= hasHelmet;
        createOxygen &= hasBacktankWithAir;

        if (createOxygen) {
            BacktankUtil.consumeAir(entity, BacktankUtil.getAllWithAir(entity).get(0), 1);
        }

        return original || createOxygen;
    }

    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/items/armor/SpaceSuitItem;hasFullSet(Lnet/minecraft/world/entity/LivingEntity;)Z"),
            method = "entityTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    private boolean redirectHasFullSet(boolean original, ServerLevel level, LivingEntity entity) {

        boolean createArmor = true;
        ItemStack divinghelmet = DivingHelmetItem.getWornItem(entity);
        boolean hasDivingHelmet = divinghelmet != null && !divinghelmet.isEmpty();
        boolean hasBacktank = !BacktankUtil.getAllWithAir(entity).isEmpty();

        createArmor &= hasDivingHelmet;
        createArmor &= hasBacktank;

        if (createArmor) {
            BacktankUtil.consumeAir(entity, BacktankUtil.getAllWithAir(entity).get(0), 1);
        }

        return original || createArmor;
    }
}