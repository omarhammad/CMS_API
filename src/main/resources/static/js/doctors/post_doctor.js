const submitBtn = document.getElementById('submit_btn');
submitBtn.addEventListener('click', createDoctor)


async function createDoctor(event) {
    event.preventDefault();
    const doctorJson = getFormData();
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        [csrfHeader]: csrfToken
    });
    try {
        const response = await fetch('http://localhost:8080/api/doctors',
            {
                method: 'POST',
                headers: headers,
                body: doctorJson
            });

        const data = await response.json();

        if (response.status === 400) {
            if (data.hasOwnProperty('exceptionMsg')) {
                console.log('Exception Msg')
                showToast(data.exceptionMsg);
            } else {
                console.log('Fields Errors')
                handleFieldsError(data);
            }
        } else if (response.ok) {
            console.log(data)
            window.location.href = 'doctors_page.html';
        } else {
            console.error('Error : ', response.status)
        }
    } catch (exc) {
        console.error('Sys Exception', exc);
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


function handleFieldsError(fieldsError) {

    const ulElementsList = document.querySelectorAll('ul');
    ulElementsList.forEach((ul) => {
        ul.innerHTML = null;

    });

    if (fieldsError.hasOwnProperty('firstName')) {
        document.getElementById('first_name')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsError.firstName));
    }

    if (fieldsError.hasOwnProperty('lastName')) {
        document.getElementById('last_name')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsError.lastName));
    }

    if (fieldsError.hasOwnProperty('specialization')) {
        document.getElementById('spec')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsError.specialization));
    }
    if (fieldsError.hasOwnProperty('phoneNumber')) {
        document.getElementById('phone_number')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsError.phoneNumber));
    }
    if (fieldsError.hasOwnProperty('email')) {
        document.getElementById('email')
            .parentElement.appendChild(getFieldsErrorElementList(fieldsError.email));
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