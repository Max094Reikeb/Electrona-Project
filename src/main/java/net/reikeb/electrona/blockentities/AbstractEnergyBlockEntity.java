package net.reikeb.electrona.blockentities;

import net.reikeb.electrona.utils.ItemHandler;

public interface AbstractEnergyBlockEntity {

    ItemHandler getItemInventory();

    int getElectronicPowerTimesHundred();

    void setElectronicPowerTimesHundred(int electronicPowerTimesHundred);

    double getElectronicPower();

    void setElectronicPower(double electronicPower);

    int getMaxStorage();

    void setMaxStorage(int maxStorage);

    boolean getLogic();

    void setLogic(boolean logic);
}
