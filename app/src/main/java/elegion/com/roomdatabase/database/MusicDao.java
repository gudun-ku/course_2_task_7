package elegion.com.roomdatabase.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

/**
 * @author Azret Magometov
 */

@Dao
public interface MusicDao {

    // ALBUMS
    // получить альбом
    @Query("select * from album")
    List<Album> getAlbums();

    @Query("select * from album")
    Cursor getAlbumsCursor();

    @Query("select * from album where id = :albumId")
    Cursor getAlbumWithIdCursor(int albumId);

    //вставить альбом
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    //обновить информацию об альбоме
    @Update
    int updateAlbumInfo(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    int deleteAlbumById(int albumId);

    @Delete
    void deleteAlbum(Album album);

    // SONGS

    @Query("select * from song")
    List<Song> getSongs();

    @Query("select * from song")
    Cursor getSongsCursor();

    @Query("select * from song where id = :songId")
    Cursor getSongWithIdCursor(int songId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    //вставить песню
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);

    //обновить песню
    @Update
    int updateSongInfo(Song song);

    //удалить песню по Id
    @Delete
    void deleteSongById(int songId);

    @Delete
    void deleteSong(Song song);

    //связи альбом - песня

    // получить альбом
    @Query("select * from albumsong")
    List<AlbumSong> getAlbumSongs();

    @Query("select * from albumsong")
    Cursor getAlbumSongsCursor();

    //получить песню из альбома по id связи?
    @Query("select * from albumsong where id = :albumSongId")
    Cursor getAlbumSongWithIdCursor(int albumSongId);

    //вставить связь песни и альбома
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

    //Удалить связь песни и альбом по Id
    @Delete
    void deleteAlbumSongById(int songAlbumId);

    //Удалить связь песни и альбома
    @Delete
    void deleteAlbumSongByAlbumAndSong(int songId, int albumId);

    //получить список песен переданного id альбома
    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

}
