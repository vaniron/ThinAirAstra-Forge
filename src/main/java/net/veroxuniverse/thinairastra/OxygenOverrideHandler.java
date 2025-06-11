package net.veroxuniverse.thinairastra;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import fuzs.thinair.api.v1.AirQualityLevel;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thinairastra.MODID)
public class OxygenOverrideHandler {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide()) return;

        var air = AirQualityHelperImpl.INSTANCE.getAirQualityAtLocation(level, entity.getEyePosition());

        if (air == AirQualityLevel.RED || air == AirQualityLevel.YELLOW) {
            if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                return;
            }

            if (SpaceSuitItem.hasFullSet(entity) ||
                    SpaceSuitItem.hasFullJetSuitSet(entity) ||
                    SpaceSuitItem.hasFullNetheriteSet(entity)) {

                var chestStack = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);

                if (chestStack.getItem() instanceof SpaceSuitItem suitItem) {
                    if (SpaceSuitItem.hasOxygen(entity)) {
                        if (entity.tickCount % 20 == 0) {
                            suitItem.consumeOxygen(chestStack, 1L);
                        }

                        entity.setAirSupply(entity.getMaxAirSupply());
                    }
                }
            }
        }
    }
}