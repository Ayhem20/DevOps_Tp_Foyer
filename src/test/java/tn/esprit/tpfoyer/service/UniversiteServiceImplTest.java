package tn.esprit.tpfoyer.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllUniversites() {
        // Préparez une liste simulée de données
        List<Universite> universites = new ArrayList<>();
        universites.add(new Universite(1L, "Universite A", "Adresse A", null));
        universites.add(new Universite(2L, "Universite B", "Adresse B", null));

        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.retrieveAllUniversites();
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void retrieveUniversite() {
        Universite universite = new Universite(1L, "Universite A", "Adresse A", null);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.retrieveUniversite(1L);
        assertNotNull(result);
        assertEquals("Universite A", result.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void addUniversite() {
        Universite universite = new Universite(1L, "Universite A", "Adresse A", null);

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addUniversite(universite);
        assertNotNull(result);
        assertEquals("Universite A", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void modifyUniversite() {
        Universite universite = new Universite(1L, "Universite A", "Adresse A", null);

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.modifyUniversite(universite);
        assertNotNull(result);
        assertEquals("Universite A", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void removeUniversite() {
        doNothing().when(universiteRepository).deleteById(1L);

        universiteService.removeUniversite(1L);

        verify(universiteRepository, times(1)).deleteById(1L);
    }
}
