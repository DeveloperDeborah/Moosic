package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.inventory.RunsafeInventoryType;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CustomJukeboxRepository extends Repository
{
	public CustomJukeboxRepository(IConsole console, IServer server)
	{
		this.console = console;
		this.server = server;
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "moosic_jukeboxes";
	}

	public void storeJukebox(ILocation location, RunsafeMeta item)
	{
		RunsafeInventory holder = server.createInventory(null, RunsafeInventoryType.CHEST);

		if (holder != null)
		{
			holder.addItems(item);

			this.database.execute(
				"INSERT INTO `moosic_jukeboxes` (world, x, y, z, item) VALUES(?, ?, ?, ?, ?)",
				location.getWorld().getName(),
				location.getBlockX(),
				location.getBlockY(),
				location.getBlockZ(),
				holder.serialize()
			);
		}
	}

	public void deleteJukeboxes(ILocation location)
	{
		this.database.execute(
			"DELETE FROM `moosic_jukeboxes` WHERE world = ? AND x = ? AND y = ? AND z = ?",
			location.getWorld().getName(),
			location.getBlockX(),
			location.getBlockY(),
			location.getBlockZ()
		);
	}

	public List<CustomJukebox> getJukeboxes()
	{
		List<CustomJukebox> jukeboxes = new ArrayList<>();
		for (IRow node : this.database.query("SELECT world, x, y, z, item FROM moosic_jukeboxes"))
		{
			ILocation location = node.Location();
			if (location == null)
			{
				console.logError("Tried initializing jukebox at null location from database! (%s)", node.String("world"));
				continue;
			}
			RunsafeInventory holder = server.createInventory(null, RunsafeInventoryType.CHEST);

			if (holder != null)
			{
				holder.unserialize(node.String("item"));
				jukeboxes.add(new CustomJukebox(location, holder.getContents().get(0)));
			}
		}
		return jukeboxes;
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE `moosic_jukeboxes` (" +
				"`world` VARCHAR(50) NOT NULL," +
				"`x` DOUBLE NOT NULL," +
				"`y` DOUBLE NOT NULL," +
				"`z` DOUBLE NOT NULL," +
				"`item` longtext" +
			")"
		);

		return update;
	}

	private final IConsole console;
	private final IServer server;
}
