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

package com.kingcreator11.coreprotecthelper.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kingcreator11.coreprotecthelper.CPHBase;
import com.kingcreator11.coreprotecthelper.CoreProtectHelperPlugin;
import com.kingcreator11.coreprotecthelper.Search.SearchSettings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Handles all basic commands from the plugin
 */
public class CommandHandler extends CPHBase implements CommandExecutor, TabCompleter {

	/**
	 * Meta information regarding a command / sub command
	 */
	static class CommandMeta {

		/**
		 * The name of the command
		 */
		public String name;

		/**
		 * The permission required to use the command
		 */
		public String permission;

		/**
		 * A simple description of what the command's usage is
		 */
		public String description;

		/**
		 * A simple usage string of how the command is to be used
		 */
		public String usage;

		/**
		 * Creates a new command meta data class
		 * @param name The name of the command
		 * @param permission The permissions required to use the command
		 */
		public CommandMeta(String name, String permission) {
			this.name = name;
			this.permission = permission;
		}

		/**
		 * Creates a new command meta data class
		 * @param name The name of the command
		 * @param permission The permissions required to use the command
		 * @param description A simple description of what the command's usage is
		 * @param usage A simple usage string of how the command is to be used
		 */
		public CommandMeta(String name, String permission, String description, String usage) {
			this.name = name;
			this.permission = permission;
			this.description = description;
			this.usage = usage;
		}
	}

	/**
	 * A list of all base commands
	 */
	private static final CommandMeta[] baseCommands = {
		// Searching
		new CommandMeta("search", "coreprotecthelper.usage"),
		// Search saving
		new CommandMeta("savesearch", "coreprotecthelper.usage",
			"§bSaves a search for future usage - all saved searches are deleted when you log off",
			"§3/coh savesearch §c<name>"),
		new CommandMeta("deletesearch", "coreprotecthelper.usage",
			"§bDeletes a saved search",
			"§3/coh deletesearch §c<name>"),
		new CommandMeta("sendsearch", "coreprotecthelper.usage",
			"§bSends a search to another player, the other player must have §6coreprotecthelper.usage§b. " +
			"Both players must be online.",
			"§3/coh sendsearch §c<ign>"),
		// Timeframe
		new CommandMeta("settimezone", "coreprotecthelper.usage",
			"§bSets the timezone shift for timeframes from utc",
			"§3/coh settimezone §c<+/->n"),
		new CommandMeta("settimeframe", "coreprotecthelper.usage"),
		new CommandMeta("cleartimeframe", "coreprotecthelper.usage",
			"§bClears any time frame restrictions on the search for future searches.",
			"§3/coh cleartimeframe"),
		// User Commands
		new CommandMeta("restrictusers", "coreprotecthelper.usage"),
		new CommandMeta("excludeusers", "coreprotecthelper.usage"),
		new CommandMeta("clearusersrestrictions", "coreprotecthelper.usage", 
			"§bClears any user based restrictions placed for future searches.",
			"§3/coh clearusersrestrictions"),
		// Location commands
		new CommandMeta("setloc", "coreprotecthelper.usage",
			"§bRestricts the location of the search to be in the box created between the two coords",
			"§3/coh setloc §cx-y-z x-y-z"),
		new CommandMeta("setpos1", "coreprotecthelper.usage",
			"§bSets position 1 of the location to the block pointed to when using this command",
			"§3/coh setpos1"),
		new CommandMeta("setpos2", "coreprotecthelper.usage",
			"§bSets position 2 of the location to the block pointed to when using this command",
			"§3/coh setpos2"),
		new CommandMeta("setradius", "coreprotecthelper.usage",
			"§bRestricts the location of the search to be within the sphere created by the radius from the command " +
			"executor's location",
			"§3/coh setradius §c<radius>"),
		new CommandMeta("setworld", "coreprotecthelper.usage",
			"§bRestricts the location of the search to be within a certain world",
			"§3/coh setworld §c<world>"),
		new CommandMeta("clearlocrestrictions", "coreprotecthelper.usage",
			"§bClears location based restrictions",
			"§3/coh clearlocrestrictions")
	};

	/**
	 * All sub commands linked to a base command
	 */
	private static final Map<String, List<CommandMeta>> subCommands;
	static {
		Map<String, List<CommandMeta>> map = new HashMap<>();

		// Search sub commands
		map.put("search", Arrays.asList(
			new CommandMeta("container", "coreprotecthelper.search.container",
				"§bSearches through container logs given a list of blocks/items, and a list of blocks/items to exclude",
				"§3/coh search container §c<add/remove/all> §b(OPTIONAL)§c<list of blocks/items> " +
				"§b(OPTIONAL)§ce:<list of blocks/items>"),
			new CommandMeta("kill", "coreprotecthelper.search.kill",
				"§bSearches through kill logs given a list of mobs/players, and a list of mobs/players to exclude.",
				"§3/coh search kill §b(OPTIONAL)§c<list of mobs> §b(OPTIONAL)§ce:<list of mobs>"),
			new CommandMeta("blocks", "coreprotecthelper.search.blocks",
				"§bSearches through blocklogs given a list of blocks, and a list of blocks to exclude",
				"§3/coh search blocks §c<place/break/all> §b(OPTIONAL)§c<list of blocks> §b(OPTIONAL)§ce:<list of blocks>"),
			new CommandMeta("users", "coreprotecthelper.search.users",
				"§bSearches for a user's in game name based on keywords - useful for when you know someone's name but " +
				"they have annoying numbers",
				"§3/coh search users §c<keyword>"),
			new CommandMeta("chat", "coreprotecthelper.search.chat",
				"§bSearches for specific keywords within the chat logs",
				"§3/coh search chat §c<keyword/message>"),
			new CommandMeta("sign", "coreprotecthelper.search.sign",
				"§bSearches for specific keywords within the sign logs",
				"§3/coh search sign §c<keyword/message>"),
			new CommandMeta("command", "coreprotecthelper.search.command",
				"§bSearches for specific keywords within the command logs",
				"§3/coh search command §c<keyword/message>"),
			new CommandMeta("toggleablechat", "coreprotecthelper.search.toggleablechat",
				"§bSearches for a private/toggleable chat, #inverse returns all chat not toggled. <chatname> should " +
				"be set to the config chats",
				"§3/coh search toggleablechat §c<chatname> <keyworld/message> §b(OPTIONAL)§c#inverse")
		));

		// Timeframe sub commands
		map.put("settimeframe", Arrays.asList(
			new CommandMeta("fromtimepast", "coreprotecthelper.usage",
				"§bSets the timeframe from the time of the search to the past n time",
				"§3/coh settimeframe fromtimepast §c<days>d<hours>h<minutes>m"),
			new CommandMeta("fromtimepastrange", "coreprotecthelper.usage",
				"§bRestricts future searches for the user to the time range given. " + 
				"Each time variable is the time past from now eg 5d - 1d is between the past 5-1 days.",
				"§3/coh settimeframe fromtimepastrange §c<days>d<hours>h<minutes>m - <days>d<hours>h<minutes>m"),
			new CommandMeta("fromdates", "coreprotecthelper.usage",
				"§bRestricts future searches for the user to the time range given.",
				"§3/coh settimeframe fromdates §c<year>-<month>-<day>:<hours>:<minutes> - <year>-<month>-<day>:<hours>:<minutes>")
		));

		// Restrict users sub commands
		map.put("restrictusers", Arrays.asList(
			new CommandMeta("fromsearch", "coreprotecthelper.usage",
				"§bRestricts the search to only include users from a saved user search named §6name",
				"§3/coh restrictusers fromsearch §c<name>"),
			new CommandMeta("list", "coreprotecthelper.usage",
				"§bRestricts the search to only include users in the list",
				"§3/coh restrictusers list §c<list of users separated by ,>")
		));

		// Exclude users sub commands
		map.put("excludeusers", Arrays.asList(
			new CommandMeta("fromsearch", "coreprotecthelper.usage",
				"§bExcludes the users from a search from a saved user search named §6name",
				"§3/coh excludeusers fromsearch §c<name>"),
			new CommandMeta("list", "coreprotecthelper.usage",
				"§bExcludes the users in the list from the search",
				"§3/coh excludeusers list §c<list of users separated by ,>")
		));

		subCommands = Collections.unmodifiableMap(map);
	}

	/**
	 * A map of all command sender's search settings
	 */
	private Map<CommandSender, SearchSettings> searchSettings = new HashMap<>();

	/**
	 * Creates a new Command Handler Instnace
	 * @param plugin
	 */
	public CommandHandler(CoreProtectHelperPlugin plugin) {
		super(plugin);
	}

	/**
	 * Method called when the command is used
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 * @return whether or not the command was succesful
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// No args / Invalid first arg. Use as help command
		if (args.length == 0 || !Arrays.stream(baseCommands).anyMatch(x -> x.name.equals(args[0]))) {
			this.helpCommand(sender, args);
			return true;
		}

		// Search saving
		if (args[0].toLowerCase().equals("savesearch")) {
			return true;
		}

		// Search deleting
		if (args[0].toLowerCase().equals("deletesearch")) {
			return true;
		}

		// Search sending
		if (args[0].toLowerCase().equals("sendsearch")) {
			return true;
		}

		// Search command - run a search
		if (args[0].toLowerCase().equals("search")) {
			return true;
		}

		// Command is a settings command
		try {
			searchSettings.put(sender, SettingsHandler.handleSettingsCommand(args, searchSettings.get(sender), sender));
		}
		catch (InvalidCommandException error) {
			sender.sendMessage("§4Invalid Command - " + error.getMsg());
			this.helpCommand(sender, args);
			return true;
		}

		return true;
	}

	/**
	 * Method called when tab completion is used
	 * @param sender
	 * @param command
	 * @param alias
	 * @param args
	 * @return The tab completion options
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		// No args - return all commands which user has perms to
		if (args.length == 0)
			return Arrays.stream(baseCommands).filter(x -> sender.hasPermission(x.permission))
				.map(x -> x.name).collect(Collectors.toList());
		
		// Filter base commands
		if (args.length == 1 && !subCommands.containsKey(args[0]))
			return Arrays.stream(baseCommands).filter(x -> sender.hasPermission(x.permission) && x.name.contains(args[0]))
				.map(x -> x.name).collect(Collectors.toList());
		
		// Filter sub commands
		if (args.length >= 1 && subCommands.containsKey(args[args.length - 1]))
			return subCommands.get(args[args.length - 1]).stream().filter(x -> sender.hasPermission(x.permission))
				.map(x -> x.name).collect(Collectors.toList());
		
		// Filter sub commands by search
		if (args.length > 1 && subCommands.containsKey(args[args.length - 2]))
			return subCommands.get(args[args.length - 2]).stream()
				.filter(x -> sender.hasPermission(x.permission) && x.name.contains(args[args.length - 1]))
				.map(x -> x.name).collect(Collectors.toList());

		return null;
	}

	/**
	 * Runs the help command on the sender
	 * @param sender The sender to run the command on
	 * @param args The args to filter the help by
	 */
	public void helpCommand(CommandSender sender, String[] args) {

		// Message to return
		String message = "§c§l>----- §6§lCore Protect Helper §c§l-----<\n";

		// Send all sub command information
		if (args.length >= 1 && subCommands.containsKey(args[0])) {
			// Go through all sub commands and provide info
			for (CommandMeta subData : subCommands.get(args[0])) {
				message += "§2§lCommand: " + subData.usage + "\n";
				message += "§2§lDescription: " + subData.description + "\n";
			}
		}
		// Argument length more than 1 but first arg is invalid - filter by first arg
		else if (args.length >= 1) {
			// Loop through the commands
			for (CommandMeta data : baseCommands) {
				// Filter by first arg
				if (!data.name.contains(args[0])) continue;
				// Command has subcommands
				if (data.description == null || data.usage == null) {
					// Go through all sub commands and provide info
					for (CommandMeta subData : subCommands.get(data.name)) {
						message += "§2§lCommand: " + subData.usage + "\n";
						message += "§2§lDescription: " + subData.description + "\n";
					}
				}
				// No sub commands - provide basic info
				else {
					message += "§2§lCommand: " + data.usage + "\n";
					message += "§2§lDescription: " + data.description + "\n";
				}
			}
		}
		// Send all command information
		else {
			// Loop through the commands
			for (CommandMeta data : baseCommands) {
				// Command has subcommands
				if (data.description == null || data.usage == null) {
					// Go through all sub commands and provide info
					for (CommandMeta subData : subCommands.get(data.name)) {
						message += "§2§lCommand: " + subData.usage + "\n";
						message += "§2§lDescription: " + subData.description + "\n";
					}
				}
				// No sub commands - provide basic info
				else {
					message += "§2§lCommand: " + data.usage + "\n";
					message += "§2§lDescription: " + data.description + "\n";
				}
			}
		}

		message += "§c§l>-------------------------------<";

		sender.sendMessage(message);
	}
}