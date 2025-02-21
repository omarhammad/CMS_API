import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
import { getCurrentUser } from "../util/currentUser.js"
window.addEventListener("DOMContentLoaded", loadPatientData)
const patient_id = window.location.pathname.split("/").pop()

async function loadPatientData() {
  try {
    const response = await fetch(
      `http://localhost:8080/api/patients/${patient_id}`,
    )

    if (response.status === HttpStatus.NOT_FOUND) {
      console.log("NOT FOUND")
      window.location.href = "/patients?not_found=true"
    } else if (response.status === HttpStatus.OK) {
      const patient = await response.json()
      fillForm(patient)
    } else {
      console.log("Error")
      throw new Error(response.statusText)
    }
  } catch (error) {
    console.error(error)
  }
}

function fillForm(patient) {
  console.log(patient)
  document.getElementById("first_name").value = patient["firstName"]
  document.getElementById("last_name").value = patient["lastName"]
  document.getElementById("gender").value = patient["gender"]
  const contactInfo = patient["contactInfo"].split(",")
  document.getElementById("national_num").value = patient["nationalNumber"]
  document.getElementById("phone_number").value = contactInfo[0]
  document.getElementById("email").value = contactInfo[1]
}

const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", updatePatient)

async function updatePatient() {
  const current_user = await getCurrentUser()
  const patientJson = getFormData()
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
  const response = await fetch(
    `http://localhost:8080/api/patients/${patient_id}`,
    {
      method: "PUT",
      headers: headers,
      body: patientJson,
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
    if (current_user.userRoles.includes("ROLE_PATIENT")) {
      window.location.href = "/patients/details?updated=true"
    } else {
      window.location.href = "/patients?updated=true"
    }
  }
}

function getFormData() {
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {
    patientId: patient_id,
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

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "firstName")) {
    document
      .getElementById("first_name")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.firstName),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "lastName")) {
    document
      .getElementById("last_name")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.lastName),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "nationalNumber")) {
    document
      .getElementById("national_num")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.nationalNumber),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "phoneNumber")) {
    document
      .getElementById("phone_number")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.phoneNumber),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "email")) {
    document
      .getElementById("email")
      .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.email))
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
