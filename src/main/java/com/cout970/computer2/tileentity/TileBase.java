package com.cout970.computer2.tileentity;

import net.darkaqua.blacksmith.api.common.storage.DataElementFactory;
import net.darkaqua.blacksmith.api.common.storage.IDataCompound;
import net.darkaqua.blacksmith.api.common.tileentity.defaults.DefaultTileEntityDefinition;

/**
 * Created by cout970 on 15/11/2015.
 */
public class TileBase extends DefaultTileEntityDefinition{

    @Override
    public IDataCompound getUpdateData() {
        IDataCompound data = DataElementFactory.createDataCompound();
        writeUpdatePacket(data);
        return data;
    }

    @Override
    public void onUpdateDataArrives(IDataCompound data) {
        readUpdatePacket(data);
    }

    public void writeUpdatePacket(IDataCompound data) {
        saveData(data);
    }

    public synchronized void readUpdatePacket(IDataCompound dataCompound) {
        loadData(dataCompound);
    }
}
