import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", postNewAppointment)

async function postNewAppointment() {
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
  console.log(appointmentData)
  try {
    const response = await fetch("http://localhost:8080/api/appointments/", {
      method: "POST",
      headers: headers,
      body: appointmentData,
    })
    console.log("Entered 1")
    const data = await response.json()
    if (response.status === HttpStatus.BAD_REQUEST) {
      console.log("Entered 2")
      if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
        showToast(data.exceptionMsg)
      } else {
        handleFieldsError(data)
      }
    } else if (response.status === HttpStatus.CREATED) {
      console.log("Entered 3")
      console.log(data)
      window.location.href = "/appointments?created=true"
    } else {
      console.error("MyError:" + response.status)
    }
  } catch (error) {
    console.error(error.message)
  }
}

function getFormData() {
  document.getElementById("doctor").disabled = false
  document.getElementById("patient_nn").disabled = false
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {}
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

window.addEventListener("DOMContentLoaded", prepareAddPage)

async function prepareAddPage() {
  const current_user = await getcurrentUser()

  if (current_user.userRoles.includes("ROLE_PATIENT")) {
    fetch(`http://localhost:8080/api/patients/${current_user.userId}`)
      .then((response) => {
        if (response.ok) {
          return response.json()
        }
      })
      .then((patient) => {
        const patient_nn_input = document.getElementById("patient_nn")
        patient_nn_input.value = patient.nationalNumber
        patient_nn_input.disabled = true
      })
  } else if (current_user.userRoles.includes("ROLE_DOCTOR")) {
    const doctor_input = document.getElementById("doctor")
    doctor_input.value = current_user.userId
    doctor_input.disabled = true
  }
}

async function getcurrentUser() {
  const response = await fetch("http://localhost:8080/api/auth/user/current")

  if (response.status === HttpStatus.UNAUTHORIZED) {
    window.location.href = "/signIn"
  } else if (response.status === HttpStatus.OK) {
    return await response.json()
  }
}
