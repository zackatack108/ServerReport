package us.cyberplex.serverreport;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.ChatColor;

public class ReportData {

	Main main = Main.getMain();

	public enum ReportType { PLAYER, BUG }

	//----------------------------------------------------
	//Class Setters
	//----------------------------------------------------
	public void setReport(Player player, ReportType type, String desc) { //set a report to the report list

		if(player != null && desc != null && type != null) {

			int totalReports = getReportsCount();

			main.getConfig().set("Reports." + totalReports +".player", player.getUniqueId().toString());
			main.getConfig().set("Reports." + totalReports +".type", type.toString());
			main.getConfig().set("Reports." + totalReports +".desc", desc);
			main.saveConfig();

			setReportCount(totalReports);

		}

	}

	public void setArchived(int index) { //moves report to archive

		if(main.getConfig().contains("Reports." + index)) {

			String playerID = main.getConfig().getString("Reports." + index + ".player");
			String type = main.getConfig().getString("Reports." + index + ".type");
			String desc = main.getConfig().getString("Reports." + index + ".desc");			

			main.getConfig().set("Archived." + index +".player", playerID);
			main.getConfig().set("Archived." + index +".type", type);
			main.getConfig().set("Archived." + index +".desc", desc);

			main.getConfig().set("Reports." + index, null);			
			main.saveConfig();
			
			setArchivedCount(getArchivedCount());

		}


	}

	public void setReportCount(int count) { //set a new reports count

		if(count >= 0 && count <= getReportsCount()) {

			main.getConfig().set("Total reports", count);
			main.saveConfig();

		}

	}

	public void setArchivedCount(int count) { //set a new archive reports count

		if(count >= 0 && count <= getArchivedCount()) {

			main.getConfig().set("Total archived", count);
			main.saveConfig();

		}

	}

	//----------------------------------------------------
	//Class Getters
	//----------------------------------------------------
	public void getReport(int index, Player player) {

		if(main.getConfig().contains("Reports." + index) && player != null) {

			UUID playerID = UUID.fromString(main.getConfig().getString("Reports." + index + ".player"));
			String author;
			OfflinePlayer offline;
			
			if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerID))) {
				author = Bukkit.getPlayer(playerID).getName();
			} else {
				offline = Bukkit.getOfflinePlayer(playerID);
				author = offline.getName();
			}
			
			String description = main.getConfig().getString("Reports." + index + ".desc");
			String reportType = main.getConfig().getString("Reports." + index + ".type");

			ItemStack report = new ItemStack(Material.WRITTEN_BOOK);			
			BookMeta bookMeta = (BookMeta) report.getItemMeta();
			
			bookMeta.setTitle(ChatColor.DARK_RED + "Report: " + index + ChatColor.YELLOW + " Type: " + reportType);
			bookMeta.setAuthor(author);
			bookMeta.addPage(ChatColor.DARK_RED + "Reporter: " + ChatColor.DARK_GRAY + author + "\n" +
					ChatColor.DARK_RED + "Desc: " + ChatColor.DARK_GRAY + description);
			
			report.setItemMeta(bookMeta);			
			player.getInventory().addItem(report);
			
		}

	}

	public void getArchived(int index, Player player) {
		
		if(main.getConfig().contains("Archived." + index) && player != null) {

			UUID playerID = UUID.fromString(main.getConfig().getString("Archived." + index + ".player"));
			String author;
			OfflinePlayer offline;
			
			if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerID))) {
				author = Bukkit.getPlayer(playerID).getName();
			} else {
				offline = Bukkit.getOfflinePlayer(playerID);
				author = offline.getName();
			}
			
			String description = main.getConfig().getString("Archived." + index + ".desc");
			String reportType = main.getConfig().getString("Archived." + index + ".type");

			ItemStack report = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta bookMeta = (BookMeta) report.getItemMeta();

			bookMeta.setTitle(ChatColor.DARK_RED + "Archived: " + index + ChatColor.YELLOW + " Type: " + reportType);
			bookMeta.setAuthor(author);
			bookMeta.addPage(ChatColor.DARK_RED + "Reporter: " + ChatColor.DARK_GRAY + author + "\n" +
					ChatColor.DARK_RED + "Desc: " + ChatColor.DARK_GRAY + description);
			
			report.setItemMeta(bookMeta);			
			player.getInventory().addItem(report);
			
		}

	}

	public int getReportsCount() {

		int totalReports = main.getConfig().getInt("Total reports");

		return totalReports + 1;
	}

	public int getArchivedCount() {

		int totalArchived = main.getConfig().getInt("Total archived");

		return totalArchived + 1;

	}


}
