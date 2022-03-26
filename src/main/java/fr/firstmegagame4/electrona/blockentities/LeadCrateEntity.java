package fr.firstmegagame4.electrona.blockentities;

import fr.firstmegagame4.electrona.BlockEntities;
import fr.firstmegagame4.electrona.screenhandlers.LeadCrateScreenHandler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

public class LeadCrateEntity extends CrateEntity {

    public LeadCrateEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LEAD_CRATE_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("gui.electrona.lead_crate.name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new LeadCrateScreenHandler(syncId, inv, this);
    }

}
