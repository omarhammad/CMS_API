import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
import { getCurrentUser } from "../util/currentUser.js"
import { formatDate, formatTime } from "../util/date_time_formatter.js"

window.addEventListener("DOMContentLoaded", loadAppointmentData)

async function loadAppointmentData() {
  const pathname = window.location.pathname
  const segments = pathname.split("/")
  const appointment_id = segments.pop()

  try {
    const response = await fetch(
      `http://localhost:8080/api/appointments/${appointment_id}`,
    )
    if (response.status === HttpStatus.NOT_FOUND) {
      console.log("NOT FOUND !")
    } else if (response.ok) {
      const appointment = await response.json()

      const current_user = await getCurrentUser()

      const patient_nn_input = document.getElementById("patient_nn")
      const doctor_selection = document.getElementById("doctor")
      const appointmentSlots = document.getElementById("appointment_slots")

      if (current_user.userRoles.includes("ROLE_PATIENT")) {
        patient_nn_input.value = appointment.patient.nationalNumber
        patient_nn_input.disabled = true
        doctor_selection.value = appointment.doctor.id
      } else if (current_user.userRoles.includes("ROLE_DOCTOR")) {
        doctor_selection.value = appointment.doctor.id
        doctor_selection.disabled = true
        patient_nn_input.value = appointment.patient.nationalNumber
      } else if (
        current_user.userRoles.includes("ROLE_ADMIN", "ROLE_SECRETARY")
      ) {
        patient_nn_input.value = appointment.patient.nationalNumber
        doctor_selection.value = appointment.doctor.id
      }

      // SET DOCTORS AVAILABILITIES ON CHANGE....

      await setAvailabilities(appointmentSlots, doctor_selection.value)
      appointmentSlots.appendChild(
        get_slot_option(appointment.availabilitySlot),
      )
      appointmentSlots.value = appointment.availabilitySlot.id

      doctor_selection.addEventListener("change", async (event) => {
        const doctorId = Number(event.target.value)
        await setAvailabilities(appointmentSlots, doctorId)
        if (doctorId === appointment.doctor.id) {
          console.log("Adding current Appointmetn slot")
          appointmentSlots.appendChild(
            get_slot_option(appointment.availabilitySlot),
          )
          appointmentSlots.value = appointment.availabilitySlot.id
        }
      })

      document.getElementById("appointment_slots").value =
        appointment.availabilitySlot.id

      document.getElementById("appointment_type").value =
        appointment.appointmentType
      document.getElementById("purpose").value = appointment.purpose
    }
  } catch (error) {
    console.error(error)
  }
}

const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", put_appointments)
const pathname = window.location.pathname
const segments = pathname.split("/")
const appointment_id = segments.pop()

async function put_appointments() {
  const csrf_token = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content")
  const csrf_header = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content")

  const headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
    [csrf_header]: csrf_token,
  }

  const appointmentData = getFormData()
  try {
    const response = await fetch(
      `http://localhost:8080/api/appointments/${appointment_id}`,
      {
        method: "PUT",
        headers: headers,
        body: appointmentData,
      },
    )
    if (response.status === HttpStatus.BAD_REQUEST) {
      const data = await response.json()
      if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
        showToast(data.exceptionMsg)
      } else {
        handleFieldsError(data)
      }
    } else if (response.status === HttpStatus.NO_CONTENT) {
      window.location.href = "/appointments?updated=true"
    } else {
      console.error("MyError:" + response.status)
    }
  } catch (error) {
    console.error(error)
  }
}

function getFormData() {
  document.getElementById("doctor").disabled = false
  document.getElementById("patient_nn").disabled = false
  const currentSlotSelected = document.getElementById("appointment_slots").value

  if (currentSlotSelected !== 0) {
    document.getElementById("appointment_slots").disabled = false
  }
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {
    appointmentId: appointment_id,
  }
  for (const [key, value] of formData.entries()) {
    formJson[key] = value
  }
  return JSON.stringify(formJson)
}

function handleFieldsError(fieldsErrors) {
  const ulElements = document.querySelectorAll("ul")
  ulElements.forEach((ul) => {
    ul.innerHTML = null
  })

  if (
    Object.prototype.hasOwnProperty.call(fieldsErrors, "appointmentDateTime")
  ) {
    document
      .getElementById("appointment_date_time")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.appointmentDateTime),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "patientNN")) {
    document
      .getElementById("patient_nn")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.patientNN),
      )
  }
}

function getFieldsErrorElementList(errors) {
  const ulElement = document.createElement("ul")
  ulElement.className = "custom-bullet"
  errors = errors.split(";")
  for (const error of errors) {
    const ilElement = document.createElement("il")
    ilElement.className = "form-error"
    ilElement.innerText = "* " + error
    ulElement.appendChild(ilElement)

    ulElement.appendChild(document.createElement("br"))
  }

  return ulElement
}

async function setAvailabilities(appointmentSlots, doctorId) {
  const availabilities_response = await fetch(
    `/api/doctors/${doctorId}/availability`,
  )
  if (availabilities_response.status === HttpStatus.NOT_FOUND) {
    showToast("Doctor Not Found!")
  } else if (availabilities_response.status === HttpStatus.NO_CONTENT) {
    appointmentSlots.innerHTML = null
    const no_slots = document.createElement("option")
    no_slots.innerText = "NO AVAILABLE SLOTS!"
    no_slots.value = 0
    appointmentSlots.appendChild(no_slots)
    appointmentSlots.disabled = true
  } else if (availabilities_response.status === HttpStatus.OK) {
    appointmentSlots.innerHTML = null
    const slots = await availabilities_response.json()
    for (const slot of slots) {
      appointmentSlots.appendChild(get_slot_option(slot))
    }
    appointmentSlots.disabled = false
  }
}

function get_slot_option(slot) {
  const slot_option = document.createElement("option")
  const slot_date_time = new Date(slot.slot)
  slot_option.innerText =
    formatDate(slot_date_time) + " - " + formatTime(slot_date_time)
  slot_option.value = slot.id
  return slot_option
}
