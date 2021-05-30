# Core-Protect-Helper
Main Github Link: https://github.com/KingCreator11/Core-Protect-Helper

## Simple Description
This is a simple plugin which adds a few search commands to make some searching easier with the core protect database.

## Disclaimer
I am not affiliated with core protect or its creators in any way. If you are affiliated with core protect and wish for this resource to be removed then please contact me via an issue on the repository and I will private/remove this resource and any reuploads of the jar I have posted. Licensing information can be found in the License file.

## Commands
For all commands, you can use `/cohelper` or `/coh`, both do the same thing.

### Search Commands
| Command | Permissions | Description |
|---------|-------------|-------------|
| `/coh search container <add/remove/all> (OPTIONAL)<list of blocks/items> (OPTIONAL)e:<list of blocks/items>` | coreprotect.search.container | Searches through container logs given a list of blocks/items, and a list of blocks/items to exclude |
| `/coh search kill (OPTIONAL)<list of mobs> (OPTIONAL)e:<list of mobs>` | coreprotect.search.kill | Searches through kill logs given a list of mobs/players, and a list of mobs/players to exclude. |
| `/coh search blocks <place/break/all> (OPTIONAL)<list of blocks> (OPTIONAL)e:<list of blocks>` | coreprotect.search.blocks | Searches through blocklogs given a list of blocks, and a list of blocks to exclude |
| `/coh search users <keyword>` | coreprotecthelper.search.users | Searches for a user's in game name based on keywords - useful for when you know someone's name but they have annoying numbers |
| `/coh search chat <keyword/message>` | coreprotecthelper.search.chat | Searches for specific keywords within the chat logs |
| `/coh search sign <keyword/message>` | coreprotecthelper.search.sign | Searches for specific keywords within the sign logs |
| `/coh search command <keyword/message>` | coreprotecthelper.search.command | Searches for specific keywords within the command logs |

### Saving Commands
| Command | Permissions | Description |
|---------|-------------|-------------|
| `/coh savesearch <name>` | coreprotecthelper.usage | Saves a search for future usage - all saved searches are deleted when you log off |
| `/coh deletesearch <name>` | coreprotecthelper.usage | Deletes a saved search |
| `/coh sendsearch <ign>` | coreprotecthelper.usage | Sends a search to another player, the other player must have `coreprotecthelper.usage`. Both players must be online. |

### Filtering Commands

#### Timeframe Commands
| Command | Permissions | Description |
|---------|-------------|-------------|
| `/coh settimezone +/-n` | coreprotecthelper.usage | Sets the timezone shift for timeframes |
| `/coh settimeframe fromtimepast <days>d<hours>h<minutes>m - <days>d<hours>h<minutes>m` | coreprotecthelper.usage | Restricts future searches for the user to the time range given. Each time variable is the time past from now eg 5d - 1d is between the past 5-1 days. |
| `/coh settimeframe fromdates <+/-><hours from utc> <year>-<month>-<day>:<hours>:<minutes> - <year>-<month>-<day>:<hours>:<minutes>` | coreprotecthelper.usage | Restricts future searches for the user to the time range given. Each time variable is the time past from now eg 5d - 1d is between the past 5-1 days. |
| `/coh settimeframe fromtime <days>d<hours>h<minutes>m` | coreprotecthelper.usage | Sets the timeframe from the time of the search to the past n time |

#### User Commands
| Command | Permissions | Description |
|---------|-------------|-------------|
| `/coh restrictusers list <list of users separated by ,>` | coreprotecthelper.usage | Restricts the search to only include users in the list |
| `/coh restrictusers fromsearch name` | coreprotecthelper.usage | Restricts the search to only include users from a user search named `name` |
| `/coh excludeusers list <list of users separated by ,>` | coreprotecthelper.usage | Excludes the users in the list from the search |
| `/coh excludeusers fromsearch name` | coreprotecthelper.usage | Excludes the users from a search from a user search named `name` |

#### Location Commands
| Command | Permissions | Description |
|---------|-------------|-------------|
| `/coh setloc x-y-z x-y-z` | coreprotecthelper.usage | Restricts the location of the search to be in the box created between the two coords |
| `/coh setpos1` | coreprotecthelper.usage | Sets position 1 of the location to the block pointed to when using this command |
| `/coh setpos2` | coreprotecthelper.usage | Sets position 2 of the location to the block pointed to when using this command |
| `/coh setradius <radius>` | coreprotecthelper.usage | Restricts the location of the search to be within the sphere created by the radius from the command executor's location |
| `/coh setworld <world>` | coreprotecthelper.usage | Restricts the location of the search to be within a certain world |

## Config

```yaml
# Whether or not to connect to a Mysql database instead of an sqlite3 database
useMysql: false

# Mysql Connection Details
mysqlHost: 127.0.0.1
mysqlPort: 3306
mysqlUser: database
mysqlPass: password
mysqlDBName: name

# Sqlite3 Database Location
sqlite3DbPath: ../CoreProtect/database.db

# The table name prefix for all core protect tables
tableNamePrefix: co_
```