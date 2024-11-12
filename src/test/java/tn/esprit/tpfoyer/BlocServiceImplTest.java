package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class BlocServiceImplTest {
    @Autowired
    BlocServiceImpl blocService;

    @Autowired
    BlocRepository blocRepository;
    @BeforeEach
    void setUp() {
        // Clear the database before each test
        blocRepository.deleteAll();
    }
    @Test
    @Order(1)
    @Transactional
    @Rollback
    void testAddBloc() {
        Bloc bloc = new Bloc(0L, "Bloc B", 200L, null, null);
        Bloc savedBloc = blocService.addBloc(bloc);
        assertNotNull(savedBloc);
        assertTrue(savedBloc.getIdBloc() > 0);
    }

    @Test
    @Order(2)
    void testRetrieveAllBlocs() {
        List<Bloc> blocs = blocService.retrieveAllBlocs();
        assertNotNull(blocs);
        assertTrue(blocs.isEmpty() || blocs.size() > 1);
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback
    void testRetrieveBlocById() {
        Bloc bloc = new Bloc(0L, "Bloc C", 150L, null, null);
        Bloc savedBloc = blocService.addBloc(bloc);

        Bloc retrievedBloc = blocService.retrieveBloc(savedBloc.getIdBloc());
        assertNotNull(retrievedBloc);
        assertEquals("Bloc C", retrievedBloc.getNomBloc());
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback
    void testModifyBloc() {
        Bloc bloc = new Bloc(0L, "Bloc D", 300L, null, null);
        Bloc savedBloc = blocService.addBloc(bloc);
        savedBloc.setCapaciteBloc(400L);

        Bloc updatedBloc = blocService.modifyBloc(savedBloc);
        assertEquals(400L, updatedBloc.getCapaciteBloc());
    }

    @Test
    @Order(5)
    @Transactional
    @Rollback
    void testRemoveBloc() {
        Bloc bloc = new Bloc(0L, "Bloc E", 250L, null, null);
        Bloc savedBloc = blocService.addBloc(bloc);

        blocService.removeBloc(savedBloc.getIdBloc());
        assertFalse(blocRepository.existsById(savedBloc.getIdBloc()));
    }
}
