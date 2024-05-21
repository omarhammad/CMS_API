import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
import { getCurrentUser } from "../util/currentUser.js"
import { formatDate, formatTime } from "../util/date_time_formatter.js"
import { csrf_header, csrf_token } from "../util/csrf.js"
import * as Joi from "joi"

const submitBtn = document.getElementById("submitBtn")
submitBtn.addEventListener("click", postNewAppointment)

async function postNewAppointment() {
  const headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
    [csrf_header]: csrf_token,
  }

  const appointmentData = await getFormData()

  const schema = Joi.object({
    doctor: Joi.number().positive().required().messages({
      "any.required": "Doctor id must be provided!",
      "number.base": "Doctor id must be a number",
      "number.positive": "Doctor id must be a positive number",
    }),
    patientNN: Joi.string()
      .pattern(/^\d{2}\.\d{2}\.\d{2}-\d{3}\.\d{2}$/)
      .required()
      .messages({
        "string.empty": "National Number must be provided",
        "string.pattern.base":
          "National Number must be provided as e.g 'yy.mm.dd-xxx.cd'",
      }),
    appointmentSlotId: Joi.number().positive().required().messages({
      "any.required": "Appointment Date and Time must be provided!",
      "number.base": "Appointment Slot Id must be a number",
      "number.positive": "Appointment Slot Id must be a positive number",
    }),
    appointmentType: Joi.string().required().messages({
      "any.required": "Appointment Type must be provided!",
      "string.base": "Appointment Type must be a string",
    }),
    purpose: Joi.string().allow("", null).messages({
      "string.base": "Purpose must be a string",
    }),
  })

  const validate = schema.validate(appointmentData, {
    abortEarly: false,
  })

  if (validate.error) {
    validate.error.details.forEach((curr) => {
      console.log(curr)
    })
    const errorMessages = validate.error.details.reduce((acc, curr) => {
      acc[curr.path[0]] = curr.message
      return acc
    }, {})
    console.log(errorMessages)
    handleFieldsError(errorMessages)
  } else {
    try {
      const response = await fetch("http://localhost:8080/api/appointments/", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(appointmentData),
      })
      const data = await response.json()
      if (response.status === HttpStatus.BAD_REQUEST) {
        if (Object.prototype.hasOwnProperty.call(data, "exceptionMsg")) {
          showToast(data.exceptionMsg)
        } else {
          handleFieldsError(data)
        }
      } else if (response.status === HttpStatus.CREATED) {
        window.location.href = "/appointments?created=true"
      } else {
        console.error("MyError:" + response.status)
      }
    } catch (error) {
      console.error(error.message)
    }
  }
}

async function getFormData() {
  document.getElementById("doctor").disabled = false
  document.getElementById("patient_nn").disabled = false
  const form = document.getElementById("form")
  const formData = new FormData(form)
  const formJson = {}
  for (const [key, value] of formData.entries()) {
    formJson[key] = value
  }

  return formJson
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

  if (Object.prototype.hasOwnProperty.call(fieldsErrors, "purpose")) {
    document
      .getElementById("purpose")
      .parentElement.appendChild(
        getFieldsErrorElementList(fieldsErrors.purpose),
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
  const current_user = await getCurrentUser()

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

  const doctorSelection = document.getElementById("doctor")
  setAvailabilities(doctorSelection.value)
  doctorSelection.addEventListener("change", (event) => {
    const doctorId = event.target.value
    setAvailabilities(doctorId)
  })
}

async function setAvailabilities(doctorId) {
  const appointmentSlots = document.getElementById("appointment_slots")
  const availabilities_response = await fetch(
    `/api/doctors/${doctorId}/availability`,
  )
  if (availabilities_response.status === HttpStatus.NOT_FOUND) {
    showToast("Doctor Not Found!")
  } else if (availabilities_response.status === HttpStatus.NO_CONTENT) {
    appointmentSlots.innerHTML = null
    const no_slots = document.createElement("option")
    no_slots.innerText = "NO AVAILABLE SLOTS!"
    appointmentSlots.appendChild(no_slots)
    appointmentSlots.disabled = true
  } else if (availabilities_response.status === HttpStatus.OK) {
    appointmentSlots.innerHTML = null
    const slots = await availabilities_response.json()
    for (const slot of slots) {
      const slot_option = document.createElement("option")
      const slot_date_time = new Date(slot.slot)
      slot_option.innerText =
        formatDate(slot_date_time) + " - " + formatTime(slot_date_time)
      slot_option.value = slot.id
      appointmentSlots.appendChild(slot_option)
    }
    appointmentSlots.disabled = false
  }
}
