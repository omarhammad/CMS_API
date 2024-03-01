const doctor_id = new URLSearchParams(window.location.search).get('doctor_id');


window.addEventListener('DOMContentLoaded', async () => {
    const doctor = await getOneDoctor(doctor_id);
    const form = document.getElementById('updateForm');

    for (const key in doctor) {
        if (key === 'contactInfo') {
            const contactInfo = doctor[key].split(',');
            form.elements['phoneNumber'].value = contactInfo[0];
            form.elements['email'].value = contactInfo[1];
            continue;
        } else if (key === 'hisPatients') {
            continue;
        }
        const input = form.elements[key];
        input.value = doctor[key];
    }
});

export async function getOneDoctor(doctor_id) {
    try {
        const response = await fetch(`http://localhost:8080/api/doctors/${doctor_id}`);
        if (response.status === 404) {
            console.log("NOT FOUND");
        }
        return await response.json();
    } catch (exc) {
        console.error(exc);
        return null;
    }
}


document.getElementById('submit_btn').addEventListener('click', updateDoctor);

async function updateDoctor(event) {
    event.preventDefault();
    const doctorJson = getFormData();

    try {
        const response = await fetch(`http://localhost:8080/api/doctors/${doctor_id}`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: doctorJson
        });

        let data = {};
        if (response.status === 400) {
            data = await response.json();
            if (data.hasOwnProperty('exceptionMsg')) {
                console.log('Exception Msg')
                showToast(data.exceptionMsg);
            } else {
                console.log('Fields Errors')
                handleFieldsError(data);
            }

        } else if (response.status === 409) {
            showToast("CONFLICT IN THE REQUEST!")

        } else if (response.status === 404) {
            showToast("DOCTOR NOT FOUND!");

        } else if (response.status === 204) {
            window.location.href = 'doctors_page.html';
        } else {
            console.error('Error : ', response.status);
        }
    } catch (exc) {
        console.error("SYS ERROR : ", exc);
    }
}

function getFormData() {

    const form = document.getElementById('updateForm');
    const formData = new FormData(form);

    const dataJson = {};

    dataJson['id'] = parseInt(doctor_id);

    for (const [key, value] of formData) {
        dataJson[key] = value;
    }
    return JSON.stringify(dataJson);
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