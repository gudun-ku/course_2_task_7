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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    @Query("select * from album")
    List<Album> getAlbums();

    @Query("select * from album")
    Cursor getAlbumsCursor();

    @Query("select * from album where id = :albumId")
    Cursor getAlbumWithIdCursor(int albumId);

    //получить список песен переданного id альбома
    @Query("select song.* from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

    //обновить информацию об альбоме
    @Update
    int updateAlbumInfo(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    int deleteAlbumById(int albumId);

    @Delete
    void deleteAlbum(Album album);

    @Query("DELETE FROM album")
    void deleteAllAlbums();

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

    ////////////////////////////////////////////////////

    //удалить песню по Id
    @Query("DELETE FROM song where id = :songId")
    int deleteSongById(int songId);

    @Delete
    void deleteSong(Song song);

    @Query("DELETE FROM song")
    void deleteAllSongs();

    //связи альбом - песня
    @Insert
    long setLinkAlbumSong(AlbumSong albumSong);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

    // получить связи альбом - песня
    @Query("select * from albumsong")
    List<AlbumSong> getAlbumSongs();

    @Query("select * from albumsong")
    Cursor getAlbumSongsCursor();

    //получить связь из альбома по id связи
    @Query("select * from albumsong where id = :albumSongId")
    Cursor getAlbumSongWithIdCursor(int albumSongId);

    //получить связи из альбома по id альбома и id песни
    @Query("select * from albumsong where album_id = :albumSongAlbumId and song_id = :albumSongSongId")
    Cursor getAlbumSongWithAlbumIdSongIdCursor(int albumSongAlbumId, int albumSongSongId);


    //обновить связь альбом - песня
    @Update
    int updateLinkAlbumSong(AlbumSong albumSong);

    //Удалить связь песни и альбом по Id
    @Query("DELETE FROM albumsong where id = :songAlbumId")
    int deleteAlbumSongById(int songAlbumId);

    //Удалить связь песни и альбома
    @Delete
    void deleteAlbumSong(AlbumSong albumSong);

    //Удалить связь по Id песни и Id альбома
    @Query("DELETE FROM albumsong  where album_id = :albumSongAlbumId and song_id = :albumSongSongId")
    int deleteAlbumSongById(int albumSongAlbumId, int albumSongSongId);

    @Query("DELETE FROM albumsong")
    void deleteAllAlbumSongs();

}
