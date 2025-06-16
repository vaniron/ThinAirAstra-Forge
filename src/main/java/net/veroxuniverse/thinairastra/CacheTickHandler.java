package net.veroxuniverse.thinairastra;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thinairastra.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CacheTickHandler {

    @SubscribeEvent
    public static void serverEnd(TickEvent.ServerTickEvent e){
        if (e.phase == TickEvent.Phase.END) OxygenAreaCache.nextTick();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientStart(TickEvent.ClientTickEvent e){
        if (e.phase == TickEvent.Phase.START) OxygenAreaCache.nextTick();
    }
}