/*
  Copyright (C) 2015 Petr Mrázek

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.

  Petr Mrázek <peterix@gmail.com>
 */

package org.dethware.friendsss;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, version = Main.VERSION, guiFactory = "org.dethware.friendsss.GuiFactory")
public class Main
{
    public static final String MODID = "org.dethware.friendsss";
    public static final String VERSION = "0.0.3";

    public static Logger logger = LogManager.getLogger(MODID);

    public static Configuration configFile;

    public static Option NERF_CREEPERS = new Option("friendsss.config.nerfCreepers", "nerfCreepers", true, "Creepers won't explode.", "Creepers will explode.", "Make creepers not explode (default).");
    public static Option NERF_ENDERMEN = new Option("friendsss.config.nerfEndermen", "nerfEndermen", true, "Endermen won't steal blocks.", "Endermen will steal blocks.", "Make endermen not pick up things (default).");
    public static Option NERF_GHASTS = new Option("friendsss.config.nerfGhasts", "nerfGhasts", true, "Ghasts won't spit fireballs.", "Ghasts will spit fireballs.", "Make ghasts not spit fireballs (default).");
    public static Option PROTECT_VILLAGERS = new Option("friendsss.config.protectVillagers", "protectVillagers", true, "Zombies are not interested.", "Zombies are hungry.", "Make zombies not attack villagers (default).");
    public static Option[] options = new Option[]{NERF_CREEPERS, NERF_ENDERMEN, NERF_GHASTS, PROTECT_VILLAGERS};

    // The instance of your mod that Forge uses.
    @Mod.Instance("Main")
    public static Main instance;
    EventHandler handler;

    public static void reconfigure(boolean initial)
    {
        for (Option option : options)
        {
            option.reconfigure(initial);
        }

        if (configFile.hasChanged())
        {
            configFile.save();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        reconfigure(true);
        handler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event)
    {
        handler.onServerStop();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartedEvent event)
    {
        handler.onServerStart();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(handler);
    }
}
