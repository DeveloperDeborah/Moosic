package no.runsafe.moosic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicTrack
{
	public MusicTrack(File songFile) throws Exception
	{
		DataInputStream stream = new DataInputStream(new BufferedInputStream(Files.newInputStream(songFile.toPath())));
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		while (stream.available() > 0)
			bytes.write(stream.readByte());

		ByteBuffer buffer = ByteBuffer.wrap(bytes.toByteArray());
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		this.length = buffer.getShort(); // Song length. 0 if new format

		int versionNum = 0;
		if (this.length == 0)
		{
			versionNum = buffer.get(); // NBS version
			buffer.get(); // Vanilla instrument count

			if (versionNum >= 3)
				this.length = buffer.get();
			else
				this.length = 200; // set arbitrary value to get it to work for versions 1-2
		}

		buffer.getShort(); // Layers
		this.songName = readString(buffer);

		// We pull this data but have no need for it.
		readString(buffer); // Song author
		readString(buffer); // Original song author
		readString(buffer); // Description
		this.tempo = buffer.getShort() / 100;
		buffer.get(); // Auto-save
		buffer.get(); // Auto-save duration
		buffer.get(); // Time sig
		buffer.getInt(); // Minutes spent
		buffer.getInt(); // Left clicks
		buffer.getInt(); // Right clicks
		buffer.getInt(); // Blocks added
		buffer.getInt(); // Blocks removed
		readString(buffer); // Midi/Schematic

		if (versionNum >= 4)
		{
			buffer.get(); // Loop on/off
			buffer.get(); // Max loop count
			buffer.getShort(); // Loop start tick
		}

		short tick = -1;
		short jumps;

		while (true)
		{
			jumps = buffer.getShort();
			if (jumps == 0)
				break;

			tick += jumps;

			while (true)
			{
				jumps = buffer.getShort();
				if (jumps == 0)
					break;

				byte inst = buffer.get();
				byte key = buffer.get();

				byte volume = 100;
				short pitch = 0;

				if(versionNum >= 4)
				{
					volume = buffer.get();
					buffer.get(); // stereo, from 0 - 200
					pitch = buffer.getShort();
				}

				if (!this.notes.containsKey(tick))
					this.notes.put(tick, new ArrayList<>());

				this.notes.get(tick).add(new NoteBlockSound(new NoteBlockInstrument(inst), key, volume, pitch));
			}
		}
	}

	private String readString(ByteBuffer buffer)
	{
		StringBuilder string = new StringBuilder();
		int stringLength = buffer.getInt();

		for (int i = 0; i < stringLength; i++)
			string.append((char) buffer.get());

		return string.toString();
	}

	public String getSongName()
	{
		return this.songName;
	}

	public int getTempo()
	{
		return this.tempo;
	}

	public short getLength()
	{
		return this.length;
	}

	public List<NoteBlockSound> getNoteBlocksAtTick(short tick)
	{
		if (this.notes.containsKey(tick))
			return this.notes.get(tick);

		return new ArrayList<>();
	}

	private final String songName;
	private final int tempo;
	private short length;
	private final HashMap<Short, List<NoteBlockSound>> notes = new HashMap<>();
}
