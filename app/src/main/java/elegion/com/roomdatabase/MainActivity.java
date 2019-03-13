package elegion.com.roomdatabase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import elegion.com.roomdatabase.database.Album;
import elegion.com.roomdatabase.database.AlbumSong;
import elegion.com.roomdatabase.database.MusicDao;
import elegion.com.roomdatabase.database.Song;

public class MainActivity extends AppCompatActivity {
    private Button mAddBtn;
    private Button mGetBtn;
    private MusicDao mMusicDao;

    // добавить базу данных Room ----
    // вставить данные / извлечь данные ---
    // добавить контент провайдер над Room ---

    private final String CLEAR_DATA = "clear";
    private final String GENERATE_DATA = "generate";
    private final String SHOW_DATA = "show";
    private Toast mToast;

    private void showToast(String msg) {
        if (mToast != null) mToast.cancel();

        mToast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMusicDao = ((AppDelegate) getApplicationContext()).getMusicDatabase().getMusicDao();

        mAddBtn = (findViewById(R.id.add));
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBMaintenanceAsyncTask task = new DBMaintenanceAsyncTask();
                task.execute(CLEAR_DATA);
                task = new DBMaintenanceAsyncTask();
                task.execute(GENERATE_DATA);

            }
        });

        mGetBtn = findViewById(R.id.get);
        mGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBMaintenanceAsyncTask task = new DBMaintenanceAsyncTask();
                task.execute(SHOW_DATA);
            }
        });

    }


    // let's clean our tables first




    // Async Task to work with database (Loader will be better)

    public class DBMaintenanceAsyncTask extends AsyncTask<String, Void, String> {

        private String query;

        @Override
        protected String doInBackground(String... strings) {
            if (strings == null || strings.length == 0) {
                return null;
            }
            query = strings[0];
            switch (query) {
                case GENERATE_DATA:
                    // generate data
                    mMusicDao.insertAlbums(createAlbums());
                    mMusicDao.insertSongs(createSongs());
                    mMusicDao.setLinksAlbumSongs(createAlbumSongs());
                    return null;
                case CLEAR_DATA:
                    mMusicDao.deleteAllAlbumSongs();
                    mMusicDao.deleteAllSongs();
                    mMusicDao.deleteAllAlbums();
                    return null;
                case SHOW_DATA:
                    List<Album> albums = mMusicDao.getAlbums();
                    List<Song> songs = mMusicDao.getSongs();
                    List<AlbumSong> albumSongs = mMusicDao.getAlbumSongs();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0, size = albums.size(); i < size; i++) {
                        builder.append(albums.get(i).toString()).append("\n");
                    }
                    for (int i = 0, size = songs.size(); i < size; i++) {
                        builder.append(songs.get(i).toString()).append("\n");
                    }
                    for (int i = 0, size = albumSongs.size(); i < size; i++) {
                        builder.append(albumSongs.get(i).toString()).append("\n");
                    }
                    return builder.toString();
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                showToast(s);
            }
            super.onPostExecute(s);
        }
    }


    private List<Album> createAlbums() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd ,yyyy");

        List<Album> albums = new ArrayList<>(10);
        for (int i = 0; i < 3; i++) {
            albums.add(new Album(i, "album " + i, "release " + sdf.format(new Date(System.currentTimeMillis()))));
        }
        return albums;
    }

    private List<Song> createSongs() {
        List<Song> songs = new ArrayList<>(10);
        for (int i = 0; i < 3; i++) {
            songs.add(new Song(i, "song " + i, String.valueOf(i * 50) + ":" +String.valueOf(i * 11)));
        }
        return songs;
    }


    private List<AlbumSong> createAlbumSongs() {
        List<AlbumSong> albumSongs = new ArrayList<>(10);
        for (int i = 0; i < 3; i++) {
            albumSongs.add(new AlbumSong(i+1,i,i));
        }
        return albumSongs;
    }


    private void showToast(List<Album> albums, List<Song> songs, List<AlbumSong> albumSongs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = albums.size(); i < size; i++) {
            builder.append(albums.get(i).toString()).append("\n");
        }
        for (int i = 0, size = songs.size(); i < size; i++) {
            builder.append(songs.get(i).toString()).append("\n");
        }
        for (int i = 0, size = albumSongs.size(); i < size; i++) {
            builder.append(albumSongs.get(i).toString()).append("\n");
        }

        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();

    }
}
