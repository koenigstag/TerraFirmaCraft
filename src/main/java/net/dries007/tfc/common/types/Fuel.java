/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.util.JsonHelpers;

public class Fuel
{
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int duration;
    private final float temperature;

    public Fuel(ResourceLocation id, JsonObject json)
    {
        this.id = id;
        this.ingredient = Ingredient.fromJson(JsonHelpers.get(json, "ingredient"));
        this.duration = GsonHelper.getAsInt(json, "duration");
        this.temperature = GsonHelper.getAsFloat(json, "temperature");
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public boolean isValid(ItemStack stack)
    {
        return ingredient.test(stack);
    }

    public Collection<Item> getValidItems()
    {
        return Arrays.stream(this.ingredient.getItems()).map(ItemStack::getItem).collect(Collectors.toSet());
    }

    public int getDuration()
    {
        return duration;
    }

    public float getTemperature()
    {
        return temperature;
    }
}
