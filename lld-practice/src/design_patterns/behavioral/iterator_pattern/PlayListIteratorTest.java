package design_patterns.behavioral.iterator_pattern;

import java.util.ArrayList;
import java.util.List;


// Implement the SongCursor interface here.
interface SongCursor {
    boolean hasNext();
    String next();
}

// Implement the ForwardCursor class here.
class ForwardCursor implements SongCursor {
    private List<String> songs;
    private int idx;
    public ForwardCursor(List<String> songs){
        this.songs = songs;
        this.idx = 0;
    }

    @Override
    public boolean hasNext() {
        return idx < songs.size();
    }

    @Override
    public String next() {
        if (!hasNext()) {
            return "END";
        }
        return songs.get(idx++);
    }
}

// Implement the ReverseCursor class here.
class ReverseCursor implements SongCursor {
    private List<String> songs;
    private int idx;

    public ReverseCursor(List<String> songs){
        this.songs = songs;
        this.idx = this.songs.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return idx >= 0;
    }

    @Override
    public String next() {
        if (!hasNext()) {
            return "END";
        }
        return this.songs.get(idx--);
    }
}


// Playlist is pre-implemented. Do not change it.
class Playlist {
    private final List<String> songs = new ArrayList<>();
    private final List<SongCursor> cursors = new ArrayList<>();

    public Playlist() {
    }

    public boolean addSong(String title) {
        if (songs.size() >= 20) {
            return false;
        }
        songs.add(title);
        return true;
    }

    public int songCount() {
        return songs.size();
    }

    public int openForward() {
        cursors.add(new ForwardCursor(songs));
        return cursors.size();
    }

    public int openReverse() {
        cursors.add(new ReverseCursor(songs));
        return cursors.size();
    }

    public boolean hasNext(int cursorId) {
        SongCursor cursor = find(cursorId);
        return cursor != null && cursor.hasNext();
    }

    public String next(int cursorId) {
        SongCursor cursor = find(cursorId);
        return cursor == null ? "NONE" : cursor.next();
    }

    public int cursorCount() {
        return cursors.size();
    }

    private SongCursor find(int cursorId) {
        if (cursorId < 1 || cursorId > cursors.size()) {
            return null;
        }
        return cursors.get(cursorId - 1);
    }
}

public class PlayListIteratorTest {
    public static void main(String[] args) {

    }
}
