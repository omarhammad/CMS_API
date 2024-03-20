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
submitBtn.addEventListener('click', postNewAppointment);


async function postNewAppointment() {

    const csrf_token = document.querySelector('meta[name="_csrf"]').getAttribute('content')
    const csrf_header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        [csrf_header]: csrf_token
    }

    const appointmentData = getFormData()
    try {
        const response = await fetch('http://localhost:8080/api/appointments/',
            {
                method: 'POST',
                headers: headers,
                body: appointmentData
            });
        console.log("Entered 1")
        const data = await response.json();
        if (response.status === HttpStatus.BAD_REQUEST) {
            console.log("Entered 2")
            if (data.hasOwnProperty('exceptionMsg')) {
                showToast(data.exceptionMsg);
            } else {
                handleFieldsError(data);
            }
        } else if (response.status === HttpStatus.CREATED) {
            console.log("Entered 3")
            console.log(data);
            window.location.href = '/appointments?created=true';
        } else {
            console.error("MyError:" + response.status)
        }

    } catch (error) {
        console.error(error.message)
    }
}

function getFormData() {

    const form = document.getElementById('form');
    const formData = new FormData(form);
    const formJson = {};
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
