package no.runsafe.moosic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicTrack
{
	public MusicTrack(File songFile) throws Exception
	{
		DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(songFile)));
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		while (stream.available() > 0)
			bytes.write(stream.readByte());

		ByteBuffer buffer = ByteBuffer.wrap(bytes.toByteArray());
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		this.length = buffer.getShort(); // Song length
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

				if (!this.notes.containsKey(tick))
					this.notes.put(tick, new ArrayList<NoteBlockSound>());

				this.notes.get(tick).add(new NoteBlockSound(new NoteBlockInstrument(inst), key));
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

		return new ArrayList<NoteBlockSound>();
	}

	private String songName;
	private int tempo;
	private short length;
	private final HashMap<Short, List<NoteBlockSound>> notes = new HashMap<Short, List<NoteBlockSound>>();
}
