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
    } else {
        const back_btn = document.createElement('a');
        back_btn.className = 'btn btn-danger m-2';
        back_btn.innerText = 'Back';
        back_btn.href = '/doctors/';
        card_footer.appendChild(back_btn)
    }


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
    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"];
    const day = date.getDate();
    const monthIndex = date.getMonth();
    const year = date.getFullYear();

    return `${monthNames[monthIndex]} ${day}, ${year}`;
}

async function getcurrentUser() {
    const response = await fetch('http://localhost:8080/api/auth/user/current');

    if (response.status === HttpStatus.UNAUTHORIZED) {
        window.location.href = '/signIn'
    } else if (response.status === HttpStatus.OK) {
        return await response.json();
    }

}
