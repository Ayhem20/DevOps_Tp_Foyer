package tn.esprit.tpfoyer.control;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IEtudiantService;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {

    IReservationService reservationService;
    private final IEtudiantService etudiantService; // Assurez-vous d'avoir un service pour gérer les étudiants


    // http://localhost:8089/tpfoyer/reservation/retrieve-all-reservations
    @GetMapping("/retrieve-all-reservations")
    public List<Reservation> getReservations() {
        List<Reservation> listReservations = reservationService.retrieveAllReservations();
        return listReservations;
    }
    // http://localhost:8089/tpfoyer/reservation/retrieve-reservation/8
    @GetMapping("/retrieve-reservation/{reservation-id}")
    public Reservation retrieveReservation(@PathVariable("reservation-id") String rId) {
        Reservation reservation = reservationService.retrieveReservation(rId);
        return reservation;
    }







    @GetMapping("/retrieve-reservation-date-status/{d}/{v}")
    public List<Reservation> retrieveReservationParDateEtStatus
            (@PathVariable("d") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date d, @PathVariable("v") boolean b) {
        return reservationService.trouverResSelonDateEtStatus(d, b);
    }











    // http://localhost:8089/tpfoyer/reservation/add-reservation
    @PostMapping("/add-reservation")
    public Reservation addReservation(@RequestBody Reservation r) {
        Reservation reservation = reservationService.addReservation(r);
        return reservation;
    }

    // http://localhost:8089/tpfoyer/reservation/remove-reservation/{reservation-id}
    @DeleteMapping("/remove-reservation/{reservation-id}")
    public void removeReservation(@PathVariable("reservation-id") String rId) {
        reservationService.removeReservation(rId);
    }

    // http://localhost:8089/tpfoyer/reservation/modify-reservation
    @PutMapping("/modify-reservation")
    public Reservation modifyReservation(@RequestBody Reservation r) {
        Reservation reservation = reservationService.modifyReservation(r);
        return reservation;
    }
    @GetMapping("/trouver-reservations-par-etudiant/{etudiantId}")
    public List<Reservation> trouverReservationsParEtudiant(@PathVariable("etudiantId") long etudiantId) {
        Etudiant etudiant = etudiantService.retrieveEtudiant(etudiantId);
        return reservationService.trouverReservationsParEtudiant(etudiant);
    }

    @GetMapping("/trouver-reservations-valides/{annee}")
    public List<Reservation> trouverReservationsValides(@PathVariable("annee") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date annee) {
        return reservationService.trouverReservationsValidesParAnnee(annee);
    }

    @GetMapping("/compter-total-reservations")
    public long compterTotalReservations() {
        return reservationService.compterTotalReservations();
    }

    @GetMapping("/compter-reservations-par-annee/{annee}")
    public long compterReservationsParAnnee(@PathVariable("annee") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date annee) {
        return reservationService.compterReservationsParAnnee(annee);
    }

    @GetMapping("/verifier-reservation-etudiant/{etudiantId}/{annee}")
    public boolean verifierReservationEtudiant(@PathVariable("etudiantId") long etudiantId, @PathVariable("annee") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date annee) {
        Etudiant etudiant = etudiantService.retrieveEtudiant(etudiantId);
        return reservationService.etudiantAReservation(etudiant, annee);
    }

}
