package net.reikeb.electrona.world.structures;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.gen.Structures;

import java.util.*;

public class RuinsPieces {

    private static final ResourceLocation FIRST_HOUSE = new ResourceLocation(Electrona.MODID, "ruins/houses/house_ruin_1");
    private static final ResourceLocation SECOND_HOUSE = new ResourceLocation(Electrona.MODID, "ruins/houses/house_ruin_2");
    private static final ResourceLocation THIRD_HOUSE = new ResourceLocation(Electrona.MODID, "ruins/houses/house_ruin_3");
    private static final ResourceLocation FOURTH_HOUSE = new ResourceLocation(Electrona.MODID, "ruins/houses/house_ruin_4");
    private static final ResourceLocation FIFTH_HOUSE = new ResourceLocation(Electrona.MODID, "ruins/houses/house_ruin_5");
    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(
            FIRST_HOUSE, new BlockPos(0, 1, 0),
            SECOND_HOUSE, new BlockPos(0, 1, 0),
            THIRD_HOUSE, new BlockPos(0, 1, 0),
            FOURTH_HOUSE, new BlockPos(0, 1, 0),
            FIFTH_HOUSE, new BlockPos(0, 1, 0));

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new RuinsPieces.Piece(templateManager, FIRST_HOUSE, blockpos, rotation));

        rotationOffSet = new BlockPos(-10, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new RuinsPieces.Piece(templateManager, SECOND_HOUSE, blockpos, rotation));

        rotationOffSet = new BlockPos(-10, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new RuinsPieces.Piece(templateManager, THIRD_HOUSE, blockpos, rotation));

        rotationOffSet = new BlockPos(-10, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new RuinsPieces.Piece(templateManager, FOURTH_HOUSE, blockpos, rotation));

        rotationOffSet = new BlockPos(-10, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new RuinsPieces.Piece(templateManager, FIFTH_HOUSE, blockpos, rotation));
    }

    public static class Piece extends TemplateStructurePiece {

        private ResourceLocation resourceLocation;
        private Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(Structures.RUINS_PIECES, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = RuinsPieces.OFFSET.get(resourceLocation);
            this.templatePosition = pos.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(Structures.RUINS_PIECES, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(TemplateManager templateManager) {
            Template template = templateManager.getOrCreate(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String p_186175_1_, BlockPos p_186175_2_, IServerWorld p_186175_3_, Random p_186175_4_, MutableBoundingBox p_186175_5_) {

        }
    }
}

