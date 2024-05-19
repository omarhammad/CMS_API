import Quill from "quill"
import "quill/dist/quill.snow.css"
import { HttpStatus } from "../util/httpStatus.js"

import { showToast } from "../util/toast.js"

const notesEditor = new Quill("#notes", {
  theme: "snow",
})
const submitBtn = document.getElementById("submitBtn")
const medication_id = window.location.pathname.split("/").pop()

submitBtn.addEventListener("click", updateMedication)
window.addEventListener("DOMContentLoaded", loadMedication)

async function updateMedication(event) {
  event.preventDefault()
  const medicationJson = getFormData()
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content")
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content")
  const headers = new Headers({
    Accept: "application/json",
    "Content-Type": "application/json",
    [csrfHeader]: csrfToken,
  })

  try {
    const response = await fetch(
      `http://localhost:8080/api/medications/${medication_id}`,
      {
        method: "PUT",
        headers: headers,
        body: medicationJson,
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
      window.location.href = "/medications?updated=true"
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadMedication() {
  try {
    const response = await fetch(
      `http://localhost:8080/api/medications/${medication_id}`,
    )

    if (response.status === HttpStatus.NOT_FOUND) {
      console.log("NOT FOUND")
    } else if (response.ok) {
      const medication = await response.json()
      fillInForm(medication)
    }
  } catch (error) {
    console.error(error)
  }
}

function fillInForm(medication) {
  document.getElementById("name").value = medication.name
  document.getElementById("med_form").value = medication.form
  document.getElementById("quantity").value = medication.dosage.quantity
  document.getElementById("unit").value = medication.dosage.unit
  document.getElementById("freq").value = medication.frequencies
  document.getElementById("dd").value = medication.daysDuration
  document.getElementById("notes").value = medication.notes
}

function handleFieldsError(fieldsErrors) {
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "name")) {
    document
      .getElementById("name")
      .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.name))
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "medicationForm")) {
    document
      .getElementById("med_form")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.medicationForm),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "quantity")) {
    document
      .getElementById("quantity")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.quantity),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "unit")) {
    document
      .getElementById("unit")
      .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.unit))
  }
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "frequencies")) {
    document
      .getElementById("freq")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.frequencies),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "daysDuration")) {
    document
      .getElementById("dd")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.daysDuration),
      )
  }
}

function getFieldsErrorElementList(errors) {
  const ulElement = document.createElement("ul")
  ulElement.className = "custom-bullet"
  errors = errors.split(";")
  for (const error of errors) {
    const ilElement = document.createElement("li")
    ilElement.className = "form-error"
    ilElement.innerText = "* " + error
    ulElement.appendChild(ilElement)
    ulElement.appendChild(document.createElement("br"))
  }

  return ulElement
}

function getFormData() {
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {
    medicationId: medication_id,
  }
  for (const [key, value] of formData.entries()) {
    formJson[key] = value
  }
  formJson["notes"] = notesEditor.getSemanticHTML()
  return JSON.stringify(formJson)
}
