import { showToast } from "../util/toast.js"
import { csrf_header, csrf_token } from "../util/csrf.js"
import * as Joi from "joi"

const submitBtn = document.getElementById("submit_btn")

submitBtn.addEventListener("click", createDoctor)

async function createDoctor(event) {
  event.preventDefault()
  const doctorJson = getFormData()

  const schema = Joi.object({
    firstName: Joi.string().required().min(3).max(50).messages({
      "string.empty": "First name is required!",
      "string.base": "First name must be string",
      "string.min": " First name must be at least 3 letters",
      "string.max": "First name must be at most 50 letters",
    }),
    lastName: Joi.string().required().min(3).max(50).messages({
      "string.empty": "Last name is required!",
      "string.min": "Last name must be at least 3 letters",
      "string.max": "Last name must be at most 50 letters",
    }),
    specialization: Joi.string().required().messages({
      "string.empty": "Specialization is required!",
      "string.base": "You must specify your Specialization as string",
    }),
    phoneNumber: Joi.string()
      .required()
      .pattern(/^(?:\+|00)\d{1,3}[\s-]?\d{1,4}[\s-]?\d{1,4}[\s-]?\d{1,4}$/)
      .messages({
        "string.empty": "Phone-number  is required!",
        "string.pattern.base": "Phone number format should be '+32xxx xxxxxx'",
      }),
    email: Joi.string()
      .required()
      .email({ tlds: { allow: false } })
      .messages({
        "string.empty": "email is required!",
        "string.email": "Email format should be 'example@email.com' ",
      }),
    username: Joi.string()
      .required()
      .pattern(/^[a-zA-Z][a-zA-Z0-9_-]{2,15}$/)
      .messages({
        "string.empty": "username is required!",
        "string.pattern.base":
          " username must starts with char[A-Za-z] and between 3 and 15 letters",
      }),
    password: Joi.string().required().min(8).messages({
      "string.empty": "Password  is required!",
      "string.min": "Password must be at least 8 letters",
    }),
    confirmPassword: Joi.string()
      .required()
      .valid(Joi.ref("password"))
      .messages({
        "string.empty": "Confirm password is required!",
        "any.only": "Confirm password must match password",
      }),
  })
  const validation = schema.validate(doctorJson, {
    abortEarly: false,
  })

  if (validation.error) {
    const errorMessages = validation.error.details.reduce((acc, curr) => {
      acc[curr.path[0]] = curr.message
      return acc
    }, {})
    handleFieldsError(errorMessages)
  } else {
    const headers = new Headers({
      Accept: "application/json",
      "Content-Type": "application/json",
      [csrf_header]: csrf_token,
    })
    try {
      const response = await fetch("http://localhost:8080/api/doctors", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(doctorJson),
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
}

function getFormData() {
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {}
  for (const [key, value] of formData.entries()) {
    formJson[key] = value
  }
  return formJson
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
