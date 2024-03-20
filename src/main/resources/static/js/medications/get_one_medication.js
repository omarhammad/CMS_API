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
window.addEventListener('DOMContentLoaded', loadMedication);

async function loadMedication() {
    const pathname = window.location.pathname;
    const segments = pathname.split('/');
    const medication_id = segments.pop();
    try {
        const response = await fetch(`http://localhost:8080/api/medications/${medication_id}`);


        if (response.status === HttpStatus.NOT_FOUND) {
            console.log("NOT FOUND");
        } else if (response.ok) {
            const medication = await response.json();
            fillInPage(medication)
        }
    } catch (error) {
        console.error(error)
    }
}

function fillInPage(medication) {

    document.getElementById('name').innerText = medication.name;
    document.getElementById('form_usage').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;' + medication.form + ` - ${medication.usage}`;
    document.getElementById('dosage').innerText = medication.dosage.quantity + ` ${medication.dosage.unit}`;
    document.getElementById('freq').innerText = medication.frequencies + ' times per day';
    document.getElementById('duration').innerText = medication.daysDuration + ' days';
    document.getElementById('notes').innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;' + medication.notes
}