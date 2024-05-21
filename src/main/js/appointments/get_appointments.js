import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
import { getCurrentUser } from "../util/currentUser.js"
import { formatDate, formatTime } from "../util/date_time_formatter.js"

window.addEventListener("DOMContentLoaded", loadAllAppointments)

let appointment_deleted = false

async function loadAllAppointments() {
  const urlParams = new URLSearchParams(window.location.search)
  if (urlParams.has("created")) {
    removeQueryParam("created")
    showToast("New Appointment added!")
  } else if (urlParams.has("updated")) {
    removeQueryParam("updated")
    showToast("Appointment updated!")
  } else if (appointment_deleted) {
    showToast("Appointment Deleted!")
    appointment_deleted = false
  }

  const appointmentTableBody = document.getElementById(
    "appointments_table_body",
  )
  let requestUrl
  const current_user = await getCurrentUser()
  if (current_user.userRoles.includes("ROLE_PATIENT")) {
    requestUrl = `http://localhost:8080/api/appointments/patient/${current_user.userId}`
  } else if (current_user.userRoles.includes("ROLE_DOCTOR")) {
    requestUrl = `http://localhost:8080/api/appointments/doctor/${current_user.userId}`
  } else {
    requestUrl = "http://localhost:8080/api/appointments/"
  }
  fetch(requestUrl)
    .then((response) => {
      if (response.status === HttpStatus.NO_CONTENT) {
        let noContentRow = document.createElement("tr")
        let noContentCell = document.createElement("td")
        noContentCell.innerHTML = "NO APPOINTMENTS YET!"
        noContentCell.colSpan = 8
        noContentRow.appendChild(noContentCell)
        appointmentTableBody.appendChild(noContentRow)
      } else if (response.status === HttpStatus.OK) {
        return response.json()
      }
    })
    .then((appointments) => {
      for (const appointment of appointments) {
        const appointment_row = document.createElement("tr")
        for (const key in appointment) {
          if (key === "appointmentId" || key === "prescription") continue
          else if (key === "availabilitySlot") {
            const appointmentDateTime = new Date(
              appointment.availabilitySlot.slot,
            )

            const date_td = document.createElement("td")
            date_td.innerText = formatDate(appointmentDateTime)

            const time_td = document.createElement("td")
            time_td.innerText = formatTime(appointmentDateTime)

            appointment_row.appendChild(date_td)
            appointment_row.appendChild(time_td)
          } else if (key === "doctor" || key === "patient") {
            const full_name =
              appointment[key].firstName + " " + appointment[key].lastName
            const stakeholder_td = document.createElement("td")
            stakeholder_td.innerText = full_name
            appointment_row.appendChild(stakeholder_td)
          } else {
            const td = document.createElement("td")
            td.innerText = appointment[key]
            appointment_row.appendChild(td)
          }
        }

        if (!current_user.userRoles.includes("ROLE_PATIENT")) {
          // DELETE BUTTON

          const deleteBtn = document.createElement("td")
          deleteBtn.className = "bi bi-trash-fill text-danger"
          deleteBtn.addEventListener("click", (event) => {
            event.stopPropagation()
            deleteAppointment(appointment.appointmentId)
          })

          //EDIT BUTTON
          const editBtn = document.createElement("td")
          editBtn.className = "bi bi-pencil-fill text-primary"
          editBtn.addEventListener("click", (event) => {
            event.stopPropagation()
            window.location.href = `/appointments/update/${appointment.appointmentId}`
          })

          // Adding a remove button and update button in case the appointment
          // is in the past as it's going to be saved as historical appointment
          const app_date = new Date(appointment.availabilitySlot.slot)
          const current_date = new Date()
          if (app_date > current_date) {
            appointment_row.appendChild(deleteBtn)
            appointment_row.appendChild(editBtn)
          } else {
            appointment_row.appendChild(document.createElement("td"))
            appointment_row.appendChild(document.createElement("td"))
          }
        }

        appointment_row.addEventListener("click", () => {
          window.location.href = `/appointments/details/${appointment.appointmentId}`
        })
        appointmentTableBody.appendChild(appointment_row)
      }
    })
    .catch((err) => {
      console.error(err)
    })
}

function deleteAppointment(appointmentId) {
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content")
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content")
  const headers = new Headers({
    [csrfHeader]: csrfToken,
  })
  fetch(`http://localhost:8080/api/appointments/${appointmentId}`, {
    method: "DELETE",
    headers: headers,
  })
    .then((response) => {
      if (response.status === HttpStatus.NO_CONTENT) {
        document.getElementById("appointments_table_body").innerHTML = ""
        appointment_deleted = true
        loadAllAppointments()
      }
    })
    .catch((err) => {
      console.error(err)
    })
}

function removeQueryParam(paramToRemove) {
  // Create a URL object based on the current location
  const url = new URL(window.location)

  // Use URLSearchParams to work with the query string easily
  const queryParams = url.searchParams

  // Remove the specified query parameter
  queryParams.delete(paramToRemove)

  // Update the URL without reloading the page
  history.pushState(
    null,
    "",
    url.pathname + "?" + queryParams.toString() + url.hash,
  )
}
