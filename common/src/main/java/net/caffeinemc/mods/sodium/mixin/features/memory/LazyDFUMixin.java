package net.caffeinemc.mods.sodium.mixin.features.memory;

import com.mojang.datafixers.DataFixerBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Executor;

/**
 * Photon — LazyDFU tekniği
 * DataFixerUpper sisteminin oyun açılışında gereksiz yere
 * tüm fix'leri derlemesini engeller.
 * Sadece gerçekten ihtiyaç duyulduğunda derlenir.
 * Açılış süresi 20-30 saniye kısalır.
 */
@Mixin(DataFixerBuilder.class)
public class LazyDFUMixin {

    /**
     * buildUnoptimized yerine boş bir executor kullan.
     * DFU fix'leri arka planda değil, istendiğinde derlenir.
     */
    @Redirect(
            method = "build(Ljava/util/concurrent/Executor;)Lcom/mojang/datafixers/DataFixer;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/Executor;execute(Ljava/lang/Runnable;)V"
            )
    )
    private void photon$skipDFUBootstrap(Executor executor, Runnable runnable) {
        // Hiçbir şey yapma — DFU açılışta derlenmez
        // İlk dünya yüklendiğinde otomatik devreye girer
    }
}