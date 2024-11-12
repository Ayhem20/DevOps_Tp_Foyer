package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlocServiceImplTest {

    @InjectMocks
    private BlocServiceImpl blocService;

    @Mock
    private BlocRepository blocRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks
    }

    @Test
    public void testRetrieveAllBlocs() {
        // Arrange
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> blocs = blocService.retrieveAllBlocs();

        // Assert
        assertEquals(2, blocs.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveBlocsSelonCapacite() {
        // Arrange
        Bloc bloc1 = new Bloc();
        bloc1.setCapaciteBloc(100);
        Bloc bloc2 = new Bloc();
        bloc2.setCapaciteBloc(50);
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> blocs = blocService.retrieveBlocsSelonCapacite(60);

        // Assert
        assertEquals(1, blocs.size());
        assertTrue(blocs.contains(bloc1));
        assertFalse(blocs.contains(bloc2));
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // Act
        Bloc result = blocService.retrieveBloc(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        // Act
        Bloc result = blocService.addBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testModifyBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        // Act
        Bloc result = blocService.modifyBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testRemoveBloc() {
        // Arrange
        Long blocId = 1L;
        doNothing().when(blocRepository).deleteById(blocId);

        // Act
        blocService.removeBloc(blocId);

        // Assert
        verify(blocRepository, times(1)).deleteById(blocId);
    }

    @Test
    public void testTrouverBlocsSansFoyer() {
        // Arrange
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> blocs = blocService.trouverBlocsSansFoyer();

        // Assert
        assertEquals(2, blocs.size());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    public void testTrouverBlocsParNomEtCap() {
        // Arrange
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");
        bloc1.setCapaciteBloc(100);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100))
                .thenReturn(Arrays.asList(bloc1));

        // Act
        List<Bloc> blocs = blocService.trouverBlocsParNomEtCap("Bloc A", 100);

        // Assert
        assertEquals(1, blocs.size());
        assertEquals("Bloc A", blocs.get(0).getNomBloc());
        verify(blocRepository, times(1))
                .findAllByNomBlocAndCapaciteBloc("Bloc A", 100);
    }
}