package net.reikeb.electrona.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class DebugEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.getHand() != player.getUsedItemHand()) {
            return;
        }
        double x = event.getPos().getX();
        double y = event.getPos().getY();
        double z = event.getPos().getZ();
        IWorld world = event.getWorld();
        if (Items.STICK == player.getMainHandItem().getItem()) {
            if (!world.isClientSide()) {
                player.displayClientMessage(new StringTextComponent((("ELs: ") + "" + ((new Object() {
                    public double getValue(IWorld world, BlockPos pos, String tag) {
                        TileEntity tileEntity = world.getBlockEntity(pos);
                        if (tileEntity != null)
                            return tileEntity.getTileData().getDouble(tag);
                        return -1;
                    }
                }.getValue(world, new BlockPos((int) x, (int) y, (int) z), "ElectronicPower"))))), false);
            }
        }
    }
}
