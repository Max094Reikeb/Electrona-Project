package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.blockentities.LeadCrateEntity;
import fr.firstmegagame4.electrona.blockentities.SteelCrateEntity;
import fr.firstmegagame4.mega_lib.lib.initialization.BlockEntitiesInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntities implements BlockEntitiesInitializer {

    public static BlockEntityType<SteelCrateEntity> STEEL_CRATE_ENTITY;
    public static BlockEntityType<LeadCrateEntity> LEAD_CRATE_ENTITY;

    public void register() {
        BlockEntities.STEEL_CRATE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                Utils.modIdentifier+":"+"steel_crate_entity",
                FabricBlockEntityTypeBuilder.create(SteelCrateEntity::new, Blocks.STEEL_CRATE).build(null)
        );
        BlockEntities.LEAD_CRATE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                Utils.modIdentifier+":"+"lead_crate_entity",
                FabricBlockEntityTypeBuilder.create(LeadCrateEntity::new, Blocks.LEAD_CRATE).build(null)
        );
    }

}
