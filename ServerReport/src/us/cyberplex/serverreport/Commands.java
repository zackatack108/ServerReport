package us.cyberplex.serverreport;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import us.cyberplex.serverreport.ReportData.ReportType;

public class Commands implements CommandExecutor{

	Main main = Main.getMain();
	ReportData data = new ReportData();

	String noPermission = ChatColor.RED + "Sorry, you don't have permisson for that command",
			systemAdmin = ChatColor.RED + "Please contact your server administrator if you believe this is wrong",
			invalidCmd = ChatColor.RED + "Sorry, the command was entered wrong",
			helpMsg = ChatColor.RED + "Do /report help for a list of commands",
			missingComponent = ChatColor.RED + "Sorry, a component of the command is missing";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

		Player player = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("report")) {

			if(args.length >= 1) {

				switch(args[0].toLowerCase()) {

				case "player":

					if(args.length >= 2) {

						StringBuilder buffer = new StringBuilder();

						for(int index = 1; index < args.length; index++) {
							buffer.append(args[index]).append(' ');
						}

						String message = buffer.toString();

						data.setReport(player, ReportType.PLAYER, message);
						player.sendMessage(ChatColor.YELLOW + "Report created");					
						Bukkit.broadcast(ChatColor.YELLOW + "Report created by " + player.getName(), "report.admin");

					} else {

						player.sendMessage(missingComponent);
						player.sendMessage(helpMsg);

					}

					break;

				case "bug":

					if(args.length >= 2) {

						StringBuilder buffer = new StringBuilder();

						for(int index = 1; index < args.length; index++) {
							buffer.append(args[index]).append(' ');
						}

						String message = buffer.toString();

						data.setReport(player, ReportType.BUG, message);
						player.sendMessage(ChatColor.YELLOW + "Report created");
						Bukkit.broadcast(ChatColor.YELLOW + "Report created by " + player.getName(), "report.admin");

					} else {

						player.sendMessage(missingComponent);
						player.sendMessage(helpMsg);

					}

					break;

				case "list":

					if(player.hasPermission("report.list")) {

						//get arenas from config and save them to arena list
						Set<String> reports = main.getConfig().getConfigurationSection("Reports").getKeys(false);
						String[] reportsList = new String[reports.size()];
						reports.toArray(reportsList);

						//display arena list to user
						String reportMsg = new String();
						for(String report : reportsList) {
							reportMsg += report + ", ";
						}

						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_RED +  "Server Reports");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.GRAY + reportMsg);

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "read":

					if(player.hasPermission("report.read")) {

						if(args.length == 2) {

							if(main.getConfig().contains("Reports." + args[1])) {

								int reportNum = Integer.parseInt(args[1]);
								data.getReport(reportNum, player);

							} else {

								player.sendMessage(ChatColor.RED + "Sorry, the report doesn't exist");
								player.sendMessage(ChatColor.RED + "Please do /report list for a list of reports");

							}

						}

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "archive":

					if(player.hasPermission("report.archive")) {

						if(main.getConfig().contains("Reports." + args[1])) {

							int reportNum = Integer.parseInt(args[1]);
							data.setArchived(reportNum);
							player.sendMessage(ChatColor.YELLOW + "Report archived");

						} else {							
							player.sendMessage(ChatColor.RED + "Sorry, the report doesn't exist");
							player.sendMessage(ChatColor.RED + "Please do /report list for a list of reports");
						}

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "total":

					if(player.hasPermission("report.total")) {

						player.sendMessage(ChatColor.RED + "Total Reports: " + ChatColor.WHITE + (data.getReportsCount()-1));

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "help":

					if(player.hasPermission("report.help.admin")) {

						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_AQUA +  "Server Reports Commands ");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.YELLOW + "/report player [Description]: " + ChatColor.WHITE + "Report a player");
						player.sendMessage(ChatColor.YELLOW + "/report bug [Description]: " + ChatColor.WHITE + "Report a bug");
						player.sendMessage(ChatColor.YELLOW + "/report list: " + ChatColor.WHITE + "List the current reports");
						player.sendMessage(ChatColor.YELLOW + "/report read [Number]: " + ChatColor.WHITE + "Read a report");
						player.sendMessage(ChatColor.YELLOW + "/report archive [Number]: " + ChatColor.WHITE + "Archive a report");
						player.sendMessage(ChatColor.YELLOW + "/report total: " + ChatColor.WHITE + "List the total number of reports");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");




					} else {

						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_AQUA +  "Server Reports Commands ");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.YELLOW + "/report player [Description]: " + ChatColor.WHITE + "Report a player");
						player.sendMessage(ChatColor.YELLOW + "/report bug [Description]: " + ChatColor.WHITE + "Report a bug");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

					}

					break;

				default:

					player.sendMessage(invalidCmd);
					player.sendMessage(helpMsg);

					break;

				}

			} else {

				player.sendMessage(invalidCmd);
				player.sendMessage(helpMsg);

			}

		}

		if(cmd.getName().equalsIgnoreCase("archive")) {

			if(args.length >= 1) {

				switch(args[0].toLowerCase()) {

				case "total":

					if(player.hasPermission("archive.total")) {

						player.sendMessage(ChatColor.RED + "Total Archived Reports: " + ChatColor.WHITE + (data.getArchivedCount()-1));

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "read":

					if(player.hasPermission("archive.read")) {

						if(args.length == 2) {

							if(main.getConfig().contains("Archived." + args[1])) {

								int reportNum = Integer.parseInt(args[1]);
								data.getArchived(reportNum, player);

							} else {

								player.sendMessage(ChatColor.RED + "Sorry, the report doesn't exist");
								player.sendMessage(ChatColor.RED + "Please do /archived list for a list of reports");

							}

						} else {

							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "list":

					if(player.hasPermission("archive.list")) {

						//get arenas from config and save them to arena list
						Set<String> reports = main.getConfig().getConfigurationSection("Archived").getKeys(false);
						String[] reportsList = new String[reports.size()];
						reports.toArray(reportsList);

						//display arena list to user
						String reportMsg = new String();
						for(String report : reportsList) {
							reportMsg += report + ", ";
						}

						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_RED +  "Server Archived Reports");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.GRAY + reportMsg);

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "help":

					if(player.hasPermission("archive.help")) {

						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_AQUA +  "Server Reports Archive Commands ");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.YELLOW + "/archive list: " + ChatColor.WHITE + "List the current reports");
						player.sendMessage(ChatColor.YELLOW + "/archive read [Number]: " + ChatColor.WHITE + "Read a report");
						player.sendMessage(ChatColor.YELLOW + "/archive total: " + ChatColor.WHITE + "List the total number of reports");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

					} else {

						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				default:

					player.sendMessage(invalidCmd);
					player.sendMessage(ChatColor.RED + "Do /archive help for a list of commands");

					break;

				}

			} else {
				
				player.sendMessage(invalidCmd);
				player.sendMessage(ChatColor.RED + "Do /archive help for a list of commands");
				
			}
			
		}

		return false;
	}

}
