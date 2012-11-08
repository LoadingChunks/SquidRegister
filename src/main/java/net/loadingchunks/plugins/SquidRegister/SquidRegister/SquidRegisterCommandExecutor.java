package net.loadingchunks.plugins.SquidRegister.SquidRegister;

/*
    This file is part of SquidRegister

    Foobar is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Foobar is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SquidRegisterCommandExecutor implements CommandExecutor {

    private SquidRegister plugin;

    public SquidRegisterCommandExecutor(SquidRegister plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("register")) {
        	if(!(sender instanceof Player))
        	{
        		sender.sendMessage("You must be a player in-game to use this command.");
        		return true;
        	}
        	
        	if(!sender.isOp() && !sender.hasPermission("squidregister.register"))
        	{
        		sender.sendMessage("You are already registered or do not have permission to access that command!");
        		return true;
        	}
        	
        	Player p = (Player)sender;
        	
        	if(args.length == 0)
        		return false;
        	
        	String username = "";
        	String email = "";
        	String mcusername = "";
        	
        	if(args.length == 2)
        		username = args[1];
        	else
        		username = p.getName();
        	
        	mcusername = p.getName();
        	
        	email = args[0];

        	SquidHttpManager http = new SquidHttpManager(this.plugin);
        	
        	String response = http.registerUser(username, email, mcusername);
        	
        	if(response.equals("invalid_key"))
        	{
        		this.plugin.getLogger().warning("Invalid Endpoint Key!");
        		return false;
        	}
        	else if(response.equals("success"))
        	{
        		sender.sendMessage("Registered successfully.");
        		return true;
        	} else if(response.equalsIgnoreCase("duplicate_user"))
        	{
        		sender.sendMessage("A user has already registered with the provided username (" + username + "), please choose another.");
        		return false;
        	} else if(response.equalsIgnoreCase("duplicate_email"))
        	{
        		sender.sendMessage("A user has already registered with the provided email address (" + email + "), please choose another");
        		return false;
        	} else if(response.equalsIgnoreCase("invalid_user"))
        	{
        		sender.sendMessage("The username specified is not valid, please choose another.");
        		return false;
        	} else if(response.equalsIgnoreCase("invalid_email"))
        	{
        		sender.sendMessage("The email specified is not valid, please choose another.");
        		return false;
        	}
        }
        return false;
    }
}
