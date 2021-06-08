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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents settings for a single search
 */
public class SearchSettings {

	/**
	 * Type of location restriction used for filtering
	 */
	public enum LocationRestrictionType {
		box, sphere, world, global
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
	public LocationRestrictionType locationRestrictionType = LocationRestrictionType.global;

	/**
	 * The location to use for the radius - Only used with sphere searches
	 */
	public Location radiusLocation;

	/**
	 * The radius of the search - Only used with sphere searches
	 */
	public int radius;

	/**
	 * The first position in the box - Only used with box searches
	 */
	public Location pos1;

	/**
	 * The second position in the box - Only used with box searches
	 */
	public Location pos2;

	/**
	 * The world used for the search - Only used with world searches
	 */
	public World world;
	
	/**
	 * A list of players to restrict to
	 */
	public List<String> restrictPlayers;

	/**
	 * A list of players to exclude
	 */
	public List<String> excludePlayers;

	/**
	 * Copy Constructor
	 * @param other Other object to copy from
	 */
	public SearchSettings(SearchSettings other) {
		if (other == null) return;
		this.timezoneHours = other.timezoneHours;
		this.fromTime = other.fromTime == null ? null : new Date(other.fromTime.getTime());
		this.toTime = other.toTime == null ? null : new Date(other.toTime.getTime());
		this.locationRestrictionType = other.locationRestrictionType;
		this.radiusLocation = other.radiusLocation == null ? null :
			new Location(other.radiusLocation.getWorld(), other.radiusLocation.getX(),
			other.radiusLocation.getY(), other.radiusLocation.getZ());
		this.radius = other.radius;
		this.pos1 = other.pos1 == null ? null : new Location(other.pos1.getWorld(), other.pos1.getX(), other.pos1.getY(), other.pos1.getZ());
		this.pos2 = other.pos2 == null ? null : new Location(other.pos2.getWorld(), other.pos2.getX(), other.pos2.getY(), other.pos2.getZ());
		this.world = other.world;
		this.restrictPlayers = other.restrictPlayers == null ? new ArrayList<>() : new ArrayList<>(other.restrictPlayers);
		this.excludePlayers = other.excludePlayers == null ? new ArrayList<>() : new ArrayList<>(other.excludePlayers);
	}
}