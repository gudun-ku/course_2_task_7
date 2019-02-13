package elegion.com.roomdatabase;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

import elegion.com.roomdatabase.database.Album;
import elegion.com.roomdatabase.database.MusicDao;
import elegion.com.roomdatabase.database.MusicDatabase;

public class MusicProvider extends ContentProvider {

    private static final String TAG = MusicProvider.class.getSimpleName();

    private static final String AUTHORITY = "com.elegion.roomdatabase.musicprovider";
    private static final String TABLE_ALBUM = "album";
    public static final String TABLE_SONG = "song";
    public static final String TABLE_ALBUM_SONG = "albumsong";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private enum TableCode {

        ALBUM_TABLE_CODE(100, TABLE_ALBUM),
        ALBUM_ROW_CODE(101, TABLE_ALBUM),
        SONG_TABLE_CODE(200, TABLE_SONG),
        SONG_ROW_CODE (201, TABLE_SONG),
        ALBUM_SONG_TABLE_CODE(300, TABLE_ALBUM_SONG),
        ALBUM_SONG_ROW_CODE(301, TABLE_ALBUM_SONG),
        ALBUM_SONG_ALBUM_CODE(400, TABLE_ALBUM_SONG),
        ALBUM_SONG_ALBUM_SONG_CODE(500, TABLE_ALBUM_SONG);

        private int VALUE;
        private String TABLE;

        private TableCode(int value, String table) {
            this.VALUE = value;
            this.TABLE = table;
        }

        public static boolean contains(int value) {
            TableCode[] values = TableCode.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].VALUE == value)
                    return true;
            }
            return false;
        }

    }

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM, TableCode.ALBUM_TABLE_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM + "/*", TableCode.ALBUM_ROW_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_SONG, TableCode.SONG_TABLE_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_SONG + "/*", TableCode.SONG_ROW_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM_SONG, TableCode.ALBUM_SONG_TABLE_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM_SONG + "/" + TableCode.ALBUM_ROW_CODE.VALUE ,TableCode.ALBUM_SONG_ALBUM_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM_SONG + "/" +  TableCode.ALBUM_ROW_CODE.VALUE + "/" + TableCode.SONG_ROW_CODE.VALUE, TableCode.ALBUM_SONG_ALBUM_SONG_CODE.VALUE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM_SONG + "/*", TableCode.ALBUM_SONG_ROW_CODE.VALUE);
    }

    private MusicDao mMusicDao;

    public MusicProvider() {
    }

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            mMusicDao = Room.databaseBuilder(getContext().getApplicationContext(), MusicDatabase.class, "music_database")
                    .build()
                    .getMusicDao();
            return true;
        }

        return false;
    }

    @Override
    public String getType(Uri uri) {
        int value = URI_MATCHER.match(uri);

        if (!TableCode.contains(value)) throw new UnsupportedOperationException("not yet implemented");
        return TableCode.values()[value].TABLE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = URI_MATCHER.match(uri);

        if (!TableCode.contains(code)) return null;

        Cursor cursor;
        //switch here
        if (code == TableCode.ALBUM_TABLE_CODE.VALUE) {
            cursor = mMusicDao.getAlbumsCursor();
        } else {
            cursor = mMusicDao.getAlbumWithIdCursor((int) ContentUris.parseId(uri));
        }
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) == TableCode.ALBUM_TABLE_CODE.VALUE && isValuesValid(values)) {
            Album album = new Album();
            Integer id = values.getAsInteger("id");
            album.setId(id);
            album.setName(values.getAsString("name"));
            album.setReleaseDate(values.getAsString("release"));
            mMusicDao.insertAlbum(album);
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new IllegalArgumentException("cant add multiple items");
        }
    }

    private boolean isValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("release");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == TableCode.ALBUM_ROW_CODE.VALUE && isValuesValid(values)) {
            Album album = new Album();
            int id = (int) ContentUris.parseId(uri);
            album.setId(id);
            album.setName(values.getAsString("name"));
            album.setReleaseDate(values.getAsString("release"));
            int updatedRows = mMusicDao.updateAlbumInfo(album);
            return updatedRows;
        } else {
            throw new IllegalArgumentException("cant add multiple items");
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == TableCode.ALBUM_ROW_CODE.VALUE) {
            int id = (int) ContentUris.parseId(uri);
            return mMusicDao.deleteAlbumById(id);
        } else {
            throw new IllegalArgumentException("cant add multiple items");
        }

    }
}
