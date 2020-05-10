package net.dries007.tfc.objects;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import net.dries007.tfc.util.Helpers;

public class TFCTags
{
    public static final Tag<Block> CAN_TRIGGER_COLLAPSE = blockTag("can_trigger_collapse");
    public static final Tag<Block> CAN_START_COLLAPSE = blockTag("can_start_collapse");

    public static final Tag<Block> GRASS = blockTag("grass");

    public static Tag<Block> blockTag(String id)
    {
        return new BlockTags.Wrapper(Helpers.identifier(id));
    }

    public static Tag<Block> blockTag(String domain, String path)
    {
        return new BlockTags.Wrapper(new ResourceLocation(domain, path));
    }
}
