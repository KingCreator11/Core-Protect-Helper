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

package com.kingcreator11.coreprotecthelper.Search;

import java.util.Date;
import java.util.List;

import com.kingcreator11.coreprotecthelper.Misc.Vector3;

import org.bukkit.World;

/**
 * Represents settings for a single search
 */
public class SearchSettings {

	/**
	 * Type of location restriction used for filtering
	 */
	enum LocationRestrictionType {
		box, sphere, global
	}
	
	/**
	 * The timezone hours +/- from UTC
	 */
	public int timezoneHours = 0;

	/**
	 * The date from which the search should begin
	 */
	public Date fromTime;

	/**
	 * The date extent to which the search should end at
	 */
	public Date toTime;

	/**
	 * The location restriction type used for searching
	 */
	public LocationRestrictionType locationRestrictionType;

	/**
	 * The world to search within
	 */
	public World world;

	/**
	 * The radius of the search - Only used with sphere searches
	 */
	public int radius;

	/**
	 * The first position in the box - Only used with box searches
	 */
	public Vector3 pos1;

	/**
	 * The second position in the box - Only used with box searches
	 */
	public Vector3 pos2;
	
	/**
	 * A list of players to restrict to
	 */
	public List<String> restrictPlayers;

	/**
	 * A list of players to exclude
	 */
	public List<String> excludePlayers;
}