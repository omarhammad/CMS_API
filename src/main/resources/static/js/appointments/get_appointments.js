window.addEventListener("DOMContentLoaded", loadAllAppointments);

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
let appointment_deleted = false;

function loadAllAppointments() {

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('created')) {
        removeQueryParam('created')
        showToast("New Appointment added!")

    } else if (urlParams.has('updated')) {
        removeQueryParam('updated')
        showToast("Appointment updated!");
    } else if (appointment_deleted) {
        showToast("Appointment Deleted!");
        appointment_deleted = false
    }


    const appointmentTableBody = document.getElementById('appointments_table_body');
    fetch('http://localhost:8080/api/appointments/')
        .then(response => {
            if (response.status === HttpStatus.NO_CONTENT) {
                let noContentRow = document.createElement('tr');
                let noContentCell = document.createElement('td');
                noContentCell.innerHTML = "NO APPOINTMENTS YET!";
                noContentCell.colSpan = 8;
                noContentRow.appendChild(noContentCell);
                appointmentTableBody.appendChild(noContentRow);
            } else if (response.status === HttpStatus.OK) {
                return response.json();
            }

        }).then(appointments => {

        for (const appointment of appointments) {
            const appointment_row = document.createElement('tr');
            for (const key in appointment) {
                if (key === 'appointmentId' || key === 'prescription') continue;
                else if (key === 'appointmentDateTime') {
                    const appointmentDateTime = new Date(appointment[key]);

                    const date_td = document.createElement("td")
                    date_td.innerText = formatDate(appointmentDateTime)


                    const time_td = document.createElement("td")
                    time_td.innerText = formatTime(appointmentDateTime)

                    appointment_row.appendChild(date_td)
                    appointment_row.appendChild(time_td);

                } else if (key === 'doctor' || key === 'patient') {
                    const full_name = appointment[key].firstName + ' ' + appointment[key].lastName;
                    const stakeholder_td = document.createElement('td');
                    stakeholder_td.innerText = full_name;
                    appointment_row.appendChild(stakeholder_td);
                } else {
                    const td = document.createElement('td');
                    td.innerText = appointment[key];
                    appointment_row.appendChild(td)
                }
            }

            // DELETE BUTTON
            const deleteBtn = document.createElement('td');
            deleteBtn.className = "bi bi-trash-fill text-danger";
            deleteBtn.addEventListener('click', event => {
                event.stopPropagation();
                deleteAppointment(appointment.appointmentId)
            })

            //EDIT BUTTON
            const editBtn = document.createElement('td');
            editBtn.className = "bi bi-pencil-fill text-primary"
            editBtn.addEventListener('click', event => {
                event.stopPropagation();
                window.location.href = `/appointments/update/${appointment.appointmentId}`;

            })

            // Adding a remove button and update button in case the appointment
            // is in the past as it's going to be saved as historical appointment
            const app_date = new Date(appointment.appointmentDateTime)
            const current_date = new Date();
            if (app_date > current_date) {
                appointment_row.appendChild(deleteBtn);
                appointment_row.appendChild(editBtn);
            } else {
                appointment_row.appendChild(document.createElement('td'))
                appointment_row.appendChild(document.createElement('td'))
            }


            appointment_row.addEventListener('click', event => {
                window.location.href = `/appointments/details/${appointment.appointmentId}`;
            });
            appointmentTableBody.appendChild(appointment_row)
        }

    }).catch(err => {
        console.error(err);
    });

}

function formatTime(date) {
    let hours = date.getHours();
    const minutes = date.getMinutes();
    const ampm = hours >= 12 ? 'PM' : 'AM';

    hours %= 12;
    hours = hours || 12; // the hour '0' should be '12'
    const minutesFormatted = minutes < 10 ? '0' + minutes : minutes;

    return `${hours}:${minutesFormatted} ${ampm}`;
}

function formatDate(date) {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // getMonth() is zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
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

function deleteAppointment(appointmentId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        [csrfHeader]: csrfToken
    })
    fetch(`http://localhost:8080/api/appointments/${appointmentId}`, {
        method: 'DELETE',
        headers: headers
    })
        .then(response => {
            if (response.status === HttpStatus.NO_CONTENT) {
                document.getElementById('appointments_table_body').innerHTML = ''
                appointment_deleted = true;
                loadAllAppointments();
            }
        }).catch(err => {
        console.error(err);
    });


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


