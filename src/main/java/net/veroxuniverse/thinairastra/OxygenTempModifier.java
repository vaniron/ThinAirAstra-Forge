package net.veroxuniverse.thinairastra;

import com.momosoftworks.coldsweat.api.temperature.modifier.TempModifier;
import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.api.util.Temperature.Units;
import earth.terrarium.adastra.api.systems.OxygenApi;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class OxygenTempModifier extends TempModifier {
    @Override
    protected Function<Double, Double> calculate(LivingEntity livingEntity, Temperature.Trait trait) {
        if (!livingEntity.level().isClientSide) {
            if (!OxygenApi.API.hasOxygen(livingEntity.level())) {
                if (OxygenApi.API.hasOxygen(livingEntity.level(), livingEntity.blockPosition())) {
                    return temp -> Temperature.convert(70, Units.F, Units.MC, true);
                }
            }
        }
        return temp -> temp;
    }
}
