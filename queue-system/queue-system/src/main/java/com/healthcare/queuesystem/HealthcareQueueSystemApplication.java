package com.healthcare.queuesystem;

import com.healthcare.queuesystem.model.Doctor;
import com.healthcare.queuesystem.model.Patient;
import com.healthcare.queuesystem.model.Appointment;
import com.healthcare.queuesystem.model.Queue;
import com.healthcare.queuesystem.model.enums.*;
import com.healthcare.queuesystem.service.DoctorService;
import com.healthcare.queuesystem.service.PatientService;
import com.healthcare.queuesystem.service.AppointmentService;
import com.healthcare.queuesystem.service.QueueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class HealthcareQueueSystemApplication implements CommandLineRunner {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private QueueService queueService;

	public static void main(String[] args) {
		SpringApplication.run(HealthcareQueueSystemApplication.class, args);
		System.out.println("🏥 Healthcare Queue Management System Started Successfully!");
		System.out.println("📊 Dashboard: http://localhost:8080");
		System.out.println("📚 API Documentation: http://localhost:8080/swagger-ui.html");
		System.out.println("🔧 Health Check: http://localhost:8080/actuator/health");
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("🚀 Initializing Healthcare Queue Management System...");

		// Initialize sample data if database is empty
		initializeSampleData();

		System.out.println("✅ System initialization completed!");
		System.out.println("📈 Sample data loaded successfully");
		printSystemStats();
	}

	/**
	 * Configure CORS for frontend integration
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(false);
			}
		};
	}

	/**
	 * Initialize sample data for testing and demonstration
	 */
	private void initializeSampleData() {
		try {
			// Check if data already exists
			if (!doctorService.getAllDoctors().isEmpty()) {
				System.out.println("📋 Sample data already exists, skipping initialization");
				return;
			}

			System.out.println("💾 Creating sample data...");

			// Create sample doctors
			List<Doctor> sampleDoctors = createSampleDoctors();
			for (Doctor doctor : sampleDoctors) {
				doctorService.saveDoctor(doctor);
			}

			// Create sample patients
			List<Patient> samplePatients = createSamplePatients();
			for (Patient patient : samplePatients) {
				patientService.savePatient(patient);
			}

			// Create sample appointments
			List<Appointment> sampleAppointments = createSampleAppointments(sampleDoctors, samplePatients);
			for (Appointment appointment : sampleAppointments) {
				appointmentService.saveAppointment(appointment);
			}

			// Create sample queue entries
			List<Queue> sampleQueues = createSampleQueues(sampleDoctors, samplePatients);
			for (Queue queue : sampleQueues) {
				queueService.saveQueue(queue);
			}

			System.out.println("✨ Sample data created successfully!");

		} catch (Exception e) {
			System.err.println("❌ Error initializing sample data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Create sample doctors with different specializations
	 */
	private List<Doctor> createSampleDoctors() {
		List<Doctor> doctors = new ArrayList<>();

		// Doctor 1 - Cardiologist
		Doctor doctor1 = new Doctor();
		doctor1.setDoctorId("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		doctor1.setName("Dr. Rajesh Sharma");
		doctor1.setSpecialization("Cardiology");
		doctor1.setPhone("+91-9876543210");
		doctor1.setEmail("rajesh.sharma@healthcare.com");
		doctor1.setDepartment("Cardiology");
		doctor1.setExperienceYears(15);
		doctor1.setAvailability("Mon-Fri 9AM-5PM");
		doctor1.setGender(Gender.MALE);
		doctor1.setConsultationOption(ConsultationOption.HYBRID);
		doctor1.setAppointmentIds(new ArrayList<>());
		doctor1.setQueueEntryIds(new ArrayList<>());

		// Doctor 2 - Pediatrician
		Doctor doctor2 = new Doctor();
		doctor2.setDoctorId("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		doctor2.setName("Dr. Priya Patel");
		doctor2.setSpecialization("Pediatrics");
		doctor2.setPhone("+91-9876543211");
		doctor2.setEmail("priya.patel@healthcare.com");
		doctor2.setDepartment("Pediatrics");
		doctor2.setExperienceYears(12);
		doctor2.setAvailability("Mon-Sat 8AM-6PM");
		doctor2.setGender(Gender.FEMALE);
		doctor2.setConsultationOption(ConsultationOption.OFFLINE);
		doctor2.setAppointmentIds(new ArrayList<>());
		doctor2.setQueueEntryIds(new ArrayList<>());

		// Doctor 3 - General Medicine
		Doctor doctor3 = new Doctor();
		doctor3.setDoctorId("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		doctor3.setName("Dr. Amit Kumar");
		doctor3.setSpecialization("General Medicine");
		doctor3.setPhone("+91-9876543212");
		doctor3.setEmail("amit.kumar@healthcare.com");
		doctor3.setDepartment("General Medicine");
		doctor3.setExperienceYears(8);
		doctor3.setAvailability("Mon-Fri 10AM-4PM");
		doctor3.setGender(Gender.MALE);
		doctor3.setConsultationOption(ConsultationOption.ONLINE);
		doctor3.setAppointmentIds(new ArrayList<>());
		doctor3.setQueueEntryIds(new ArrayList<>());

		// Doctor 4 - Orthopedics
		Doctor doctor4 = new Doctor();
		doctor4.setDoctorId("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		doctor4.setName("Dr. Sunita Gupta");
		doctor4.setSpecialization("Orthopedics");
		doctor4.setPhone("+91-9876543213");
		doctor4.setEmail("sunita.gupta@healthcare.com");
		doctor4.setDepartment("Orthopedics");
		doctor4.setExperienceYears(18);
		doctor4.setAvailability("Tue-Sat 9AM-3PM");
		doctor4.setGender(Gender.FEMALE);
		doctor4.setConsultationOption(ConsultationOption.HYBRID);
		doctor4.setAppointmentIds(new ArrayList<>());
		doctor4.setQueueEntryIds(new ArrayList<>());

		doctors.addAll(Arrays.asList(doctor1, doctor2, doctor3, doctor4));
		return doctors;
	}

	/**
	 * Create sample patients with diverse profiles
	 */
	private List<Patient> createSamplePatients() {
		List<Patient> patients = new ArrayList<>();

		// Patient 1
		Patient patient1 = new Patient();
		patient1.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		patient1.setName("Arjun Singh");
		patient1.setAge(35);
		patient1.setGender(Gender.MALE);
		patient1.setDateOfBirth(LocalDate.of(1989, 5, 15));
		patient1.setAddress("123 MG Road, Chandigarh");
		patient1.setPhone("+91-9123456789");
		patient1.setEmail("arjun.singh@email.com");
		patient1.setBloodGroup(BloodGroup.O_POSITIVE);
		patient1.setMedicalHistory("No major medical history");
		patient1.setAppointmentIds(new ArrayList<>());
		patient1.setQueueEntryIds(new ArrayList<>());

		// Patient 2
		Patient patient2 = new Patient();
		patient2.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		patient2.setName("Meera Sharma");
		patient2.setAge(28);
		patient2.setGender(Gender.FEMALE);
		patient2.setDateOfBirth(LocalDate.of(1996, 8, 22));
		patient2.setAddress("456 Sector 17, Chandigarh");
		patient2.setPhone("+91-9123456790");
		patient2.setEmail("meera.sharma@email.com");
		patient2.setBloodGroup(BloodGroup.A_POSITIVE);
		patient2.setMedicalHistory("Allergic to penicillin");
		patient2.setAppointmentIds(new ArrayList<>());
		patient2.setQueueEntryIds(new ArrayList<>());

		// Patient 3
		Patient patient3 = new Patient();
		patient3.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		patient3.setName("Rohit Verma");
		patient3.setAge(42);
		patient3.setGender(Gender.MALE);
		patient3.setDateOfBirth(LocalDate.of(1982, 12, 10));
		patient3.setAddress("789 Sector 22, Chandigarh");
		patient3.setPhone("+91-9123456791");
		patient3.setEmail("rohit.verma@email.com");
		patient3.setBloodGroup(BloodGroup.B_NEGATIVE);
		patient3.setMedicalHistory("Diabetes Type 2, Hypertension");
		patient3.setAppointmentIds(new ArrayList<>());
		patient3.setQueueEntryIds(new ArrayList<>());

		// Patient 4
		Patient patient4 = new Patient();
		patient4.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		patient4.setName("Kavya Reddy");
		patient4.setAge(31);
		patient4.setGender(Gender.FEMALE);
		patient4.setDateOfBirth(LocalDate.of(1993, 3, 18));
		patient4.setAddress("321 Sector 35, Chandigarh");
		patient4.setPhone("+91-9123456792");
		patient4.setEmail("kavya.reddy@email.com");
		patient4.setBloodGroup(BloodGroup.AB_POSITIVE);
		patient4.setMedicalHistory("Asthma");
		patient4.setAppointmentIds(new ArrayList<>());
		patient4.setQueueEntryIds(new ArrayList<>());

		// Patient 5 - Child
		Patient patient5 = new Patient();
		patient5.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		patient5.setName("Aarav Gupta");
		patient5.setAge(8);
		patient5.setGender(Gender.MALE);
		patient5.setDateOfBirth(LocalDate.of(2016, 7, 25));
		patient5.setAddress("654 Sector 11, Chandigarh");
		patient5.setPhone("+91-9123456793");
		patient5.setEmail("parent.gupta@email.com");
		patient5.setBloodGroup(BloodGroup.O_NEGATIVE);
		patient5.setMedicalHistory("Regular checkups");
		patient5.setAppointmentIds(new ArrayList<>());
		patient5.setQueueEntryIds(new ArrayList<>());

		patients.addAll(Arrays.asList(patient1, patient2, patient3, patient4, patient5));
		return patients;
	}

	/**
	 * Create sample appointments linking doctors and patients
	 */
	private List<Appointment> createSampleAppointments(List<Doctor> doctors, List<Patient> patients) {
		List<Appointment> appointments = new ArrayList<>();

		if (doctors.size() >= 4 && patients.size() >= 5) {
			// Appointment 1 - Cardiology
			Appointment apt1 = new Appointment();
			apt1.setAppointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			apt1.setDoctorId(doctors.get(0).getDoctorId()); // Dr. Rajesh Sharma
			apt1.setPatientId(patients.get(2).getPatientId()); // Rohit Verma (diabetes patient)
			apt1.setAppointmentDate(LocalDateTime.now().plusHours(2));
			apt1.setPurpose("Cardiac consultation for diabetes complications");
			apt1.setStatus(AppointmentStatus.SCHEDULED);

			// Appointment 2 - Pediatrics
			Appointment apt2 = new Appointment();
			apt2.setAppointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			apt2.setDoctorId(doctors.get(1).getDoctorId()); // Dr. Priya Patel
			apt2.setPatientId(patients.get(4).getPatientId()); // Aarav Gupta (child)
			apt2.setAppointmentDate(LocalDateTime.now().plusHours(1));
			apt2.setPurpose("Regular health checkup");
			apt2.setStatus(AppointmentStatus.SCHEDULED);

			// Appointment 3 - General Medicine
			Appointment apt3 = new Appointment();
			apt3.setAppointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			apt3.setDoctorId(doctors.get(2).getDoctorId()); // Dr. Amit Kumar
			apt3.setPatientId(patients.get(0).getPatientId()); // Arjun Singh
			apt3.setAppointmentDate(LocalDateTime.now().plusHours(3));
			apt3.setPurpose("General health consultation");
			apt3.setStatus(AppointmentStatus.PENDING);

			// Appointment 4 - Orthopedics
			Appointment apt4 = new Appointment();
			apt4.setAppointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			apt4.setDoctorId(doctors.get(3).getDoctorId()); // Dr. Sunita Gupta
			apt4.setPatientId(patients.get(1).getPatientId()); // Meera Sharma
			apt4.setAppointmentDate(LocalDateTime.now().plusHours(4));
			apt4.setPurpose("Knee pain consultation");
			apt4.setStatus(AppointmentStatus.SCHEDULED);

			// Appointment 5 - Follow-up
			Appointment apt5 = new Appointment();
			apt5.setAppointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			apt5.setDoctorId(doctors.get(1).getDoctorId()); // Dr. Priya Patel
			apt5.setPatientId(patients.get(3).getPatientId()); // Kavya Reddy
			apt5.setAppointmentDate(LocalDateTime.now().plusDays(1));
			apt5.setPurpose("Asthma follow-up consultation");
			apt5.setStatus(AppointmentStatus.SCHEDULED);

			appointments.addAll(Arrays.asList(apt1, apt2, apt3, apt4, apt5));
		}

		return appointments;
	}

	/**
	 * Create sample queue entries for active consultations
	 */
	private List<Queue> createSampleQueues(List<Doctor> doctors, List<Patient> patients) {
		List<Queue> queues = new ArrayList<>();

		if (doctors.size() >= 2 && patients.size() >= 3) {
			// Queue entry 1 - Currently waiting
			Queue queue1 = new Queue();
			queue1.setQueueId("QUE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			queue1.setDoctorId(doctors.get(0).getDoctorId()); // Dr. Rajesh Sharma
			queue1.setPatientId(patients.get(2).getPatientId()); // Rohit Verma
			queue1.setPosition(1);
			queue1.setStatus(QueueStatus.WAITING);
			queue1.setCreatedAt(LocalDateTime.now().minusMinutes(15));

			// Queue entry 2 - In progress
			Queue queue2 = new Queue();
			queue2.setQueueId("QUE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			queue2.setDoctorId(doctors.get(1).getDoctorId()); // Dr. Priya Patel
			queue2.setPatientId(patients.get(4).getPatientId()); // Aarav Gupta
			queue2.setPosition(1);
			queue2.setStatus(QueueStatus.IN_PROGRESS);
			queue2.setCreatedAt(LocalDateTime.now().minusMinutes(30));

			// Queue entry 3 - Waiting (second in line)
			Queue queue3 = new Queue();
			queue3.setQueueId("QUE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			queue3.setDoctorId(doctors.get(1).getDoctorId()); // Dr. Priya Patel
			queue3.setPatientId(patients.get(1).getPatientId()); // Meera Sharma
			queue3.setPosition(2);
			queue3.setStatus(QueueStatus.WAITING);
			queue3.setCreatedAt(LocalDateTime.now().minusMinutes(10));

			queues.addAll(Arrays.asList(queue1, queue2, queue3));
		}

		return queues;
	}

	/**
	 * Print system statistics after initialization
	 */
	private void printSystemStats() {
		try {
			int totalDoctors = doctorService.getAllDoctors().size();
			int totalPatients = patientService.getAllPatients().size();
			int totalAppointments = appointmentService.getAllAppointments().size();
			int totalQueues = queueService.getAllQueues().size();

			System.out.println("\n📊 SYSTEM STATISTICS");
			System.out.println("====================");
			System.out.println("👨‍⚕️ Total Doctors: " + totalDoctors);
			System.out.println("👥 Total Patients: " + totalPatients);
			System.out.println("📅 Total Appointments: " + totalAppointments);
			System.out.println("⏳ Active Queue Entries: " + totalQueues);
			System.out.println("====================");

			// Print sample specializations
			System.out.println("\n🏥 AVAILABLE SPECIALIZATIONS:");
			doctorService.getAllDoctors().forEach(doctor ->
					System.out.println("• " + doctor.getSpecialization() + " - " + doctor.getName())
			);

			System.out.println("\n🚀 System is ready for use!");
			System.out.println("📱 You can now integrate with frontend applications");
			System.out.println("🔗 Use the provided API endpoints to manage:");
			System.out.println("   - Patient Registration & Management");
			System.out.println("   - Doctor Profiles & Availability");
			System.out.println("   - Appointment Scheduling");
			System.out.println("   - Real-time Queue Management");

		} catch (Exception e) {
			System.err.println("❌ Error printing system statistics: " + e.getMessage());
		}
	}

	/**
	 * Application startup banner
	 */
	static {
		System.out.println("\n" +
				"████████████████████████████████████████████████████████████████\n" +
				"█                                                              █\n" +
				"█    🏥 HEALTHCARE QUEUE MANAGEMENT SYSTEM                     █\n" +
				"█                                                              █\n" +
				"█    ✨ Features:                                              █\n" +
				"█       • Patient Registration & Management                    █\n" +
				"█       • Doctor Profile & Availability Management            █\n" +
				"█       • Smart Appointment Scheduling                        █\n" +
				"█       • Real-time Queue Management                          █\n" +
				"█       • Priority-based Queue System                         █\n" +
				"█       • MongoDB Integration                                  █\n" +
				"█       • REST API with Swagger Documentation                 █\n" +
				"█                                                              █\n" +
				"█    🚀 Starting Application...                                █\n" +
				"█                                                              █\n" +
				"████████████████████████████████████████████████████████████████\n"
		);
	}
}