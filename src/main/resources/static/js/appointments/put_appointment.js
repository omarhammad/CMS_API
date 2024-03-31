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

window.addEventListener('DOMContentLoaded', loadAppointmentData)

async function loadAppointmentData() {

    const pathname = window.location.pathname;
    const segments = pathname.split('/');
    const appointment_id = segments.pop();

    try {
        const response = await fetch(`http://localhost:8080/api/appointments/${appointment_id}`);
        if (response.status === HttpStatus.NOT_FOUND) {
            console.log("NOT FOUND !");
        } else if (response.ok) {
            const appointment = await response.json();

            const current_user = await getcurrentUser();

            if (current_user.userRoles.includes('ROLE_PATIENT')) {
                const patient_nn_input = document.getElementById('patient_nn');
                patient_nn_input.value = appointment.patient.nationalNumber;
                patient_nn_input.disabled = true

            } else if (current_user.userRoles.includes('ROLE_DOCTOR')) {
                const doctor_input = document.getElementById('doctor');
                doctor_input.value = appointment.doctor.id
                doctor_input.disabled = true;
            }
            document.getElementById('appointment_date_time').value = appointment.appointmentDateTime
            document.getElementById('appointment_type').value = appointment.appointmentType
            document.getElementById('purpose').value = appointment.purpose
        }
    } catch (error) {
        console.error(error)
    }


}


const submitBtn = document.getElementById('submitBtn');
submitBtn.addEventListener('click', put_appointments)
const pathname = window.location.pathname;
const segments = pathname.split('/');
const appointment_id = segments.pop();

async function put_appointments() {


    const csrf_token = document.querySelector('meta[name="_csrf"]').getAttribute('content')
    const csrf_header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        [csrf_header]: csrf_token
    }

    const appointmentData = getFormData()
    try {
        const response = await fetch(`http://localhost:8080/api/appointments/${appointment_id}`,
            {
                method: 'PUT',
                headers: headers,
                body: appointmentData
            });
        if (response.status === HttpStatus.BAD_REQUEST) {
            const data = await response.json();
            if (data.hasOwnProperty('exceptionMsg')) {
                showToast(data.exceptionMsg);
            } else {
                handleFieldsError(data);
            }
        } else if (response.status === HttpStatus.NO_CONTENT) {
            window.location.href = '/appointments?updated=true';
        } else {
            console.error("MyError:" + response.status)
        }

    } catch (error) {
        console.error(error)
    }
}

function getFormData() {
    document.getElementById('doctor').disabled = false;
    document.getElementById('patient_nn').disabled = false;

    const form = document.getElementById('form');
    const formData = new FormData(form);
    const formJson = {
        'appointmentId': appointment_id
    };
    for (const [key, value] of formData.entries()) {
        formJson[key] = value;
    }
    return JSON.stringify(formJson);
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


function handleFieldsError(fieldsErrors) {

    const ulElements = document.querySelectorAll('ul');
    ulElements.forEach(ul => {
        ul.innerHTML = null;
    })


    if (fieldsErrors.hasOwnProperty('appointmentDateTime')) {
        document.getElementById('appointment_date_time')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.appointmentDateTime));

    }

    if (fieldsErrors.hasOwnProperty('patientNN')) {
        document.getElementById('patient_nn')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.patientNN))
    }


}

function getFieldsErrorElementList(errors) {

    const ulElement = document.createElement('ul');
    ulElement.className = "custom-bullet";
    errors = errors.split(';');
    for (const error of errors) {
        const ilElement = document.createElement('il');
        ilElement.className = 'form-error';
        ilElement.innerText = '* ' + error;
        ulElement.appendChild(ilElement);

        ulElement.appendChild(document.createElement('br'))
    }

    return ulElement;

}


async function getcurrentUser() {
    const response = await fetch('http://localhost:8080/api/auth/user/current');

    if (response.status === HttpStatus.UNAUTHORIZED) {
        window.location.href = '/signIn'
    } else if (response.status === HttpStatus.OK) {
        return await response.json();
    }

}