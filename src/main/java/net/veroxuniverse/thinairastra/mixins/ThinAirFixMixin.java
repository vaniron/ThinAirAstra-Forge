package net.veroxuniverse.thinairastra.mixins;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.thinairastra.OxygenOverrideHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AirQualityHelperImpl.class)
public abstract class ThinAirFixMixin {

    @Inject(method = "isSensitiveToAirQuality", at = @At("HEAD"),
            cancellable = true, remap = false)
    private void overrideAirSensitivity(LivingEntity ent,
                                        CallbackInfoReturnable<Boolean> cir) {

        if (!(ent instanceof Player p) || p.isCreative() || p.isSpectator()) return;

        if (OxygenOverrideHandler.isInOxygenFromDistributor(p)) {
            cir.setReturnValue(false);
            return;
        }

        if ((SpaceSuitItem.hasFullSet(ent)
                || SpaceSuitItem.hasFullJetSuitSet(ent)
                || SpaceSuitItem.hasFullNetheriteSet(ent))
                && SpaceSuitItem.hasOxygen(ent)) {
            cir.setReturnValue(false);
        }
    }
}
