package peterix.friendsss;

import java.lang.reflect.Field;

import net.minecraft.src.EntityGhast;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class GhastUpdateEvent {
    @ForgeSubscribe
    public void onLivingEvent(LivingUpdateEvent event)
    {
    	Class ghastClass = net.minecraft.src.EntityGhast.class;
    	if (event.entityLiving.getClass() == ghastClass)
    	{
    		net.minecraft.src.EntityGhast ghast = (EntityGhast) event.entityLiving;
    		if(ghast.attackCounter >= 18)
    			ghast.attackCounter = -42;
    	}
    }
}
