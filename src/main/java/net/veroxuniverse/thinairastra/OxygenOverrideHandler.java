package net.veroxuniverse.thinairastra;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import fuzs.thinair.api.v1.AirQualityLevel;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Thinairastra.MODID)
public final class OxygenOverrideHandler {

    private static final int BUFFER_TICKS = 10;
    private static final Map<UUID, Integer> oxygenTickBuffer = new ConcurrentHashMap<>();

    public static boolean isInOxygenFromDistributor(Player p) {
        int ticks = oxygenTickBuffer.getOrDefault(p.getUUID(), 0);
        if (ticks > 0) return true;

        boolean inside = OxygenAreaCache.contains(p.level(), p.blockPosition());
        if (inside) oxygenTickBuffer.put(p.getUUID(), BUFFER_TICKS);
        return inside;
    }

    @SubscribeEvent
    public static void serverStart(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            oxygenTickBuffer.replaceAll((id, t) -> t > 0 ? t - 1 : 0);
            oxygenTickBuffer.values().removeIf(t -> t == 0);

            OxygenAreaCache.nextTick();
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent evt) {

        if (!(evt.getEntity() instanceof Player player) || player.level().isClientSide())
            return;

        if (player.isCreative() || player.isSpectator()) return;

        AirQualityLevel q = AirQualityHelperImpl.INSTANCE
                .getAirQualityAtLocation(player.level(), player.getEyePosition());

        if (q == AirQualityLevel.GREEN) return;

        if (isInOxygenFromDistributor(player)) return;

        if (SpaceSuitItem.hasFullSet(player)   ||
                SpaceSuitItem.hasFullJetSuitSet(player) ||
                SpaceSuitItem.hasFullNetheriteSet(player)) {

            var chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.getItem() instanceof SpaceSuitItem suit && SpaceSuitItem.hasOxygen(player)) {
                if (!player.hasEffect(MobEffects.WATER_BREATHING)) {
                    if (player.tickCount % 40 == 0) suit.consumeOxygen(chest, 1L);
                }
                player.setAirSupply(player.getMaxAirSupply());
            }
        }
    }
}
