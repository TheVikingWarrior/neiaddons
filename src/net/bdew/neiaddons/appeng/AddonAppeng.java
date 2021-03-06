/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.appeng;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.network.ServerHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

@Mod(modid = NEIAddons.modId + "|AppEng", name = "NEI Addons: Applied Energistics 2", version = "NEIADDONS_VER", dependencies = "after:NEIAddons;after:appliedenergistics2")
public class AddonAppeng extends BaseAddon {

    @Instance(NEIAddons.modId + "|AppEng")
    public static AddonAppeng instance;

    public static final String setWorkbenchCommand = "SetCellWorkbench";

    public static Class<? extends GuiContainer> clsGuiCellWorkbench;
    public static Class<? extends Container> clsContainerCellWorkbench;
    public static Class<? extends Slot> clsSlotFakeTypeOnly;

    @Override
    public String getName() {
        return "Applied Energistics 2";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"appliedenergistics2"};
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        try {
            clsContainerCellWorkbench = Utils.getAndCheckClass("appeng.container.implementations.ContainerCellWorkbench", Container.class);
            clsSlotFakeTypeOnly = Utils.getAndCheckClass("appeng.container.slot.SlotFakeTypeOnly", Slot.class);
            if (side == Side.CLIENT) {
                clsGuiCellWorkbench = Utils.getAndCheckClass("appeng.client.gui.implementations.GuiCellWorkbench", GuiContainer.class);
            }

            ServerHandler.registerHandler(setWorkbenchCommand, new SetCellWorkbenchCommandHandler());

            active = true;
        } catch (Throwable t) {
            logSevereExc(t, "Error finding cell workbench classes");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        AppEngHelper.init();
    }
}
