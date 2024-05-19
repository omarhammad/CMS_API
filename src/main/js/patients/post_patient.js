import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", addNewPatient)

async function addNewPatient() {
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
  const response = await fetch("http://localhost:8080/api/patients", {
    method: "POST",
    headers: headers,
    body: patientJson,
  })

  if (response.status === HttpStatus.BAD_REQUEST) {
    const data = await response.json()
    if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
      showToast(data.exceptionMsg)
    } else {
      handleFieldsError(data)
    }
  } else if (response.status === HttpStatus.CREATED) {
    window.location.href = "/patients?created=true"
  }
}

function getFormData() {
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

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "username")) {
    document
      .getElementById("username")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.username),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "password")) {
    document
      .getElementById("password")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.password),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "confirmPassword")) {
    document
      .getElementById("confirm_password")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.confirmPassword),
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
