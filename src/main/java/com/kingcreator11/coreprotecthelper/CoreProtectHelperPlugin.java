/*
   Copyright 2021 KingCreator11

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.kingcreator11.coreprotecthelper;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

import com.kingcreator11.coreprotecthelper.Command.CommandHandler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main Core Protect Helper Plugin Class
 */
public class CoreProtectHelperPlugin extends JavaPlugin {

	/**
	 * Command Handler instance
	 */
	private CommandHandler commandHandler = new CommandHandler(this);

	/**
	 * Core protect API pointer
	 */
	public CoreProtectAPI coreProtectAPI;

	/**
	 * Gets the core protect API
	 * @return the core protect API
	 */
	private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
	}

	/**
	 * Called when the plugin is enabled
	 */
	@Override
	public void onEnable() {

		// Get core protect and test that the API works
		coreProtectAPI = getCoreProtect();
		if (coreProtectAPI != null) {
			coreProtectAPI.testAPI();
		}
		else {
			this.getLogger().severe("Unable to connect to core protect API - Disabling plugin");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// TODO - Read Config

		// Initialize commands
		this.getCommand("coh").setExecutor(commandHandler);
		this.getCommand("cohelper").setExecutor(commandHandler);
	}

	/**
	 * Called before the plugin is disabled
	 */
	@Override
	public void onDisable() {
		
	}
}