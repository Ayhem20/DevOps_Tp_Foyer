package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class test_chambre {
    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllChambres() {
        // Arrange
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        // Act
        List<Chambre> chambres = chambreService.retrieveAllChambres();

        // Assert
        assertEquals(2, chambres.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveChambre() {
        // Arrange
        Long chambreId = 1L;
        Chambre chambre = new Chambre();
        chambre.setIdChambre(chambreId);
        when(chambreRepository.findById(chambreId)).thenReturn(Optional.of(chambre));

        // Act
        Chambre result = chambreService.retrieveChambre(chambreId);

        // Assert
        assertNotNull(result);
        assertEquals(chambreId, result.getIdChambre());
        verify(chambreRepository, times(1)).findById(chambreId);
    }

    @Test
    void testAddChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.addChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testRemoveChambre() {
        // Arrange
        Long chambreId = 1L;

        // Act
        chambreService.removeChambre(chambreId);

        // Assert
        verify(chambreRepository, times(1)).deleteById(chambreId);
    }

    @Test
    void testModifyChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.modifyChambre(chambre);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testTrouverchambreSelonEtudiant() {
        // Arrange
        long cin = 123456789L;
        Chambre chambre = new Chambre();
        when(chambreRepository.trouverChselonEt(cin)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.trouverchambreSelonEtudiant(cin);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).trouverChselonEt(cin);
    }

    @Test
    void testRecupererChambresSelonTyp() {
        // Arrange
        TypeChambre type = TypeChambre.SIMPLE;
        Chambre chambre1 = new Chambre();
        chambre1.setTypeC(type);
        Chambre chambre2 = new Chambre();
        chambre2.setTypeC(type);
        when(chambreRepository.findAllByTypeC(type)).thenReturn(Arrays.asList(chambre1, chambre2));

        // Act
        List<Chambre> chambres = chambreService.recupererChambresSelonTyp(type);

        // Assert
        assertEquals(2, chambres.size());
        verify(chambreRepository, times(1)).findAllByTypeC(type);
    }
}






