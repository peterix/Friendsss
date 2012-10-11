package peterix.friendsss;

import java.lang.reflect.Field;

import net.minecraft.src.EntityCreeper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CreeperUpdateEvent {
    @ForgeSubscribe
    public void onLivingEvent(LivingUpdateEvent event)
    {
    	Class creeperClass = net.minecraft.src.EntityCreeper.class;
    	if (event.entityLiving.getClass() == creeperClass)
    	{
    		net.minecraft.src.EntityCreeper creeper = (EntityCreeper) event.entityLiving;
    		try {
    	    	Field sinceStarted = creeperClass.getDeclaredField("d");
    	    	sinceStarted.setAccessible(true);
				int value = sinceStarted.getInt(creeper);
				if(value > 25)
					value = 25;
				sinceStarted.setInt(creeper, value);
			} catch (Exception e) {}
    	}
    }
}
