package net.reikeb.electrona.procedures;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class T1SolarPanelUpdate {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("x") == null) {
            if (!dependencies.containsKey("x"))
                LOGGER.warn("Failed to load dependency x for procedure T1SolarPanelUpdate!");
            return;
        }
        if (dependencies.get("y") == null) {
            if (!dependencies.containsKey("y"))
                LOGGER.warn("Failed to load dependency y for procedure T1SolarPanelUpdate!");
            return;
        }
        if (dependencies.get("z") == null) {
            if (!dependencies.containsKey("z"))
                LOGGER.warn("Failed to load dependency z for procedure T1SolarPanelUpdate!");
            return;
        }
        if (dependencies.get("world") == null) {
            if (!dependencies.containsKey("world"))
                LOGGER.warn("Failed to load dependency world for procedure T1SolarPanelUpdate!");
            return;
        }
        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
        IWorld world = (IWorld) dependencies.get("world");
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) (x + 1), (int) y, (int) z))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) (x + 1), (int) y, (int) z))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) (x + 1), (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) (x + 1), (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) (x + 1), (int) y, (int) z), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) (x - 1), (int) y, (int) z))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) (x - 1), (int) y, (int) z))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) (x - 1), (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) (x - 1), (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) (x - 1), (int) y, (int) z), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) y, (int) (z - 1)))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) y, (int) (z - 1)))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) (z - 1));
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) (z - 1));
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) (z - 1)), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) y, (int) (z + 1)))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) y, (int) (z + 1)))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) (z + 1));
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) (z + 1));
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) (z + 1)), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) (y + 1), (int) z))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) (y + 1), (int) z))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) (y + 1), (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) (y + 1), (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) (y + 1), (int) z), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) (y - 1), (int) z))).getBlock()))
                || (BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable").toLowerCase(java.util.Locale.ENGLISH)))
                .contains((world.getBlockState(new BlockPos((int) x, (int) (y - 1), (int) z))).getBlock())))) {
            if ((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 4))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) (y - 1), (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) + 4));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            } else if (((((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) >= ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "MaxStorage")) - 4)) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) < (new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "MaxStorage")))) || (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) < ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "MaxStorage")) - 4)) && (((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) <= 4) && ((new Object() {
                public double getValue(IWorld world, BlockPos pos, String tag) {
                    TileEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity != null)
                        return tileEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) > 0))))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower")) - 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos((int) x, (int) (y - 1), (int) z);
                    TileEntity _tileEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_tileEntity != null)
                        _tileEntity.getTileData().putDouble("ElectronicPower", ((new Object() {
                            public double getValue(IWorld world, BlockPos pos, String tag) {
                                TileEntity tileEntity = world.getBlockEntity(pos);
                                if (tileEntity != null)
                                    return tileEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "ElectronicPower")) + 1));
                    if (world instanceof World)
                        ((World) world).sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
    }
}
