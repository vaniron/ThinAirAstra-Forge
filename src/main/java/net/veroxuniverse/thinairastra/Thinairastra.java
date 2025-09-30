package net.veroxuniverse.thinairastra;

import com.mojang.logging.LogUtils;
import com.momosoftworks.coldsweat.api.event.core.init.GatherDefaultTempModifiersEvent;
import com.momosoftworks.coldsweat.api.event.core.registry.TempModifierRegisterEvent;
import com.momosoftworks.coldsweat.api.util.Placement;
import com.momosoftworks.coldsweat.api.util.Temperature;
import fuzs.thinair.api.v1.AirQualityLevel;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Thinairastra.MODID)
public class Thinairastra {

    public static final String MODID = "thinairastra";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Thinairastra() {
        MinecraftForge.EVENT_BUS.register(Thinairastra.class);
    }

    @SubscribeEvent
    public static void onModifiersRegister(TempModifierRegisterEvent event)
    {
        LOGGER.info("Registered oxygen temp modifier");
        event.register(new ResourceLocation(MODID, "oxygen"), OxygenTempModifier::new);
    }

    @SubscribeEvent
    public static void defaultModifiersInit(GatherDefaultTempModifiersEvent event)
    {
        if (event.getTrait() == Temperature.Trait.WORLD)
        {
            event.addModifier(new OxygenTempModifier().tickRate(10), Placement.Duplicates.BY_CLASS, Placement.BEFORE_FIRST);
        }
    }

    public static boolean airQualityActivatesHelmet(LivingEntity entity) {
        AirQualityLevel air = AirQualityHelperImpl.INSTANCE.getAirQualityAtLocation(entity.level(), entity.getEyePosition());
        return air == AirQualityLevel.RED || air == AirQualityLevel.YELLOW;
    }
}
