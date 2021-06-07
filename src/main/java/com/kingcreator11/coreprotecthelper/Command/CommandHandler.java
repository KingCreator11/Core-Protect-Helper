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
		 * Creates a new command meta data class
		 * @param name The name of the command
		 * @param permission The permissions required to use the command
		 */
		public CommandMeta(String name, String permission) {
			this.name = name;
			this.permission = permission;
		}
	}

	/**
	 * A list of all base commands
	 */
	private static final CommandMeta[] baseCommands = {
		// Searching
		new CommandMeta("search", "coreprotecthelper.usage"),
		// Search saving
		new CommandMeta("savesearch", "coreprotecthelper.usage"),
		new CommandMeta("deletesearch", "coreprotecthelper.usage"),
		new CommandMeta("sendsearch", "coreprotecthelper.usage"),
		// Timeframe
		new CommandMeta("settimezone", "coreprotecthelper.usage"),
		new CommandMeta("settimeframe", "coreprotecthelper.usage"),
		new CommandMeta("cleartimeframe", "coreprotecthelper.usage"),
		// User Commands
		new CommandMeta("restrictusers", "coreprotecthelper.usage"),
		new CommandMeta("excludeusers", "coreprotecthelper.usage"),
		new CommandMeta("clearusersrestrictions", "coreprotecthelper.usage"),
		// Location commands
		new CommandMeta("setloc", "coreprotecthelper.usage"),
		new CommandMeta("setpos1", "coreprotecthelper.usage"),
		new CommandMeta("setpos2", "coreprotecthelper.usage"),
		new CommandMeta("setradius", "coreprotecthelper.usage"),
		new CommandMeta("setworld", "coreprotecthelper.usage"),
		new CommandMeta("clearlocrestrictions", "coreprotecthelper.usage")
	};

	/**
	 * All sub commands linked to a base command
	 */
	private static final Map<String, List<CommandMeta>> subCommands;
	static {
		Map<String, List<CommandMeta>> map = new HashMap<>();

		// Search sub commands
		map.put("search", Arrays.asList(
			new CommandMeta("container", "coreprotecthelper.search.container"),
			new CommandMeta("kill", "coreprotecthelper.search.kill"),
			new CommandMeta("blocks", "coreprotecthelper.search.blocks"),
			new CommandMeta("users", "coreprotecthelper.search.users"),
			new CommandMeta("chat", "coreprotecthelper.search.chat"),
			new CommandMeta("sign", "coreprotecthelper.search.sign"),
			new CommandMeta("command", "coreprotecthelper.search.command"),
			new CommandMeta("toggleablechat", "coreprotecthelper.search.toggleablechat")
		));

		// Timeframe sub commands
		map.put("settimeframe", Arrays.asList(
			new CommandMeta("fromtimepast", "coreprotecthelper.usage"),
			new CommandMeta("fromdates", "coreprotecthelper.usage"),
			new CommandMeta("fromtime", "coreprotecthelper.usage")
		));

		// Restrict users sub commands
		map.put("restrictusers", Arrays.asList(
			new CommandMeta("fromsearch", "coreprotecthelper.usage"),
			new CommandMeta("list", "coreprotecthelper.usage")
		));

		// Exclude users sub commands
		map.put("excludeusers", Arrays.asList(
			new CommandMeta("fromsearch", "coreprotecthelper.usage"),
			new CommandMeta("list", "coreprotecthelper.usage")
		));

		subCommands = Collections.unmodifiableMap(map);
	}

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
		
		return false;
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
}