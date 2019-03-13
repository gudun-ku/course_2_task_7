package elegion.com.roomdatabase.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * @author Azret Magometov
 */

@Database(entities = {Album.class, Song.class, AlbumSong.class}, version = 2)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract MusicDao getMusicDao();

}
