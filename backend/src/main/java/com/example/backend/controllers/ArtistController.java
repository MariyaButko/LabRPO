package com.example.backend.controllers;

import com.example.backend.models.Country;
import com.example.backend.repositories.CountryRepository;
import com.example.backend.tools.DataValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.models.Artist;
import com.example.backend.repositories.ArtistRepository;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    public long findByName(String cName){
        long id = 252;
        for (long index = 0; index < 251; index++){
            Optional <Country> cc = countryRepository.findById(index);
            if (cc.isPresent()) {
                Country countr = cc.get();
                if (cName.equalsIgnoreCase(countr.name)){
                    id = index;
                }
            }
        }
        return id;
    }

    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/artists")
    public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") Long artistId)
            throws DataValidationException
    {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist) throws Exception {
        try {

            long ind = findByName(artist.country.name);
            if (ind > 251){
                throw new Exception("Неизвестная страна");
            }
            Optional<Country>
                    cc = countryRepository.findById(ind);
            cc.ifPresent(country -> artist.country = country);
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error = "undefinederror";
            Map<String, String> map =  new HashMap<>();
            map.put("error", error);
            System.out.println(error);
            return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }
    @PostMapping("/deleteartists")
    public ResponseEntity<HttpStatus> deleteArtists(@RequestBody List<Artist> artists) {
        System.out.println(artists.get(0).toString());
        List<Long> listOfIds = new ArrayList<>();
        for (Artist artist : artists) {
            listOfIds.add(artist.id);
        }
        artistRepository.deleteAllById(listOfIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                               @RequestBody Artist artist) {
        Artist artistt = null;
        Optional<Artist> cc = artistRepository.findById(artistId);
        if (cc.isPresent()) {
            artistt = cc.get();
            artistt.name = artist.name;
            artistt.age = artist.age;
            System.out.println(artistt.country.name);
            artistRepository.save(artistt);
            return ResponseEntity.ok(artistt);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

}


/*@RestController
@RequestMapping("/api/v1")

public class ArtistController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/artists")
    public List
    getAllArtists() {
        return artistRepository.findAll();
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist)
            throws Exception {
        try {
            Optional<Country> cc = countryRepository.findById(artist.country.id);
            if (cc.isPresent()) {
                artist.country = cc.get();
            }
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                error = "artistalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                               @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist>
                cc = artistRepository.findById(artistId);
        if (cc.isPresent()) {
            artist = cc.get();
            artist.name = artistDetails.name;
            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }


    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist>
                artist = artistRepository.findById(artistId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

*/



