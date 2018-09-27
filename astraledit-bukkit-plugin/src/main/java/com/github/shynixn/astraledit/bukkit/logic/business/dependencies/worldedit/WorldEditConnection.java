package com.github.shynixn.astraledit.bukkit.logic.business.dependencies.worldedit;

import com.github.shynixn.astraledit.api.bukkit.business.controller.WorldEditController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.logic.lib.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * Copyright 2017 Shynixn
 * <p>
 * Do not remove this header!
 * <p>
 * Version 1.0
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class WorldEditConnection implements WorldEditController {

    private static final String PLUGIN_NAME = "WorldEdit";

    /**
     * Returns the WorldEdit selection of the player rightClicking
     *
     * @param player player
     * @return worldEditSelectionLocation
     */
    public Location getRightSelection(Player player) {
        return getSelection(player, "getMaximumPoint");
    }

    /**
     * Returns the worldEdit selection of the player leftClicking
     *
     * @param player player
     * @return worldEditSelection
     */
    public Location getLeftSelection(Player player) {
        return getSelection(player, "getMinimumPoint");
    }

    /**
     * Returns if the player has selected something with worldEdit
     *
     * @param player player
     * @return isSelected
     */
    public boolean hasSelections(Player player) {
        return getRightSelection(player) != null && getLeftSelection(player) != null;
    }

    /**
     * Returns the plugin of WorldEdit
     *
     * @return plugin
     * @throws ClassNotFoundException exception
     */
    private static Object getPlugin() throws ClassNotFoundException {
        return Class.forName("com.sk89q.worldedit.bukkit.WorldEditPlugin").cast(Bukkit.getPluginManager().getPlugin(PLUGIN_NAME));
    }

    /**
     * Returns the selection of the given type and player
     *
     * @param player player
     * @param type   type
     * @return location
     */
    private static Location getSelection(Player player, String type) {
        final Object object;
        try {
            object = ReflectionUtils.invokeMethodByObject(getPlugin(), "getSelection", new Class[]{Player.class}, new Object[]{player});
            if (object != null)
                return (Location) ReflectionUtils.invokeMethodByObject(object, type, new Class[0], new Object[0], Class.forName("com.sk89q.worldedit.bukkit.selections.RegionSelection"));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            AstralEditPlugin.logger().log(Level.WARNING, "Cannot access WorldEdit.", e);
        }
        return null;
    }
}
