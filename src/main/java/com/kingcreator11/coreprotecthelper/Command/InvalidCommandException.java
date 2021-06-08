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

/**
 * Exception class for invalid commands
 */
public class InvalidCommandException extends Exception {

	/**
	 * The error message of whats wrong with the command
	 */
	private String msg;

	/**
	 * Creates a new invalid command exception
	 * @param msg The message of whats wrong with the command
	 */
	public InvalidCommandException(String msg) {
		super(msg);
		this.msg = msg;
	}

	/**
	 * Gets the message of whats wrong with the command
	 * @return The message of whats wrong with the command
	 */
	public String getMsg() {
		return msg;
	}
}