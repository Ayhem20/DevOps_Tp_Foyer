package tn.esprit.tpfoyer.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    ReservationRepository reservationRepository;

    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation retrieveReservation(String reservationId) {
        return reservationRepository.findById(reservationId).get();
    }

    public Reservation addReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    public Reservation modifyReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> trouverResSelonDateEtStatus(Date d, boolean b) {
        return reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(d, b);
    }

    @Override
    public List<Reservation> trouverReservationsParEtudiant(Etudiant etudiant) {
        return reservationRepository.findByEtudiants(etudiant);
    }

    @Override
    public List<Reservation> trouverReservationsValidesParAnnee(Date anneeUniversitaire) {
        return reservationRepository.findByAnneeUniversitaireAndEstValide(anneeUniversitaire, true);
    }

    @Override
    public long compterTotalReservations() {
        return reservationRepository.count();
    }

    @Override
    public long compterReservationsParAnnee(Date anneeUniversitaire) {
        return reservationRepository.findByAnneeUniversitaireAndEstValide(anneeUniversitaire, true).size();
    }

    @Override
    public boolean etudiantAReservation(Etudiant etudiant, Date anneeUniversitaire) {
        List<Reservation> reservations = reservationRepository.findByEtudiants(etudiant);
        return reservations.stream().anyMatch(reservation -> reservation.getAnneeUniversitaire().equals(anneeUniversitaire));    }

    public void removeReservation(String reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
