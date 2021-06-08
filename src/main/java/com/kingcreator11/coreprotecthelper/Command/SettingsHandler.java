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

import com.kingcreator11.coreprotecthelper.Search.SearchSettings;

import org.bukkit.command.CommandSender;

/***
 * Handles updating settings given commands
 */
public class SettingsHandler {
	
	/**
	 * Handles a settings command
	 * @param args The arguments of the command
	 * @param settings The current settings
	 * @param sender The command sender - for use for pos commands
	 * @return The updated settings
	 */
	public static SearchSettings handleSettingsCommand(String[] args, SearchSettings settings, CommandSender sender)
		throws InvalidCommandException {

		SearchSettings updatedSettings = new SearchSettings(settings);

		if (args.length == 0) throw new InvalidCommandException("Not enough arguments");

		String base = args[0].toLowerCase();

		// Break the command up and delegate it
		switch (base) {
			case "settimezone":
				setTimeZone(updatedSettings, args[1]);
				break;

			case "settimeframe":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				if (args[1].toLowerCase().equals("fromtimepast"))
					setTimeRangeFromTimePast(updatedSettings, args[2]);
				else if (args[1].toLowerCase().equals("fromdates")) {
					if (args.length < 5 || !args[3].equals("-")) throw new InvalidCommandException("Invalid arguments");
					else setTimeRangeFromDates(updatedSettings, args[2], args[4]);
				}
				else if (args[1].toLowerCase().equals("fromtimepastrange")) {
					if (args.length < 5 || !args[3].equals("-")) throw new InvalidCommandException("Invalid arguments");
					else setTimeRangeFromTimePastRange(updatedSettings, args[2], args[4]);
				}

				else throw new InvalidCommandException("Invalid second argument");
				break;
			
			case "cleartimeframe":
				clearTimeFrame(updatedSettings);
				break;

			case "restrictusers":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				else if (args[1].toLowerCase().equals("list")) {
					String[] players = Arrays.copyOfRange(args, 2, args.length);
					restrictUsers(updatedSettings, players);
				}
				else if (args[1].toLowerCase().equals("fromsearch"))
					restrictUsers(updatedSettings, args[2]);

				else throw new InvalidCommandException("Invalid second argument");
				break;

			case "excludeusers":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");

				else if (args[1].toLowerCase().equals("list")) {
					String[] players = Arrays.copyOfRange(args, 2, args.length);
					excludeUsers(updatedSettings, players);
				}
				else if (args[1].toLowerCase().equals("fromsearch"))
					excludeUsers(updatedSettings, args[2]);

				else throw new InvalidCommandException("Invalid second argument");
				break;

			case "clearusersrestrictions":
				clearUserRestrictions(updatedSettings);
				break;

			case "setloc":
				if (args.length < 3) throw new InvalidCommandException("Not enough arguments");
				setLocation(updatedSettings, args[1], args[2]);
				break;

			case "setpos1":
				setPos1(updatedSettings, sender);
				break;

			case "setpos2":
				setPos2(updatedSettings, sender);
				break;

			case "setradius":
				if (args.length < 2) throw new InvalidCommandException("Not enough arguments");
				setRadius(updatedSettings, args[1]);
				break;

			case "setworld":
				if (args.length < 2) throw new InvalidCommandException("Not enough arguments");
				setWorld(updatedSettings, args[1]);
				break;

			case "clearlocrestrictions":
				clearLocationRestrictions(updatedSettings);
				break;

			default:
				throw new InvalidCommandException("Base argument not found");
		}
		
		return updatedSettings;
	}

	private static void setTimeZone(SearchSettings settings, String shift) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setTimeRangeFromTimePast(SearchSettings settings, String time) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setTimeRangeFromTimePastRange(SearchSettings settings, String from, String to) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setTimeRangeFromDates(SearchSettings settings, String from, String to) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void clearTimeFrame(SearchSettings settings) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void restrictUsers(SearchSettings settings, String[] players) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void excludeUsers(SearchSettings settings, String[] players) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void restrictUsers(SearchSettings settings, String searchName) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void excludeUsers(SearchSettings settings, String searchName) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void clearUserRestrictions(SearchSettings settings) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setLocation(SearchSettings settings, String pos1, String pos2) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setPos1(SearchSettings settings, CommandSender sender) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setPos2(SearchSettings settings, CommandSender sender) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setRadius(SearchSettings settings, String radius) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void setWorld(SearchSettings settings, String world) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}

	private static void clearLocationRestrictions(SearchSettings settings) throws InvalidCommandException {
		throw new InvalidCommandException("Unimplemented Command");
	}
}