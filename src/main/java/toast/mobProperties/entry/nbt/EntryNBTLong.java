package toast.mobProperties.entry.nbt;

import net.minecraft.nbt.NBTTagLong;
import toast.mobProperties.FileHelper;
import toast.mobProperties.IPropertyReader;
import toast.mobProperties.entry.NBTStatsInfo;

import com.google.gson.JsonObject;

public class EntryNBTLong extends EntryNBTNumber {
    public EntryNBTLong(String path, JsonObject root, int index, JsonObject node, IPropertyReader loader) {
        super(path, root, index, node, loader);
    }

    /// Adds any NBT tags to the list.
    @Override
    public void addTags(NBTStatsInfo nbtStats) {
        long value = FileHelper.getLongCount(this.values, nbtStats.random);
        nbtStats.addTag(this.name, new NBTTagLong(value));
    }
}