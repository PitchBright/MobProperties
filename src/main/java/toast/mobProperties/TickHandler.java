package toast.mobProperties;

import java.util.ArrayDeque;

import net.minecraft.entity.EntityLivingBase;
import toast.mobProperties.entry.MobStatsInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandler {
    // Stack of entities that need to be spawned.
    public static ArrayDeque<InitEntry> entityStack = new ArrayDeque<InitEntry>();

    public TickHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Called each tick.
     * TickEvent.Type type = the type of tick.
     * Side side = the side this tick is on.
     * TickEvent.Phase phase = the phase of this tick (START, END).
     *
     * @param event The event being triggered.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!TickHandler.entityStack.isEmpty()) {
                int count = 10;
                InitEntry entry;
                while (count-- > 0) {
                    entry = TickHandler.entityStack.pollFirst();
                    if (entry == null) {
                        break;
                    }
                    entry.execute();
                }
            }
        }
    }

    // Puts the mob into the stack.
    public static void markForInit(EntityLivingBase entity, MobProperties props) {
        TickHandler.entityStack.add(new InitEntry(entity, props));
    }

    // Saves the entity with its properties object for easy access.
    private static class InitEntry {
        // The entity to be initialized.
        public final EntityLivingBase entity;
        // The properties to initialize by.
        public final IProperty props;

        public InitEntry(EntityLivingBase initEntity, MobProperties props) {
            this.entity = initEntity;
            this.props = props;
        }

        // Called to do the actual initialization.
        public void execute() {
            EffectHelper.loadStats(this.entity, false);
            if (EffectHelper.getInit(this.entity) > 0 || !this.entity.isEntityAlive())
				return;

            EffectHelper.setInit(this.entity, 1);
            MobStatsInfo mobStats = new MobStatsInfo(this.entity);
            this.props.init(mobStats);
            mobStats.save();
        }
    }
}