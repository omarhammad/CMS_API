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

async function getOneDoctor(doctor_id) {
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

window.addEventListener('DOMContentLoaded', async event => { // Add async keyword here
    const pathname = window.location.pathname;
    const segments = pathname.split('/');
    const doctor_id = Number(segments.pop());
    const data = await getOneDoctor(doctor_id);
    if (data) {
        console.log(data)
        document.getElementById('fullName').innerText = data.firstName + ' ' + data.lastName;
        document.getElementById('specialization').innerText = data.specialization;
        const contactInfo = data.contactInfo.split(',');
        document.getElementById('phone').innerText = contactInfo[0]
        document.getElementById('email').innerText = contactInfo[1]
        const appointments = data.appointments;
        if (appointments.length !== 0) {
            for (const appointment of appointments) {
                const p = document.createElement('p');
                p.classList.add("d-block", "details-text", "text-center");
                const appointment_date_time = new Date(appointment.appointmentDateTime);
                p.innerText = appointment.patient.firstName + ' ' + appointment.patient.lastName + ', ' + formatDate(appointment_date_time) + ' at ' + formatTime(appointment_date_time);

                document.getElementById('upcoming-appointments').appendChild(p);
            }
        } else {
            const p = document.createElement('p');
            p.classList.add("h4", "m-3");
            p.innerText = "There is No appointments";
            if (document.getElementById('upcoming-appointments')) {
                document.getElementById('upcoming-appointments').appendChild(p);
            }

        }


    } else {
        console.log('No data returned for this doctor.');
    }
    await setCurrenDoctorPrivileges(doctor_id)


});

async function setCurrenDoctorPrivileges(doctor_id) {

    const current_user = await getcurrentUser();
    const card_footer = document.querySelector('.card-footer');
    if (current_user.userRoles.includes('ROLE_DOCTOR') && current_user.userId === doctor_id) {

        const update_btn = document.createElement('a');
        update_btn.className = 'btn btn-primary m-2';
        update_btn.innerText = 'Update';
        update_btn.href = '/doctors/update';

        const back_btn = document.createElement('a');
        back_btn.className = 'btn btn-danger m-2';
        back_btn.innerText = 'Back';
        back_btn.href = '/appointments/';
        card_footer.appendChild(update_btn)
        card_footer.appendChild(back_btn)
        document.getElementById('nav_block').className = 'd-block'

        // Availability Section
        const availability_section = document.createElement('div');
        availability_section.className = 'w-50'

        availability_section.innerHTML = '<i class="bi bi bi-clock-fill text-warning me-3 details-icons"></i>' +
            '<p class="d-inline-block details-text"> Availiabilties : </p>'

        // adding slots section
        const add_slot_btn_component = document.createElement('div')
        add_slot_btn_component.className = 'm-auto row w-50'

        const date_time_picker = document.createElement('input');
        date_time_picker.className = 'col-lg-10 col-sm-12 rounded-start slot-picker'
        date_time_picker.type = 'datetime-local';
        date_time_picker.step = '1800';


        const add_slot_btn = document.createElement('a');
        add_slot_btn.className = 'btn btn-primary bi bi-plus-circle rounded-start-0 rounded-end col-lg-2 p-2 ';


        add_slot_btn.addEventListener('click', async () => {
            const date_time = date_time_picker.value;
            console.log(date_time)
            if (date_time === undefined || date_time === null || date_time === '') {
                showToast('SLOT MUST BE PROVIDED!', 'FAIL');
            } else {
                const csrf_token = document.querySelector('meta[name="_csrf"]').getAttribute('content')
                const csrf_header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                const headers = {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    [csrf_header]: csrf_token
                }

                const add_slot_response =
                    await fetch(`http://localhost:8080/api/doctors/${doctor_id}/availability`,
                        {
                            method: 'POST',
                            headers: headers,
                            body: JSON.stringify({
                                slot: date_time
                            })
                        });
                const data = await add_slot_response.json();
                if (add_slot_response.status === HttpStatus.CREATED) {

                    showToast('SLOT ADDED!', 'SUCCESS');
                    console.log("'Added slot' : " + data)
                    add_slot_component(data, availability_section);


                } else {


                    showToast(data.exceptionMsg, 'FAIL')
                }


            }
        });

        add_slot_btn_component.appendChild(date_time_picker)
        add_slot_btn_component.appendChild(add_slot_btn)

        // slots components section
        const availability_response = await fetch(`http://localhost:8080/api/doctors/${doctor_id}/availability`);
        if (availability_response.status === HttpStatus.NO_CONTENT) {
            const no_content = document.createElement('div');
            no_content.innerText = 'No availability slots yet!';
            availability_section.appendChild(no_content)
        } else if (availability_response.status === HttpStatus.OK) {
            const slots = await availability_response.json();
            for (const slot of slots) {
                add_slot_component(slot, availability_section);
            }
        }
        const card_body = document.querySelector('.card-body');
        availability_section.appendChild(add_slot_btn_component);
        card_body.appendChild(availability_section);


    } else {
        const back_btn = document.createElement('a');
        back_btn.className = 'btn btn-danger m-2';
        back_btn.innerText = 'Back';
        back_btn.href = '/doctors/';
        card_footer.appendChild(back_btn)
    }


}

function add_slot_component(slot, availability_section) {
    const slot_component = document.createElement('div');
    slot_component.className = 'row mb-2 bg-info rounded w-50 m-auto'
    slot_component.id = slot.id;

    const slot_text = document.createElement('div');
    slot_text.className = 'text-center p-2 col-lg-10 col-sm-12 fw-bold p-0';

    const slot_date_time = new Date(slot.slot)
    slot_text.innerText = formatDate(slot_date_time) + ' ' + formatTime(slot_date_time);

    const slot_delete = document.createElement('a');
    slot_delete.className = 'btn btn-danger bi bi-trash col-lg-2 col-sm-12 rounded-start-0';
    slot_delete.addEventListener('click', async () => {
        const csrf_token = document.querySelector('meta[name="_csrf"]').getAttribute('content')
        const csrf_header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        const headers = {
            [csrf_header]: csrf_token
        }
        const slot_delete_response =
            await fetch(`http://localhost:8080/api/doctors/availability/${slot.id}`,
                {
                    method: 'DELETE',
                    headers: headers
                });
        if (slot_delete_response.status === HttpStatus.NOT_FOUND) {
            showToast('SLOT NOT FOUND !', 'FAIL')
        } else if (slot_delete_response.status === HttpStatus.NO_CONTENT) {
            availability_section.removeChild(slot_component);
            showToast("SLOT DELETED !", 'SUCCESS')
        }

    })

    slot_component.appendChild(slot_text);
    slot_component.appendChild(slot_delete);
    availability_section.appendChild(slot_component);
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
    const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const day = date.getDate();
    const monthIndex = date.getMonth();
    const year = date.getFullYear();

    return `${monthNames[monthIndex]} ${day}, ${year}`;
}

function showToast(message, bg_color) {
    let toastElement = document.querySelector('.toast');
    let toastBody = toastElement.querySelector('.toast-body');
    if (bg_color === 'FAIL') {
        toastElement.classList.add(' text-bg-danger');
        toastElement.classList.remove(' text-bg-success');

    } else if (bg_color === 'SUCCESS') {
        toastElement.classList.remove(' text-bg-danger');
        toastElement.classList.add(' text-bg-success');

    }
    toastBody.innerText = message;

    let toast = new bootstrap.Toast(toastElement, {
        autohide: false
    });
    toast.show();

}


async function getcurrentUser() {
    const response = await fetch('http://localhost:8080/api/auth/user/current');

    if (response.status === HttpStatus.UNAUTHORIZED) {
        window.location.href = '/signIn'
    } else if (response.status === HttpStatus.OK) {
        return await response.json();
    }

}

