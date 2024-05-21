import { HttpStatus } from "../util/httpStatus.js"

window.addEventListener("DOMContentLoaded", loadPatient)
const patient_id = window.location.pathname.split("/").pop()

async function loadPatient() {
  try {
    const response = await fetch(
      `http://localhost:8080/api/patients/${patient_id}`,
    )

    if (response.status === HttpStatus.NOT_FOUND) {
    } else if (response.status === HttpStatus.OK) {
      const patient = await response.json()
      fillPage(patient)
    }
  } catch (error) {
    console.error(error)
  }
}

function fillPage(patient) {
  document.getElementById("patient_name").innerText =
    patient["firstName"] + " " + patient["lastName"]
  document.getElementById("gender").innerText = patient["gender"]
  document.getElementById("age").innerText = patient["age"]
  document.getElementById("patient_nn").innerText = patient["nationalNumber"]
  const contactInfo = patient["contactInfo"].split(",")
  document.getElementById("email").innerText = contactInfo[1]
  document.getElementById("phone").innerText = contactInfo[0]

  const his_doctors_body = document.getElementById("hisDoctors")
  const his_doctors = patient["hisDoctors"]
  if (his_doctors.length === 0) {
    const no_doctors = document.createElement("p")
    no_doctors.className = "h4 m-3"
    no_doctors.innerText = "There is No appointments"
    his_doctors_body.appendChild(no_doctors)
  } else {
    for (const doctor of his_doctors) {
      const doctor_body = document.createElement("p")
      doctor_body.className = "d-block details-text"
      doctor_body.innerText = doctor.firstName + " " + doctor.lastName
      his_doctors_body.appendChild(doctor_body)
    }
  }

  const all_medical_records_body = document.getElementById("medical_records")
  const old_appointments = patient["oldAppointments"]

  if (old_appointments.length === 0) {
    const no_records = document.createElement("div")
    no_records.className = "card-text text-center h5"
    no_records.className = "NO MEDICAL RECORDS"
    all_medical_records_body.appendChild(no_records)
  } else {
    let counter = 0
    for (const appointment of old_appointments) {
      const medical_record = document.createElement("div")
      medical_record.className = "card-text m-2 mb-4 h5"
      const appointment_date = formatDate(
        new Date(appointment["availabilitySlot"].slot),
      )
      const appointment_doctor =
        appointment.doctor["firstName"] + " " + appointment.doctor["lastName"]
      const appointment_purpose = appointment["purpose"]
      const appointment_prescriptions = appointment["prescription"]
      let medicationNames
      if (appointment_prescriptions === null) {
        medicationNames = "nothing."
      } else {
        const medications = appointment_prescriptions["medications"]
        medicationNames = medications.reduce(
          (acc, medication, index, array) => {
            // Add the medication name to the accumulator
            const separator = index === array.length - 1 ? "" : ", "
            return acc + medication.name + separator
          },
          "",
        )
        medicationNames = `[ ${medicationNames} ]`
      }

      medical_record.innerHTML = `<span>${++counter}. On ${appointment_date} saw Dr. ${appointment_doctor} for ${appointment_purpose} and prescribed him: ${medicationNames}  </span>`
      all_medical_records_body.appendChild(medical_record)
    }
  }
}

function formatDate(date) {
  const day = date.getDate().toString().padStart(2, "0")
  const month = (date.getMonth() + 1).toString().padStart(2, "0") // getMonth() is zero-indexed
  const year = date.getFullYear()

  return `${day}/${month}/${year}`
}
