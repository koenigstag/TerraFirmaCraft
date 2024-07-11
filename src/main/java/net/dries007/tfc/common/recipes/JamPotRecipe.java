/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.recipes;

import java.util.ArrayList;
import java.util.List;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.recipes.outputs.PotOutput;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.dries007.tfc.util.Helpers;

public class JamPotRecipe extends PotRecipe
{
    public static final MapCodec<JamPotRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        PotRecipe.CODEC.forGetter(c -> c),
        ItemStack.CODEC.fieldOf("result").forGetter(c -> c.jarredStack),
        ResourceLocation.CODEC.fieldOf("texture").forGetter(c -> c.texture)
    ).apply(i, JamPotRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, JamPotRecipe> STREAM_CODEC = StreamCodec.composite(
        PotRecipe.STREAM_CODEC, c -> c,
        ItemStack.STREAM_CODEC, c -> c.jarredStack,
        ResourceLocation.STREAM_CODEC, c -> c.texture,
        JamPotRecipe::new
    );

    public static final PotOutput.OutputType OUTPUT_TYPE = nbt -> {
        ItemStack stack = ItemStack.of(nbt.getCompound("item"));
        ResourceLocation texture = Helpers.resourceLocation(nbt.getString("texture"));
        return new JamPotRecipe.JamOutput(stack, texture);
    };

    private final ItemStack jarredStack;
    private final ResourceLocation texture;

    public JamPotRecipe(PotRecipe base, ItemStack jarredStack, ResourceLocation texture)
    {
        super(base);
        this.jarredStack = jarredStack;
        this.texture = texture;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access)
    {
        return jarredStack;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    @Override
    public PotOutput getOutput(PotBlockEntity.PotInventory inventory)
    {
        return new JamOutput(jarredStack.copy(), texture);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return TFCRecipeSerializers.POT_JAM.get();
    }

    public record JamOutput(ItemStack stack, ResourceLocation texture) implements PotOutput
    {
        @Override
        public boolean isEmpty()
        {
            return stack.isEmpty();
        }

        @Override
        public InteractionResult onInteract(PotBlockEntity entity, Player player, ItemStack clickedWith)
        {
            if (Helpers.isItem(clickedWith, TFCTags.Items.EMPTY_JAR_WITH_LID) && !stack.isEmpty())
            {
                // take the player's empty jar
                clickedWith.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, stack.split(1));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        @Override
        public ResourceLocation getRenderTexture()
        {
            return texture;
        }

        @Override
        public float getFluidYLevel()
        {
            return Mth.clampedMap(stack.getCount(), 0, 4, 7f / 16, 10f / 16);
        }

        @Override
        public void write(CompoundTag nbt)
        {
            nbt.put("item", stack.save(new CompoundTag()));
            nbt.putString("texture", texture.toString());
        }

        @Override
        public OutputType getType()
        {
            return JamPotRecipe.OUTPUT_TYPE;
        }

        @Override
        public BlockEntityTooltip getTooltip()
        {
            return ((level, state, pos, entity, tooltip) -> {
                final List<Component> text = new ArrayList<>();
                BlockEntityTooltips.itemWithCount(tooltip, stack);
                FoodCapability.addTooltipInfo(stack, text);
                text.forEach(tooltip);
            });
        }
    }
}
