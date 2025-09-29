package net.veroxuniverse.thinairastra.mixins;

import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fluids.FluidType;
import net.veroxuniverse.thinairastra.Thinairastra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DivingHelmetItem.class, remap = false)
public abstract class DivingHelmetItemMixin {
    @Redirect(method={"breatheUnderwater(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingTickEvent;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;canDrownInFluidType(Lnet/minecraftforge/fluids/FluidType;)Z"))
    private static boolean redirectCanDrownInFluidType(LivingEntity entity, FluidType fluidtype) {
        return entity.isInFluidType() || fluidtype == entity.getEyeInFluidType() && Thinairastra.airQualityActivatesHelmet(entity);
    }
}