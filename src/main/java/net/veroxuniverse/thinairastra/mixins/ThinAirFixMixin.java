package net.veroxuniverse.thinairastra.mixins;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AirQualityHelperImpl.class)
public abstract class ThinAirFixMixin {

    @Inject(
            method = "isSensitiveToAirQuality",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void thinairastra$overrideAirSensitivity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return;
        }

        if ((SpaceSuitItem.hasFullSet(entity)
                || SpaceSuitItem.hasFullJetSuitSet(entity)
                || SpaceSuitItem.hasFullNetheriteSet(entity))
                && SpaceSuitItem.hasOxygen(entity)) {

            cir.setReturnValue(false);
        }
    }
}