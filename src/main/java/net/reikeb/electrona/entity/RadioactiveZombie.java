package net.reikeb.electrona.entity;

import com.google.common.collect.Maps;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.monster.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.gen.Structures;

import javax.annotation.Nullable;
import java.util.Map;

public class RadioactiveZombie extends ZombieEntity {

    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(RadioactiveZombie.class, DataSerializers.INT);
    public static final Map<Integer, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), (resource) -> {
        resource.put(0, new ResourceLocation(Electrona.MODID, "textures/entity/radioactive_zombie/radioactive_zombie_1.png"));
        resource.put(1, new ResourceLocation(Electrona.MODID, "textures/entity/radioactive_zombie/radioactive_zombie_2.png"));
    });

    public RadioactiveZombie(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return ZombieEntity.createMonsterAttributes()
                .add(Attributes.ARMOR, 2.5F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public boolean canBreakDoors() {
        return true;
    }

    @Override
    public boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean isSunSensitive() {
        return false;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        data = super.finalizeSpawn(serverWorld, difficultyInstance, reason, data, nbt);
        this.setZombieType(this.random.nextInt(2));
        ServerWorld world = serverWorld.getLevel();
        if (world.structureFeatureManager().getStructureAt(this.blockPosition(), true, Structures.RUINS.get()).isValid()) {
            this.setPersistenceRequired();
        }
        return data;
    }

    public int getZombieType() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setZombieType(int type) {
        if (type < 0 || type >= 3) {
            type = this.random.nextInt(2);
        }

        this.entityData.set(DATA_TYPE_ID, type);
    }

    public ResourceLocation getResourceLocation() {
        return TEXTURES.getOrDefault(this.getZombieType(), TEXTURES.get(0));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 1);
    }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ZombieType", this.getZombieType());
    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.setZombieType(nbt.getInt("ZombieType"));
    }
}
