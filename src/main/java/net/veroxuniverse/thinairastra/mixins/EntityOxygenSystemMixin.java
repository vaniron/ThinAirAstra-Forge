package net.veroxuniverse.thinairastra.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import earth.terrarium.adastra.common.systems.OxygenApiImpl;
import earth.terrarium.adastra.common.tags.ModItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OxygenApiImpl.class)
public abstract class EntityOxygenSystemMixin {
    private static final Logger LOGGER = LogManager.getLogger("ThinAirAstraMixin");

    /**
     * Make air exists if Create Air
     */
    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/items/armor/SpaceSuitItem;hasOxygen(Lnet/minecraft/world/entity/Entity;)Z"),
            method = "entityTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    private boolean redirectHasOxygen(boolean original, ServerLevel level, LivingEntity entity) {
        LOGGER.debug("Checking oxygen for entity: {}", entity.getName().getString());
        LOGGER.debug("Original SpaceSuitItem.hasOxygen: {}", original);

        boolean createOxygen = true;
        ItemStack helmet = DivingHelmetItem.getWornItem(entity);
        boolean hasHelmet = helmet != null && !helmet.isEmpty();
        boolean hasBacktankWithAir = !BacktankUtil.getAllWithAir(entity).isEmpty();

        LOGGER.debug("DivingHelmetItem worn: {}, Helmet stack: {}", hasHelmet, helmet);
        LOGGER.debug("Backtank with air present: {}", hasBacktankWithAir);

        createOxygen &= hasHelmet;
        createOxygen &= hasBacktankWithAir;

        LOGGER.debug("createOxygen result: {}", createOxygen);
        boolean finalResult = original || createOxygen;
        LOGGER.debug("Final hasOxygen result: {}", finalResult);

        return finalResult;
    }

    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/items/armor/SpaceSuitItem;hasFullSet(Lnet/minecraft/world/entity/LivingEntity;)Z"),
            method = "entityTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    private boolean redirectHasFullSet(boolean original, ServerLevel level, LivingEntity entity) {
        LOGGER.debug("Checking full set for entity: {}", entity.getName().getString());
        LOGGER.debug("Original SpaceSuitItem.hasFullSet: {}", original);

        boolean createArmor = true;
        boolean hasDivingHelmet = entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof DivingHelmetItem;
        boolean hasBacktank = entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BacktankItem;
        boolean hasSpaceSuitLegs = entity.getItemBySlot(EquipmentSlot.LEGS).is(ModItemTags.SPACE_SUITS);
        boolean hasSpaceSuitFeet = entity.getItemBySlot(EquipmentSlot.FEET).is(ModItemTags.SPACE_SUITS);

        LOGGER.debug("Head slot has DivingHelmetItem: {}", hasDivingHelmet);
        LOGGER.debug("Chest slot has BacktankItem: {}", hasBacktank);
        LOGGER.debug("Legs slot has SPACE_SUITS tag: {}", hasSpaceSuitLegs);
        LOGGER.debug("Feet slot has SPACE_SUITS tag: {}", hasSpaceSuitFeet);

        createArmor &= hasDivingHelmet;
        createArmor &= hasBacktank;
        createArmor &= hasSpaceSuitLegs;
        createArmor &= hasSpaceSuitFeet;

        LOGGER.debug("createArmor result: {}", createArmor);
        boolean finalResult = original || createArmor;
        LOGGER.debug("Final hasFullSet result: {}", finalResult);

        return finalResult;
    }
}