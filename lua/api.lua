-- Song object
Song = class('Song');

function Song:initialize(location, songFile, volume)
    self.id = Moosic.song.playSong(location.world, location.x, location.y, location.z, songFile, volume);
end

function Song:stop()
    Moosic.song.stopSong(self.id);
end