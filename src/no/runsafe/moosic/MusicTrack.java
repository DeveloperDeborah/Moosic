package no.runsafe.moosic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicTrack
{
	public MusicTrack(File songFile) throws Exception
	{
		DataInputStream data = new DataInputStream(new BufferedInputStream(new FileInputStream(songFile)));

		this.length = data.readShort(); // Song length
		Plugin.output.write("Length: " + this.length);
		Plugin.output.write("Layers: " + data.readShort()); // Layers
		this.songName = readString(data);

		// We pull this data but have no need for it.
		readString(data); // Song author
		readString(data); // Original song author
		readString(data); // Description
		this.tempo = data.readShort() / 100;
		data.readByte(); // Auto-save
		data.readByte(); // Auto-save duration
		data.readByte(); // Time sig
		data.readInt(); // Minutes spent
		data.readInt(); // Left clicks
		data.readInt(); // Right clicks
		data.readInt(); // Blocks added
		data.readInt(); // Blocks removed
		readString(data); // Midi/Schematic

		short tick = -1;
		short jumps;

		while (true)
		{
			jumps = data.readShort();
			if (jumps == 0)
				break;

			tick += jumps;
			byte inst = data.readByte();
			byte key = data.readByte();

			if (!this.notes.containsKey(tick))
				this.notes.put(tick, new ArrayList<NoteBlockSound>());

			this.notes.get(tick).add(new NoteBlockSound(new NoteBlockInstrument(inst), key));
		}
	}

	private String readString(DataInputStream data) throws Exception
	{
		StringBuilder string = new StringBuilder();
		int stringLength = data.readInt();

		for (int i = 0; i < stringLength; i++)
			string.append((char) data.readByte());

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
	private HashMap<Short, List<NoteBlockSound>> notes = new HashMap<Short, List<NoteBlockSound>>();
}
