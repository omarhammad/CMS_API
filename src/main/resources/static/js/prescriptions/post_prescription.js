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
const submitBtn = document.getElementById('submitBtn');
const appointment_id = window.location.pathname.split('/').pop();

submitBtn.addEventListener('click', addNewPrescription);

async function addNewPrescription() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        [csrfHeader]: csrfToken
    })
    const prescriptionBody = getFormData();
    console.log(prescriptionBody);
    try {
        const response = await fetch(`http://localhost:8080/api/appointments/${appointment_id}/prescription`,
            {
                method: 'POST',
                headers: headers,
                body: prescriptionBody
            });

        if (response.status === HttpStatus.BAD_REQUEST) {
            console.log(response.statusText)
            const data = await response.json();
            if (data.hasOwnProperty('exceptionMsg')) {
                showToast(data.exceptionMsg);
            } else {
                handleFieldsError(data);
            }
        } else if (response.status === HttpStatus.CREATED) {
            console.log(response.statusText)
            window.location.href = `/appointments/details/${appointment_id}?created=true`;
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

function handleFieldsError(fieldsErrors) {

    const ulElements = document.querySelectorAll('ul');
    ulElements.forEach(ul => {
        ul.innerHTML = null;
    })

    if (fieldsErrors.hasOwnProperty('expireDate')) {
        document.getElementById('expireDate')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.expireDate))
    }

    if (fieldsErrors.hasOwnProperty('medicationsIds')) {
        document.getElementById('medications')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.medicationsIds))
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

function getFormData() {
    const form = document.getElementById('form');
    const formData = new FormData(form);
    const jsonData = {
        'appointmentId': appointment_id,
        'medicationsIds': formData.getAll('medicationsIds')
    };

    formData.forEach((value, key) => {
        if (key !== 'medicationsIds') {
            jsonData[key] = value;
        }
    });

    return JSON.stringify(jsonData);
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

