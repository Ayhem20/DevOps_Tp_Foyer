import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
        reservation.setEtudiants(new HashSet<>());
    }

    @Test
    public void testRetrieveAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.retrieveAllReservations();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveReservation() {
        when(reservationRepository.findById(reservation.getIdReservation())).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation(reservation.getIdReservation());

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo(reservation.getIdReservation());
        verify(reservationRepository, times(1)).findById(reservation.getIdReservation());
    }

    @Test
    public void testAddReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.addReservation(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo(reservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testRemoveReservation() {
        doNothing().when(reservationRepository).deleteById(reservation.getIdReservation());

        reservationService.removeReservation(reservation.getIdReservation());

        verify(reservationRepository, times(1)).deleteById(reservation.getIdReservation());
    }
}
