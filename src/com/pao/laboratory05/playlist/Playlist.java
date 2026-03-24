package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song){
        Song[] copie = new Song[songs.length + 1];
        System.arraycopy(songs, 0 , copie, 0 , songs.length);
        copie[copie.length - 1 ] = song;
        songs = copie;
    }

    public void printSortedByTitle(){
        Song[] copie = songs;
        Arrays.sort(copie);
        System.out.println(Arrays.toString(copie));
    }

    public void printSortedByDuration(){
        Song[] copie = songs;
        Arrays.sort(copie, new SongDurationComparator());
        System.out.println(Arrays.toString(copie));
    }

    public int getTotalDuration(){
        int suma = 0;
        for(Song s : songs){
            suma += s.durationSeconds();
        }
        return suma;
    }
}
