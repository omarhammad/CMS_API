const HttpStatus = {
    OK: 200,
    CREATED: 201,
    ACCEPTED: 202,
    NO_CONTENT: 204,
    MOVED_PERMANENTLY: 301,
    FOUND: 302,
    SEE_OTHER: 303,
    NOT_MODIFIED: 304,
    BAD_REQUEST: 400,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,
    NOT_FOUND: 404,
    METHOD_NOT_ALLOWED: 405,
    CONFLICT: 409,
    INTERNAL_SERVER_ERROR: 500,
    NOT_IMPLEMENTED: 501,
    BAD_GATEWAY: 502,
    SERVICE_UNAVAILABLE: 503,
    GATEWAY_TIMEOUT: 504
};

const medications_tbody = document.getElementById('medications_tbody');
window.addEventListener('DOMContentLoaded', loadAllMedications);

async function loadAllMedications() {

    const searchParam = new URLSearchParams(window.location.search);
    if (searchParam.has('created')) {
        removeQueryParam('created')
        showToast('New Medication created!')
    } else if (searchParam.has("updated")) {
        removeQueryParam("updated");
        showToast('Medication updated!')
    }


    try {
        const response = await fetch('http://localhost:8080/api/medications/');
        if (response.status === HttpStatus.NO_CONTENT) {
            console.log("NO CONTENT");
            let noContentRow = document.createElement('tr');
            let noContentCell = document.createElement('td');
            noContentCell.innerHTML = "NO APPOINTMENTS YET!";
            noContentCell.colSpan = 7;
            noContentRow.appendChild(noContentCell);
            medications_tbody.appendChild(noContentRow);
        } else if (response.status === HttpStatus.OK) {
            const medications = await response.json();
            await fillMedicationsTable(medications)
        }
    } catch (error) {
        console.error(error)
    }

}

async function fillMedicationsTable(medications) {
    const current_user = await getcurrentUser();

    for (const medication of medications) {
        const medication_row = document.createElement('tr')

        const name_td = document.createElement('td')
        name_td.innerText = medication.name;

        const med_form_td = document.createElement('td')
        med_form_td.innerText = medication.form;

        const dosage_td = document.createElement('td');
        const dosage = medication.dosage;
        dosage_td.innerText = dosage.quantity + ' ' + dosage.unit;

        const freq_td = document.createElement('td')
        freq_td.innerText = medication.frequencies;

        const duration_td = document.createElement('td')
        duration_td.innerText = medication.daysDuration;



        medication_row.appendChild(name_td);
        medication_row.appendChild(med_form_td)
        medication_row.appendChild(dosage_td)
        medication_row.appendChild(freq_td)
        medication_row.appendChild(duration_td)
        if (!current_user.userRoles.includes('ROLE_PATIENT')) {
            // DELETE BUTTON
            const deleteBtn = document.createElement('td');
            deleteBtn.className = "bi bi-trash-fill text-danger";
            deleteBtn.addEventListener('click', event => {
                event.stopPropagation();
                deleteMedication(medication.medicationId, medication.name)
            })

            //EDIT BUTTON
            const editBtn = document.createElement('td');
            editBtn.className = "bi bi-pencil-fill text-primary"
            editBtn.addEventListener('click', event => {
                event.stopPropagation();
                window.location.href = `/medications/update/${medication.medicationId}`;

            })
            medication_row.appendChild(deleteBtn)
            medication_row.appendChild(editBtn)
        }

        medication_row.addEventListener('click', event => {
            window.location.href = `/medications/details/${medication.medicationId}`;
        });
        medications_tbody.appendChild(medication_row);
    }


}

async function deleteMedication(medication_id, medication_name) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        [csrfHeader]: csrfToken
    })
    try {
        const response = await fetch(`http://localhost:8080/api/medications/${medication_id}`, {
            method: 'DELETE',
            headers: headers
        });

        if (response.status === HttpStatus.NO_CONTENT) {
            medications_tbody.innerHTML = ''
            //medication_deleted = true;
            showToast(medication_name + ' is deleted!')
            loadAllMedications();
        } else if (response.status === HttpStatus.BAD_REQUEST) {
            const data = await response.json();
            showToast(data.exceptionMsg)
        }
    } catch (error) {
        console.error(error)
    }


}

function showToast(message) {
    let toastElement = document.querySelector('.toast');
    let toastBody = toastElement.querySelector('.toast-body');
    toastBody.innerText = message;

    let toast = new bootstrap.Toast(toastElement, {
        autohide: false
    });
    toast.show();

}

function removeQueryParam(paramToRemove) {
    // Create a URL object based on the current location
    const url = new URL(window.location);

    // Use URLSearchParams to work with the query string easily
    const queryParams = url.searchParams;

    // Remove the specified query parameter
    queryParams.delete(paramToRemove);

    // Update the URL without reloading the page
    history.pushState(null, '', url.pathname + '?' + queryParams.toString() + url.hash);
}

async function getcurrentUser() {
    const response = await fetch('http://localhost:8080/api/auth/user/current');

    if (response.status === HttpStatus.UNAUTHORIZED) {
        window.location.href = '/signIn'
    } else if (response.status === HttpStatus.OK) {
        return await response.json();
    }

}

