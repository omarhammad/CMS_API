import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
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

      const current_user = await getcurrentUser()

      if (current_user.userRoles.includes("ROLE_PATIENT")) {
        const patient_nn_input = document.getElementById("patient_nn")
        patient_nn_input.value = appointment.patient.nationalNumber
        patient_nn_input.disabled = true
      } else if (current_user.userRoles.includes("ROLE_DOCTOR")) {
        const doctor_input = document.getElementById("doctor")
        doctor_input.value = appointment.doctor.id
        doctor_input.disabled = true
      }
      document.getElementById("appointment_date_time").value =
        appointment.appointmentDateTime
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

async function getcurrentUser() {
  const response = await fetch("http://localhost:8080/api/auth/user/current")

  if (response.status === HttpStatus.UNAUTHORIZED) {
    window.location.href = "/signIn"
  } else if (response.status === HttpStatus.OK) {
    return await response.json()
  }
}
