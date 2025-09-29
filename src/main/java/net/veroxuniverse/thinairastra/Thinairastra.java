package net.veroxuniverse.thinairastra;

import com.mojang.logging.LogUtils;
import fuzs.thinair.api.v1.AirQualityLevel;
import fuzs.thinair.helper.AirQualityHelperImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Thinairastra.MODID)
public class Thinairastra {

    public static final String MODID = "thinairastra";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean airQualityActivatesHelmet(LivingEntity entity) {
        AirQualityLevel air = AirQualityHelperImpl.INSTANCE.getAirQualityAtLocation(entity.level(), entity.getEyePosition());
        return air == AirQualityLevel.RED || air == AirQualityLevel.YELLOW;
    }
}
