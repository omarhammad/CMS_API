import { HttpStatus } from "../util/httpStatus.js"
import { showToast } from "../util/toast.js"
import { getCurrentUser } from "../util/currentUser.js"

window.addEventListener("DOMContentLoaded", loadAllPatients)
const patientTable = document.getElementById("patients_table_body")

async function loadAllPatients() {
  patientTable.innerHTML = null
  const urlParam = new URLSearchParams(window.location.search)
  if (urlParam.has("created")) {
    removeQueryParam("created")
    showToast("Patient added!")
  } else if (urlParam.has("updated")) {
    removeQueryParam("updated")
    showToast("Patient updated!")
  } else if (urlParam.has("not_found")) {
    removeQueryParam("not_found")
    showToast("Patient Not Found!")
  }

  try {
    const response = await fetch("http://localhost:8080/api/patients")

    if (response.status === HttpStatus.NO_CONTENT) {
      let noContentRow = document.createElement("tr")
      let noContentCell = document.createElement("td")
      noContentCell.innerHTML = "NO PATIENTS YET!"
      noContentCell.colSpan = 7
      noContentRow.appendChild(noContentCell)
      patientTable.appendChild(noContentRow)
    } else if (response.status === HttpStatus.OK) {
      const patients = await response.json()
      await fillInTable(patients)
    }
  } catch (error) {
    console.error(error)
  }
}

async function fillInTable(patients) {
  for (const patient of patients) {
    const table_row = document.createElement("tr")
    for (const key in patient) {
      const table_cell = document.createElement("td")
      if (key === "id" || key === "hisDoctors" || key === "oldAppointments")
        continue

      if (key === "nationalNumber") {
        table_cell.innerText = patient[key]
        const clipboard = document.createElement("i")
        clipboard.className = "bi bi-clipboard m-2 text-success"

        clipboard.addEventListener("click", async (event) => {
          event.stopPropagation()
          const textToCopy = table_cell.innerText
          try {
            await navigator.clipboard.writeText(textToCopy)
            clipboard.className = ""
            clipboard.className = "bi bi-clipboard-check m-2 text-danger" // Update for Font Awesome if needed
            setTimeout(() => {
              clipboard.className = "bi bi-clipboard m-2 text-success"
            }, 2000) // Reset after 2 seconds
          } catch (err) {
            console.error("Failed to copy:", err)
          }
        })
        table_cell.appendChild(clipboard)
      } else {
        table_cell.innerText = patient[key]
      }
      table_row.appendChild(table_cell)
    }

    const current_user = await getCurrentUser()

    if (
      !(
        current_user.userRoles.includes("ROLE_DOCTOR") ||
        current_user.userRoles.includes("ROLE_SECRETARY")
      )
    ) {
      // DELETE BUTTON
      const deleteBtn = document.createElement("td")
      deleteBtn.className = "bi bi-trash-fill text-danger"
      deleteBtn.addEventListener("click", async (event) => {
        event.stopPropagation()
        await deletePatient(patient["id"])
      })
      table_row.appendChild(deleteBtn)
    }

    //EDIT BUTTON
    const editBtn = document.createElement("td")
    editBtn.className = "bi bi-pencil-fill text-primary"
    editBtn.addEventListener("click", (event) => {
      event.stopPropagation()
      window.location.href = `/patients/update/${patient.id}`
    })
    table_row.appendChild(editBtn)

    table_row.addEventListener("click", () => {
      window.location.href = `/patients/details/${patient.id}`
    })
    patientTable.appendChild(table_row)
  }
}

async function deletePatient(patient_id) {
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
  try {
    const response = await fetch(
      `http://localhost:8080/api/patients/${patient_id}`,
      {
        method: "DELETE",
        headers: headers,
      },
    )

    if (response.status === HttpStatus.NOT_FOUND) {
      showToast("Patient Not Found!")
    } else if (response.status === HttpStatus.NO_CONTENT) {
      showToast("Patient deleted!")
      await loadAllPatients()
    }
  } catch (error) {
    console.error(error)
  }
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
