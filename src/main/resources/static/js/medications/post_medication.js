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
submitBtn.addEventListener('click', addNewMedication);

async function addNewMedication(event) {
    event.preventDefault()
    const medicationJson = getFormData()
    console.log(medicationJson)
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        [csrfHeader]: csrfToken
    });

    try {
        const response = await fetch('http://localhost:8080/api/medications/',
            {
                method: 'POST',
                headers: headers,
                body: medicationJson
            });
        if (response.status === HttpStatus.BAD_REQUEST) {
            const data = await response.json();
            if (data.hasOwnProperty('exceptionMsg')) {
                showToast(data.exceptionMsg);
            } else {
                handleFieldsError(data);
            }

        } else if (response.status === HttpStatus.CREATED) {
            window.location.href = '/medications?created=true';
        }

    } catch (error) {
        console.error(error);
    }

}


function handleFieldsError(fieldsErrors) {

    const ulElements = document.querySelectorAll('ul');
    ulElements.forEach(ul => {
        ul.innerHTML = null;
    })


    if (fieldsErrors.hasOwnProperty('name')) {
        document.getElementById('name')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.name))
    }

    if (fieldsErrors.hasOwnProperty('medicationForm')) {
        document.getElementById('medicationForm')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.medicationForm))
    }


    if (fieldsErrors.hasOwnProperty('quantity')) {
        document.getElementById('quantity')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.quantity))
    }
    if (fieldsErrors.hasOwnProperty('unit')) {
        document.getElementById('unit')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.unit))
    }
    if (fieldsErrors.hasOwnProperty('frequencies')) {
        document.getElementById('frequencies')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.frequencies))
    }
    if (fieldsErrors.hasOwnProperty('daysDuration')) {
        document.getElementById('daysDuration')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsErrors.daysDuration))
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
