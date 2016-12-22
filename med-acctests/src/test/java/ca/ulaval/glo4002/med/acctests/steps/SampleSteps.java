package ca.ulaval.glo4002.med.acctests.steps;

import ca.ulaval.glo4002.med.acctests.context.LargeContext;
import ca.ulaval.glo4002.med.acctests.fixtures.patient.PatientMediumFixture;
import ca.ulaval.glo4002.med.acctests.fixtures.createPrescription.CreatePrescriptionFixture;
import ca.ulaval.glo4002.med.acctests.fixtures.createPrescription.CreatePrescriptionRestFixture;
import ca.ulaval.glo4002.med.core.prescriptions.PrescriptionIdentifier;
import cucumber.api.java.Before;
import cucumber.api.java8.Fr;

import static ca.ulaval.glo4002.med.acctests.context.MediumContext.EXISTENT_PATIENT_IDENTIFIER;

public class SampleSteps implements Fr {

    private CreatePrescriptionFixture createPrescriptionFixture;
    private PatientMediumFixture patientFixture;

    @Before
    public void beforeScenario() {
        new LargeContext().apply();
        createPrescriptionFixture = new CreatePrescriptionRestFixture();
        patientFixture = new PatientMediumFixture();
    }

    public SampleSteps() {

        Étantdonné("^une ordonnance valide pour Alice$", () -> {
            createPrescriptionFixture.givenValidPrescription(EXISTENT_PATIENT_IDENTIFIER);
        });

        Quand("^Alice ajoute l'ordonnance à son dossier$", () -> {
            createPrescriptionFixture.whenAddingPrescription(EXISTENT_PATIENT_IDENTIFIER);
        });

        Alors("^l'ordonnance est ajoutée à son dossier$", () -> {
            PrescriptionIdentifier prescriptionIdentifier = createPrescriptionFixture.getCreatedPrescriptionIdentifier();
            patientFixture.thenPatientHasPrescription(EXISTENT_PATIENT_IDENTIFIER, prescriptionIdentifier);
        });

        Alors("^une confirmation lui est signalée$", () -> {
            createPrescriptionFixture.thenPrescriptionCreationIsConfirmed();
        });

        Étantdonné("^une ordonnance invalide pour Alice$", () -> {
            createPrescriptionFixture.givenInvalidPrescription(EXISTENT_PATIENT_IDENTIFIER);
        });

        Alors("^l'ordonnance n'est pas ajoutée à son dossier$", () -> {
            PrescriptionIdentifier prescriptionIdentifier = createPrescriptionFixture.getCreatedPrescriptionIdentifier();
            patientFixture.thenPatientDoesntHaveAnyPrescription(EXISTENT_PATIENT_IDENTIFIER, prescriptionIdentifier);
        });

        Alors("^une erreur lui est signalée$", () -> {
            createPrescriptionFixture.thenPrescriptionCreationHasAnError();
        });
    }
}
