package design_patterns.behavioral.iterator_pattern;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//class PlayList {
//    private List<String> songs = new ArrayList<>();
//
//    public void addSongs(String song){
//        songs.add(song);
//    }
//
//    public List<String> getSongs(){
//        return songs;
//    }
//}

//Not efficient way of doing the stuff

//class MusicPlayer {
//    public void playAll(PlayList playList) {
//        for (String song : playList.getSongs()) {
//            System.out.println("Playing: " + song);
//        }
//    }
//}

interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface IterableCollection<T> {
    Iterator<T> createIterator(String type);
}

class PlayList implements IterableCollection<String> {
    private List<String> songs = new ArrayList<>();

    public void addSongs(String song){
        songs.add(song);
    }

    public String getSongAt(int index){
        return songs.get(index);
    }

    public int getSize() {
        return songs.size();
    }

    @Override
    public Iterator<String> createIterator(String type) {
        return switch (type) {
            case "Reverse" -> new ReversePlayListIterator(this);
            case "Random" -> new ShufflePlaylistIterator(this);
            default -> new PlayListIterator(this);
        };
    }
}

class PlayListIterator implements Iterator<String> {
    private final PlayList playList;
    private int index = 0;

    public PlayListIterator(PlayList playList) {
        this.playList = playList;
    }

    @Override
    public boolean hasNext() {
        return index < playList.getSize();
    }

    @Override
    public String next() {
        return playList.getSongAt(index++);
    }
}

// Extended design
class ReversePlayListIterator implements Iterator<String> {
    private final PlayList playList;
    private int index;

    public ReversePlayListIterator(PlayList playList) {
        this.playList = playList;
        this.index = playList.getSize() - 1;
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public String next() {
        return playList.getSongAt(index--);
    }
}

class ShufflePlaylistIterator implements Iterator<String> {
    private final PlayList playlist;
    private final List<Integer> shuffledIndices;
    private int position = 0;

    public ShufflePlaylistIterator(PlayList playlist) {
        this.playlist = playlist;
        this.shuffledIndices = new ArrayList<>();
        for (int i = 0; i < playlist.getSize(); i++) {
            shuffledIndices.add(i);
        }
        Collections.shuffle(shuffledIndices);
    }

    @Override
    public boolean hasNext() {
        return position < shuffledIndices.size();
    }

    @Override
    public String next() {
        int index = shuffledIndices.get(position++);
        return playlist.getSongAt(index);
    }
}

public class PlaylistIteratorDemo {
    public static void main(String[] args) {
        PlayList list = new PlayList();
        list.addSongs("Shape of you");
        list.addSongs("Bohemian Rhapsody");
        list.addSongs("Binding Lights");

        Iterator<String> iterator = list.createIterator("Normal"); // iterator can be changed
        System.out.println("Now playing: ");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
