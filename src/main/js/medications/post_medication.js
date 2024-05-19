import Quill from "quill"
import "quill/dist/quill.snow.css"
import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
const notesEditor = new Quill("#notes", {
  theme: "snow",
})

const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", addNewMedication)

async function addNewMedication(event) {
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
    const response = await fetch("http://localhost:8080/api/medications/", {
      method: "POST",
      headers: headers,
      body: medicationJson,
    })
    if (response.status === HttpStatus.BAD_REQUEST) {
      const data = await response.json()
      if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
        showToast(data.exceptionMsg)
      } else {
        handleFieldsError(data)
      }
    } else if (response.status === HttpStatus.CREATED) {
      window.location.href = "/medications?created=true"
    }
  } catch (error) {
    console.error(error)
  }
}

function handleFieldsError(fieldsErrors) {
  const ulElements = document.querySelectorAll("ul")
  ulElements.forEach((ul) => {
    ul.innerHTML = null
  })

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "name")) {
    document
      .getElementById("name")
      .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.name))
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "medicationForm")) {
    document
      .getElementById("medicationForm")
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
      .getElementById("frequencies")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.frequencies),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "daysDuration")) {
    document
      .getElementById("daysDuration")
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
    const ilElement = document.createElement("il")
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
  const formJson = {}
  formJson["notes"] = notesEditor.getSemanticHTML()
  for (const [key, value] of formData.entries()) {
    formJson[key] = value
  }
  return JSON.stringify(formJson)
}
