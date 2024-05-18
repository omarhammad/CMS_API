import { HttpStatus } from "../util/httpStatus.js"
window.addEventListener("DOMContentLoaded", loadMedication)

async function loadMedication() {
  const pathname = window.location.pathname
  const segments = pathname.split("/")
  const medication_id = segments.pop()
  try {
    const response = await fetch(
      `http://localhost:8080/api/medications/${medication_id}`,
    )

    if (response.status === HttpStatus.NOT_FOUND) {
      console.log("NOT FOUND")
    } else if (response.ok) {
      const medication = await response.json()
      fillInPage(medication)
    }
  } catch (error) {
    console.error(error)
  }
}

function fillInPage(medication) {
  document.getElementById("name").innerText = medication.name
  document.getElementById("form_usage").innerHTML =
    "&nbsp;&nbsp;&nbsp;&nbsp;" + medication.form + ` - ${medication.usage}`
  document.getElementById("dosage").innerText =
    medication.dosage.quantity + ` ${medication.dosage.unit}`
  document.getElementById("freq").innerText =
    medication.frequencies + " times per day"
  document.getElementById("duration").innerText =
    medication.daysDuration + " days"
  document.getElementById("notes").innerHTML =
    "&nbsp;&nbsp;&nbsp;&nbsp;" + medication.notes
}
