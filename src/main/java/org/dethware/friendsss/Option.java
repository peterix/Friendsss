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

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Option
{
    String name;
    boolean defaultValue;
    boolean value;
    String enableLog;
    String disableLog;
    String description;
    String languageKey;

    Option(String languageKey, String name, boolean defaultValue, String enableLog, String disableLog, String description)
    {
        this.languageKey = languageKey;
        this.name = name;
        this.value = this.defaultValue = defaultValue;
        this.enableLog = enableLog;
        this.disableLog = disableLog;
        this.description = description;
    }

    void reconfigure(boolean initial)
    {
        Property prop = Main.configFile.get(Configuration.CATEGORY_GENERAL, name, defaultValue);
        prop.comment = description;
        prop.setLanguageKey(languageKey);
        boolean newValue = prop.getBoolean();
        if (newValue != value || initial)
        {
            value = newValue;
        }
    }

    public boolean getValue()
    {
        return value;
    }
}