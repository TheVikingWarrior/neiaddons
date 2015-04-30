/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.exnihilo.proxies;

import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.TypedField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class HammerRegistryProxy {
    private static TypedField<HashMap> f_rewards;
    public static List<ItemStack> hammers = new ArrayList<ItemStack>();
    public static Class<? extends Item> clsBaseHammer;
    public static Set<Item> sourceIds;
    public static Set<Item> dropIds;

    @SuppressWarnings("unchecked")
    public static List<SmashableProxy> getRegistry() {
        Collection<ArrayList<Object>> vals = f_rewards.get(null).values();
        ArrayList<Object> allSmashable = new ArrayList<Object>();
        for (ArrayList<Object> x : vals) {
            allSmashable.addAll(x);
        }
        return new ProxyListView<SmashableProxy>(allSmashable, SmashableProxy.class);
    }

    @SuppressWarnings("unchecked")
    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        Class<?> c_HammerRegistry = Utils.getAndCheckClass("exnihilo.registries.HammerRegistry", Object.class);
        f_rewards = TypedField.fromPrivate(c_HammerRegistry, "rewards", HashMap.class);

        clsBaseHammer = Utils.getAndCheckClass("exnihilo.items.hammers.ItemHammerBase", Item.class);

        for (String name : (Set<String>) Item.itemRegistry.getKeys()) {
            Item item = (Item) Item.itemRegistry.getObject(name);
            if (clsBaseHammer.isInstance(item))
                hammers.add(new ItemStack(item, 1));
        }

        dropIds = new HashSet<Item>();
        sourceIds = new HashSet<Item>();

        for (SmashableProxy smashable : getRegistry()) {
            if (smashable != null) {
                sourceIds.add(Item.getItemFromBlock(smashable.source()));
                dropIds.add(smashable.item());
            }
        }
    }
}
