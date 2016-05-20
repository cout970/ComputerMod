package com.cout970.computer2;

import net.darkaqua.blacksmith.api.common.block.IBlock;
import net.darkaqua.blacksmith.api.common.fluid.IFluidStack;
import net.darkaqua.blacksmith.api.common.inventory.IItemStack;
import net.darkaqua.blacksmith.api.common.item.IItem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LangHelper {

    public static Map<String, String> names = new HashMap<>();

    public static void addTexts(){
        putWithoutSuffix("itemGroup."+ComputerMod.COMPUTER_TAB.getLabel(), ComputerMod.MOD_NAME+" Main");
    }

    public static void addName(Object obj, String name) {
        if (obj == null) return;
        if (name == null) return;
        if (obj instanceof IItemStack) {
            put(((IItemStack) obj).getUnlocalizedName(), name);
        } else if (obj instanceof IBlock) {
            put(((IBlock) obj).getUnlocalizedName(), name);
        } else if (obj instanceof IItem) {
            put(((IItem) obj).getUnlocalizedName(), name);
        } else if (obj instanceof IFluidStack) {
            putWithoutSuffix(((IFluidStack) obj).getUnlocalizedName(), name);
        } else if (obj instanceof String) {
            put((String) obj, name);
        }
    }

    public static void put(String key, String name) {
        names.put(key + ".name", name);
    }

    public static void putWithoutSuffix(String key, String name) {
        names.put(key, name);
    }

    public static void setupLangFile() {
        File f = new File(ComputerMod.DEV_HOME + "/src/main/resources/assets/"+ComputerMod.MOD_ID+"/lang/en_US.lang");
        Writer w;
        try {
            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            for (Map.Entry<String, String> s : names.entrySet()) {
                w.write(s.getKey() + "=" + s.getValue() + "\n");
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
