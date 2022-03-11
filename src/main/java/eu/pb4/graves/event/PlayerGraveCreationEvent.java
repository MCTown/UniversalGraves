package eu.pb4.graves.event;
import eu.pb4.graves.CarpetSupport;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

/**
 * This event is called before grave is created to check if it's allowed
 */
public interface PlayerGraveCreationEvent {
    Event<PlayerGraveCreationEvent> EVENT = EventFactory.createArrayBacked(PlayerGraveCreationEvent.class,
                (listeners) -> (player) -> {
                    CreationResult result = CreationResult.ALLOW;
                    for (PlayerGraveCreationEvent listener : listeners) {
                         result = listener.shouldCreate(player);
                         if (!result.allow) {
                             return result;
                         }
                    }
                    if (CarpetSupport.isPlayerFake(player))
                    {
                        result = CreationResult.BLOCK;
                    }
                    return result;
                });

    CreationResult shouldCreate(ServerPlayerEntity player);

    enum CreationResult {
        ALLOW(true),
        BLOCK(false),
        BLOCK_PVP(false),
        BLOCK_CLAIM(false),
        BLOCK_VOID(false),
        BLOCK_SILENT(false);
        private final boolean allow;

        CreationResult(boolean allow) {
            this.allow = allow;
        }

        public boolean canCreate() {
            return this.allow;
        }
    }
}
