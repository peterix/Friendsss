/*
  Copyright (C) 2012 Petr Mrázek

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

package peterix.friendsss;

import java.lang.reflect.Field;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.src.EntityCreeper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CreeperUpdateEvent
{
	Class creeperClass = net.minecraft.src.EntityCreeper.class;
	Field sinceStarted;
	public CreeperUpdateEvent()
	{
		sinceStarted = ReflectionHelper.findField(creeperClass, "timeSinceIgnited");
		sinceStarted.setAccessible(true);
	}
    @ForgeSubscribe
    public void onLivingEvent(LivingUpdateEvent event)
    {
        if (event.entityLiving.getClass() == creeperClass)
        {
            EntityCreeper creeper = (EntityCreeper) event.entityLiving;
            try
            {
                int value = sinceStarted.getInt(creeper);
                if (value > 25) value = 25;
                sinceStarted.setInt(creeper, value);
            }
            catch (Exception e)
            {}
        }
    }
}
