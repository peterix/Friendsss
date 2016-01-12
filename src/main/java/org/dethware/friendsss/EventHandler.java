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

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameRule;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class EventHandler
{
    // enderman nerf
    private static Object savedEndermanState = null;

    private GameRules gameRules = null;

    public EventHandler()
    {
    }

    public static void nerfEndermen(boolean nerf)
    {
        Class endermanClass = net.minecraft.entity.monster.EntityEnderman.class;
        try
        {
            Field carriableField = ReflectionHelper.findField(endermanClass, "carriable");
            carriableField.setAccessible(true);
            if (nerf)
            {
                savedEndermanState = carriableField.get(null);
                carriableField.set(null, new IdentityHashMap(4096));
            }
            else
            {
                if (savedEndermanState == null)
                {
                    return;
                }
                carriableField.set(null, savedEndermanState);
            }
        }
        catch (Exception e)
        {
            Main.logger.error("Endermen nerf failed!", e);
        }
    }

    @SubscribeEvent
    public void onLivingEvent(LivingUpdateEvent event)
    {
        if(gameRules == null)
            return;

        if (gameRules.getGameRuleBooleanValue("nerfGhasts") && event.entityLiving.getClass() == EntityGhast.class)
        {
            EntityGhast ghast = (EntityGhast) event.entityLiving;
            if (ghast.attackCounter >= 18)
                ghast.attackCounter = -42;
        }
        else if (gameRules.getGameRuleBooleanValue("nerfCreepers") && event.entityLiving.getClass() == EntityCreeper.class)
        {
            EntityCreeper creeper = (EntityCreeper) event.entityLiving;
            if(creeper.timeSinceIgnited > 25)
            {
                creeper.timeSinceIgnited = 25;
            }
        }
    }

    @SubscribeEvent
    public void onAttackEvent(LivingSetAttackTargetEvent event)
    {
        if(gameRules == null)
            return;

        if (!gameRules.getGameRuleBooleanValue("protectVillagers"))
            return;

        if (event.target == null || event.entity == null)
            return;
        if (event.target.getClass() == EntityVillager.class && EntityZombie.class.isAssignableFrom(event.entity.getClass()))
        {
            EntityZombie zombie = (EntityZombie) event.entity;
            zombie.attackTarget = null;
        }
    }

    private void addGameRulesIfMissing()
    {
        for (Option option : Main.options)
        {
            if (!gameRules.hasRule(option.name))
            {
                gameRules.addGameRule(option.name, option.value ? "true" : "false");
            }
        }
    }

    private void clearSavedState()
    {
        // clear saved state
        savedEndermanState = null;
    }

    private void applyInitialGameRules()
    {
        for (Option option : Main.options)
        {
            boolean nerfActive = gameRules.getGameRuleBooleanValue(option.name);
            if(option.name.equals("nerfEndermen"))
            {
                nerfEndermen(nerfActive);
            }
            if (nerfActive)
            {
                Main.logger.info(option.enableLog);
            } else
            {
                Main.logger.info(option.disableLog);
            }
        }
    }

    void onServerStart()
    {
        gameRules = getGameRules();
        addGameRulesIfMissing();
        clearSavedState();
        applyInitialGameRules();
        Main.logger.info("gamerules: " + gameRules.toString());
    }

    void onServerStop()
    {
        if(gameRules != null)
        {
            clearSavedState();
            gameRules = null;
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        Main.logger.info("Config changed: " + eventArgs.toString());
        if (eventArgs.modID.equals(Main.MODID))
        {
            Main.reconfigure(false);
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event)
    {
        if(gameRules == null)
            return;
        if (!event.command.getCommandName().equals("gamerule"))
            return;
        boolean nerfActive = gameRules.getGameRuleBooleanValue("nerfEndermen");

        CommandGameRule cgr = (CommandGameRule) event.command;
        try
        {
            cgr.processCommand(event.sender, event.parameters);
        }
        catch (CommandException commandexception)
        {
            ChatComponentTranslation cct = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorOjbects());
            cct.getChatStyle().setColor(EnumChatFormatting.RED);
            event.sender.addChatMessage(cct);
        }
        event.setCanceled(true);
        boolean nerfStillActive = gameRules.getGameRuleBooleanValue("nerfEndermen");
        if (nerfActive != nerfStillActive)
        {
            nerfEndermen(nerfStillActive);
        }
    }
    /**
     * Return the game rule set this command should be able to manipulate.
     */
    private GameRules getGameRules()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
