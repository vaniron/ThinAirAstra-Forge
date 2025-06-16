package net.veroxuniverse.thinairastra.mixins;

import earth.terrarium.adastra.common.blockentities.machines.OxygenDistributorBlockEntity;
import fuzs.thinair.api.v1.AirQualityLevel;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.veroxuniverse.thinairastra.OxygenAreaCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = OxygenDistributorBlockEntity.class, remap = false)
public abstract class DistributorPlayerAirMixin {

    @Inject(method = "tickOxygen", at = @At("TAIL"), remap = false)
    private void thinair$storeArea(ServerLevel lvl, BlockPos pos, BlockState s, CallbackInfo ci) {
        Set<BlockPos> area = ((OxygenDistributorAccessor) this).getLastDistributedBlocks();
        OxygenAreaCache.add(lvl, pos, area);
    }

    @Inject(method = "serverTick",
            at = @At(value = "INVOKE",
                    target = "Learth/terrarium/adastra/common/blockentities/machines/" +
                            "OxygenDistributorBlockEntity;consumeDistribution(J)V",
                    shift = At.Shift.AFTER))
    private void thinair$giveBreath(ServerLevel lvl, long t, BlockState s, BlockPos pos, CallbackInfo ci) {

        for (Player p : lvl.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8))) {

            AirQualityLevel q = AirQualityHelperImpl.INSTANCE.getAirQualityAtLocation(lvl, p.getEyePosition());
            if (q == AirQualityLevel.GREEN) continue;

            p.addEffect(new MobEffectInstance(
                    MobEffects.WATER_BREATHING, 2, 0,
                    true,  false,  false));
            if (p.getAirSupply() < p.getMaxAirSupply()) {
                p.setAirSupply(p.getMaxAirSupply());
            }
        }
    }
}
