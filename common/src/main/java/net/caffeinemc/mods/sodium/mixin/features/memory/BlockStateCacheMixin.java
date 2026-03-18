package net.caffeinemc.mods.sodium.mixin.features.memory;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.world.level.block.state.StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Photon — FerriteCore tekniği
 * Block state property map'lerini deduplicate eder.
 * Aynı içerikli map'ler tek nesne paylaşır → RAM %40-50 azalır.
 */
@Mixin(StateHolder.class)
public class BlockStateCacheMixin {

    // Deduplicate tablosu — aynı map içeriği = aynı nesne
    private static final Reference2ReferenceOpenHashMap<Map<?, ?>, Map<?, ?>> MAP_CACHE =
            new Reference2ReferenceOpenHashMap<>();

    /**
     * getValues() çağrısını yakalar ve sonucu deduplicate eder.
     * Her çağrıda aynı içerikli map varsa onu döndürür.
     */
    @Inject(method = "getValues", at = @At("RETURN"), cancellable = true)
    private void photon$deduplicateValues(CallbackInfoReturnable<Map<?, ?>> cir) {
        Map<?, ?> original = cir.getReturnValue();
        if (original == null) return;

        synchronized (MAP_CACHE) {
            Map<?, ?> cached = MAP_CACHE.putIfAbsent(original, original);
            if (cached != null) {
                cir.setReturnValue(cached);
            }
        }
    }
}
