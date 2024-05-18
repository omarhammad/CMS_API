import { showToast } from "../util/toast.js"
const submitBtn = document.getElementById("submit_btn")

submitBtn.addEventListener("click", createDoctor)

async function createDoctor(event) {
  event.preventDefault()
  const doctorJson = getFormData()
  console.log(doctorJson)
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
    const response = await fetch("http://localhost:8080/api/doctors", {
      method: "POST",
      headers: headers,
      body: doctorJson,
    })

    const data = await response.json()

    if (response.status === 400) {
      if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
        console.log("Exception Msg")
        showToast(data.exceptionMsg)
      } else {
        console.log("Fields Errors")
        handleFieldsError(data)
      }
    } else if (response.ok) {
      console.log(data)
      window.location.href = "/doctors"
    } else {
      console.error("Error : ", response.status)
    }
  } catch (exc) {
    console.error("Sys Exception", exc)
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

function handleFieldsError(fieldsError) {
  const ulElementsList = document.querySelectorAll("ul")
  ulElementsList.forEach((ul) => {
    ul.innerHTML = null
  })

  if (Object.prototype.hasOwnProperty.call(fieldsError, "firstName")) {
    document
      .getElementById("first_name")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.firstName),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsError, "lastName")) {
    document
      .getElementById("last_name")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.lastName),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsError, "specialization")) {
    document
      .getElementById("spec")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.specialization),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsError, "phoneNumber")) {
    document
      .getElementById("phone_number")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.phoneNumber),
      )
  }
  if (Object.prototype.hasOwnProperty.call(fieldsError, "email")) {
    document
      .getElementById("email")
      .parentElement.appendChild(getFieldsErrorElementList(fieldsError.email))
  }

  if (Object.prototype.hasOwnProperty.call(fieldsError, "username")) {
    document
      .getElementById("username")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.username),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsError, "password")) {
    document
      .getElementById("password")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.password),
      )
  }

  if (Object.prototype.hasOwnProperty.call(fieldsError, "confirmPassword")) {
    document
      .getElementById("confirm_password")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsError.confirmPassword),
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
