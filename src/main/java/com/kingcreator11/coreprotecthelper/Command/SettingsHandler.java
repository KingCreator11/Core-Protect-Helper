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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kingcreator11.coreprotecthelper.CPHBase;
import com.kingcreator11.coreprotecthelper.CoreProtectHelperPlugin;
import com.kingcreator11.coreprotecthelper.Search.SearchSettings;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

/***
 * Handles updating settings given commands
 */
public class SettingsHandler extends CPHBase {

	/**
	 * The updated settings
	 */
	private SearchSettings settings;

	/**
	 * The sender of the command
	 */
	private CommandSender sender;
	
	/**
	 * Handles a settings command
	 * @param args The arguments of the command
	 * @param settings The current settings
	 * @param sender The command sender - for use for pos commands
	 * @param plugin A pointer to the plugin
	 * @return The updated settings
	 * @throws InvalidCommandException
	 */
	public SettingsHandler(String[] args, SearchSettings settings, CommandSender sender, CoreProtectHelperPlugin plugin)
		throws InvalidCommandException {

		super(plugin);

		this.settings = new SearchSettings(settings);
		this.sender = sender;

		if (args.length == 0) throw new InvalidCommandException("Not enough arguments");

		String base = args[0].toLowerCase();

		// Break the command up and delegate it
		switch (base) {
			case "settimezone":
				if (args.length < 2) throw new InvalidCommandException("Not enough arguments");
				setTimeZone(args[1]);
				break;

			case "settimeframe":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				if (args[1].toLowerCase().equals("fromtimepast"))
					setTimeRangeFromTimePast(args[2]);
				else if (args[1].toLowerCase().equals("fromdates")) {
					if (args.length < 5 || !args[3].equals("-")) throw new InvalidCommandException("Invalid arguments");
					else setTimeRangeFromDates(args[2], args[4]);
				}
				else if (args[1].toLowerCase().equals("fromtimepastrange")) {
					if (args.length < 5 || !args[3].equals("-")) throw new InvalidCommandException("Invalid arguments");
					else setTimeRangeFromTimePastRange(args[2], args[4]);
				}

				else throw new InvalidCommandException("Invalid second argument");
				break;
			
			case "cleartimeframe":
				clearTimeFrame();
				break;

			case "restrictusers":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				else if (args[1].toLowerCase().equals("list")) {
					String[] players = Arrays.copyOfRange(args, 2, args.length);
					restrictUsers(players);
				}
				else if (args[1].toLowerCase().equals("fromsearch"))
					restrictUsers(args[2]);

				else throw new InvalidCommandException("Invalid second argument");
				break;

			case "excludeusers":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				else if (args[1].toLowerCase().equals("list")) {
					String[] players = Arrays.copyOfRange(args, 2, args.length);
					excludeUsers(players);
				}
				else if (args[1].toLowerCase().equals("fromsearch"))
					excludeUsers(args[2]);

				else throw new InvalidCommandException("Invalid second argument");
				break;

			case "clearusersrestrictions":
				clearUserRestrictions();
				break;

			case "setloc":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");
				setLocation(args[1], args[2]);
				break;

			case "setpos1":
				setPos1();
				break;

			case "setpos2":
				setPos2();
				break;

			case "setradius":
				if (args.length < 2) throw new InvalidCommandException("Not enough arguments");
				setRadius(args[1]);
				break;

			case "setworld":
				if (args.length < 2) throw new InvalidCommandException("Not enough arguments");
				setWorld(args[1]);
				break;

			case "clearlocrestrictions":
				clearLocationRestrictions();
				break;

			default:
				throw new InvalidCommandException("Base argument not found");
		}
	}

	public SearchSettings getUpdatedSearchSettings() {
		return this.settings;
	}

	/**
	 * Sets the timezone of the search
	 * @param shift
	 * @throws InvalidCommandException
	 */
	private void setTimeZone(String shift) throws InvalidCommandException {
		int timeShift = 0;
		try {
			switch (shift.charAt(0)) {
				case '+':
					timeShift = Integer.parseInt(shift.substring(1));
					break;
				case '-':
					timeShift = -Integer.parseInt(shift.substring(1));
					break;
				default:
					timeShift = Integer.parseInt(shift);
					break;
			}
		}
		catch (NumberFormatException e) {
			throw new InvalidCommandException("Unable to parse the time shift");
		}

		settings.timezoneHours = timeShift;

		sender.sendMessage("§2Timezone set to UTC" + (timeShift >= 0 ? "+" : "") + timeShift);
	}

	/**
	 * Parses a single time past string
	 * @param time A time string in the format of AwBdChDmEs with A-E as numerical values
	 * @return The time past as a date
	 * @throws Exception If unable to convert string or there is multiple of a single modifier
	 */
	private Date parseTimePast(String time) throws Exception {
		long timePast = 0;
		long[] timeModifiers = {1000 * 60 * 60 * 24 * 7, 1000 * 60 * 60 * 24, 1000 * 60 * 60, 1000 * 60, 1000};
		char[] identifiers = {'w', 'd', 'h', 'm', 's'};

		for (int i = 0; i < identifiers.length; i++) {
			Pattern pattern = Pattern.compile("[0-9]*"+identifiers[i]);
			List<String> list = new ArrayList<String>();
			Matcher m = pattern.matcher(time);
			while (m.find()) {
				list.add(m.group());
			}

			if (list.size() > 1) throw new Exception("Cannot have more than one time modifier");
			if (list.size() == 0) continue;
			
			try {
				timePast += timeModifiers[i] * Long.parseLong(list.get(0).substring(0, list.get(0).length() - 1));
			}
			catch (NumberFormatException e) {
				throw new Exception("Invalid Time Format");
			}
		}

		Date date = new Date(System.currentTimeMillis() - timePast);

		return date;
	}

	/**
	 * Formats a date to a readable clean string
	 * @param date
	 * @return The cleaned string
	 */
	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		return format.format(date);
	}

	/**
	 * Sets the time range from time past
	 * @param time
	 * @throws InvalidCommandException
	 */
	private void setTimeRangeFromTimePast(String time) throws InvalidCommandException {
		settings.fromTime = new Date(System.currentTimeMillis());
		try {
			settings.toTime = parseTimePast(time);
		}
		catch (Exception e) {
			throw new InvalidCommandException("Invalid Time Format - please use 1w2d3h4m5s");
		}

		sender.sendMessage("§2Time Range set to "+formatDate(settings.fromTime)+" - "+formatDate(settings.toTime));
	}

	/**
	 * Sets the time range from time past in a range
	 * @param from
	 * @param to
	 * @throws InvalidCommandException
	 */
	private void setTimeRangeFromTimePastRange(String from, String to) throws InvalidCommandException {
		try {
			settings.fromTime = parseTimePast(from);
			settings.toTime = parseTimePast(to);
		}
		catch (Exception e) {
			throw new InvalidCommandException("Invalid Time Format - please use 1w2d3h4m5s");
		}
		
		sender.sendMessage("§2Time Range set to "+formatDate(settings.fromTime)+" - "+formatDate(settings.toTime));
	}

	/**
	 * Sets the time range using dates
	 * @param from <year>-<month>-<day>:<hours>:<minutes>
	 * @param to <year>-<month>-<day>:<hours>:<minutes>
	 * @throws InvalidCommandException
	 */
	private void setTimeRangeFromDates(String from, String to) throws InvalidCommandException {
		DateFormat format = new SimpleDateFormat("yyyy-M-d:H:m");

		try {
			settings.fromTime = new Date(format.parse(from).getTime() + settings.timezoneHours * 1000 * 60 * 60);
			settings.toTime = new Date(format.parse(to).getTime() + settings.timezoneHours * 1000 * 60 * 60);
		}
		catch (ParseException e) {
			throw new InvalidCommandException("Unable to parse date, please use <year>-<month>-<day>:<hours>:<minutes>");
		}

		sender.sendMessage("§2Time Range set to "+formatDate(settings.fromTime)+" - "+formatDate(settings.toTime));
	}

	/**
	 * Clears the time restrictions on the search
	 * @throws InvalidCommandException
	 */
	private void clearTimeFrame() throws InvalidCommandException {
		settings.fromTime = null;
		settings.toTime = null;

		sender.sendMessage("§2Time Constraints Cleared for Future Searches");
	}

	/**
	 * Restricts based on a list of players
	 * @param players
	 * @throws InvalidCommandException
	 */
	private void restrictUsers(String[] players) throws InvalidCommandException {
		// Check that all igns are found
		for (int i = 0; i < players.length; i++) {
			if (!Bukkit.getOfflinePlayer(players[i]).hasPlayedBefore())
				throw new InvalidCommandException("Could not find user named "+players[i]);
		}

		settings.restrictPlayers = Arrays.asList(players);

		sender.sendMessage("§2Successfully restricted future searches to the players list given");
	}

	/**
	 * Excludes a list of players
	 * @param players
	 * @throws InvalidCommandException
	 */
	private void excludeUsers(String[] players) throws InvalidCommandException {
		// Check that all igns are found
		for (int i = 0; i < players.length; i++) {
			if (!Bukkit.getOfflinePlayer(players[i]).hasPlayedBefore())
				throw new InvalidCommandException("Could not find user named "+players[i]);
		}

		settings.excludePlayers = Arrays.asList(players);

		sender.sendMessage("§2Successfully excluded the players list given from future searches");
	}

	/**
	 * Restricts to all players in a saved username search
	 * @param searchName
	 * @throws InvalidCommandException
	 */
	private void restrictUsers(String searchName) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	/**
	 * Excludes all players in a username search
	 * @param searchName
	 * @throws InvalidCommandException
	 */
	private void excludeUsers(String searchName) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	/**
	 * Clears all user restrictions from the search
	 * @throws InvalidCommandException
	 */
	private void clearUserRestrictions() throws InvalidCommandException {
		settings.excludePlayers = null;
		settings.restrictPlayers = null;
		sender.sendMessage("§2Successfully cleared all user restrictions and exclusions");
	}

	/**
	 * Sets the location using a box
	 * @param pos1 x-y-z
	 * @param pos2 x-y-z
	 * @throws InvalidCommandException
	 */
	private void setLocation(String pos1, String pos2) throws InvalidCommandException {

		if (!(sender instanceof Player)) {
			throw new InvalidCommandException("Only a player can run location based commands");
		}
		
		try {
			Player player = (Player) sender;

			int[] pos1List = Arrays.stream(pos1.split("-")).mapToInt(Integer::parseInt).toArray();
			int[] pos2List = Arrays.stream(pos1.split("-")).mapToInt(Integer::parseInt).toArray();

			if (pos1List.length != 3 || pos2List.length != 3)
				throw new InvalidCommandException("Invalid coordinates - please use x-y-z");
			
			settings.pos1.setX((double) pos1List[0]);
			settings.pos1.setY((double) pos1List[1]);
			settings.pos1.setZ((double) pos1List[2]);
			settings.pos1.setWorld(player.getWorld());

			settings.pos2.setX((double) pos2List[0]);
			settings.pos2.setY((double) pos2List[1]);
			settings.pos2.setZ((double) pos2List[2]);
			settings.pos2.setWorld(player.getWorld());

			settings.locationRestrictionType = SearchSettings.LocationRestrictionType.box;

			sender.sendMessage("§2Successfully set a box area selection to " + pos1List[0] + " " + pos1List[1] + " "
				+ pos1List[2] + " - " + pos2List[0] + " " + pos2List[1] + " " + pos2List[2]);
		}
		catch (NumberFormatException e) {
			throw new InvalidCommandException("Invalid coordinates - please use x-y-z");
		}
	}

	/**
	 * Sets the first position in the box
	 * @throws InvalidCommandException
	 */
	private void setPos1() throws InvalidCommandException {
		if (!(sender instanceof Player)) {
			throw new InvalidCommandException("Only a player can run location based commands");
		}

		Player player = (Player) sender;
		RayTraceResult result = player.rayTraceBlocks(10);
		if (result == null || result.getHitBlock() == null) {
			throw new InvalidCommandException("No block found within 10 blocks of player's pointer");
		}
		settings.pos1 = result.getHitBlock().getLocation();
		if (settings.pos2 == null) settings.pos2 = settings.pos1;
		settings.locationRestrictionType = SearchSettings.LocationRestrictionType.box;

		sender.sendMessage("§2Pos1 of box area selection set to " + settings.pos1.getX() + " " + settings.pos1.getY() + " "
			+ settings.pos1.getZ());
	}

	/**
	 * Sets the second position in the box
	 * @throws InvalidCommandException
	 */
	private void setPos2() throws InvalidCommandException {
		if (!(sender instanceof Player)) {
			throw new InvalidCommandException("Only a player can run location based commands");
		}

		Player player = (Player) sender;
		RayTraceResult result = player.rayTraceBlocks(10);
		if (result == null || result.getHitBlock() == null) {
			throw new InvalidCommandException("No block found within 10 blocks of player's pointer");
		}
		settings.pos2 = result.getHitBlock().getLocation();
		if (settings.pos1 == null) settings.pos1 = settings.pos2;
		settings.locationRestrictionType = SearchSettings.LocationRestrictionType.box;

		sender.sendMessage("§2Pos2 of box area selection set to " + settings.pos2.getX() + " " + settings.pos2.getY() + " "
			+ settings.pos2.getZ());
	}

	/**
	 * Sets the location using a sphere
	 * @param radius
	 * @throws InvalidCommandException
	 */
	private void setRadius(String radius) throws InvalidCommandException {
		if (!(sender instanceof Player)) {
			throw new InvalidCommandException("Only a player can run location based commands");
		}

		try {
			Player player = (Player) sender;
			int rad = Integer.parseInt(radius);
			settings.radius = rad;
			settings.radiusLocation = player.getLocation();
			settings.locationRestrictionType = SearchSettings.LocationRestrictionType.sphere;
		}
		catch (NumberFormatException e) {
			throw new InvalidCommandException("Invalid radius - please input a number");
		}

		sender.sendMessage("§2Spherical area selection set with center at " + Math.floor(settings.radiusLocation.getX())
			+ " " + Math.floor(settings.radiusLocation.getY()) + " " + Math.floor(settings.radiusLocation.getZ())
			+ " and radius of " + settings.radius);
	}

	/**
	 * Sets a separate world to search in
	 * @param world
	 * @throws InvalidCommandException
	 */
	private void setWorld(String world)
		throws InvalidCommandException {
		settings.locationRestrictionType = SearchSettings.LocationRestrictionType.world;
		World worldObj = plugin.getServer().getWorld(world);
		if (worldObj == null) {
			throw new InvalidCommandException("World not found");
		}
		settings.world = worldObj;

		sender.sendMessage("§2World area selection successfully set to §6"+world);
	}

	/**
	 * Clears all location based restrictions
	 * @throws InvalidCommandException
	 */
	private void clearLocationRestrictions() throws InvalidCommandException {
		settings.locationRestrictionType = SearchSettings.LocationRestrictionType.global;
		sender.sendMessage("§2All location based constraints were successfully cleared from search settings.");
	}
}