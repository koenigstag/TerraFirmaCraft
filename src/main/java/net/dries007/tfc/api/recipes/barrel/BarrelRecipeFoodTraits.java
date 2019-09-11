/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.recipes.barrel;

import java.util.Queue;
import javax.annotation.Nonnull;

import com.google.common.collect.Queues;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.api.capability.food.IFoodTrait;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;

public class BarrelRecipeFoodTraits extends BarrelRecipe
{
    private final IFoodTrait trait;

    public BarrelRecipeFoodTraits(@Nonnull IIngredient<FluidStack> inputFluid, @Nonnull IIngredient<ItemStack> inputStack, IFoodTrait trait, int duration)
    {
        super(inputFluid, inputStack, null, ItemStack.EMPTY, duration);
        this.trait = trait;
    }

    @Nonnull
    @Override
    public Queue<ItemStack> getOutputItem(FluidStack inputFluid, ItemStack inputStack)
    {
        ItemStack stack = inputStack.copy();
        IFood food = stack.getCapability(CapabilityFood.CAPABILITY, null);
        if (food != null)
        {
            CapabilityFood.applyTrait(food, trait);
        }
        Queue<ItemStack> output = Queues.newArrayDeque();
        output.add(inputStack);
        return output;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getResultName()
    {
        return I18n.format("tfc.food_traits." + trait.getName() + "_active");
    }
}
