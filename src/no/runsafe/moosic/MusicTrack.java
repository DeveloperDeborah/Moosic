package no.runsafe.moosic;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicTrack
{
	public MusicTrack(Path songFile) throws Exception
	{
		ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(songFile));

		this.length = buffer.getShort(); // Song length
		Plugin.output.write("Length: " + this.length);
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
			byte inst = buffer.get();
			byte key = buffer.get();

			if (!this.notes.containsKey(tick))
				this.notes.put(tick, new ArrayList<NoteBlockSound>());

			this.notes.get(tick).add(new NoteBlockSound(new NoteBlockInstrument(inst), key));
		}
	}

	private String readString(ByteBuffer buffer) throws Exception
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
	private HashMap<Short, List<NoteBlockSound>> notes = new HashMap<Short, List<NoteBlockSound>>();
}
