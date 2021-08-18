/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.capabilities.size;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import net.dries007.tfc.common.ItemDefinition;
import net.dries007.tfc.util.JsonHelpers;

public class ItemSizeDefinition extends ItemDefinition implements IItemSize
{
    private final Size size;
    private final Weight weight;

    public ItemSizeDefinition(ResourceLocation id, JsonObject json)
    {
        super(id, json);
        this.size = JsonHelpers.getEnum(json, "size", Size.class, Size.NORMAL);
        this.weight = JsonHelpers.getEnum(json, "weight", Weight.class, Weight.MEDIUM);
    }

    @Override
    public Size getSize(ItemStack stack)
    {
        return size;
    }

    @Override
    public Weight getWeight(ItemStack stack)
    {
        return weight;
    }
}
