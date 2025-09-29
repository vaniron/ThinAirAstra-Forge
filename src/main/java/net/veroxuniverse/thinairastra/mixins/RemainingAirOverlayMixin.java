package net.veroxuniverse.thinairastra.mixins;

import net.veroxuniverse.thinairastra.Thinairastra;
import com.simibubi.create.content.equipment.armor.RemainingAirOverlay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value= RemainingAirOverlay.class, remap = false)
public class RemainingAirOverlayMixin {
    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean render(LocalPlayer instance, TagKey tagKey) {
        return instance.isEyeInFluid(FluidTags.WATER) || Thinairastra.airQualityActivatesHelmet((LivingEntity)instance);
    }
}
